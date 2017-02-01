package de.intektor.duckgames.common;

import de.intektor.duckgames.client.editor.EditableGameMap;
import de.intektor.duckgames.common.net.server_to_client.*;
import de.intektor.duckgames.world.WorldServer;
import de.intektor.network.IPacket;
import de.intektor.network.PacketOnWrongSideException;
import de.intektor.network.Side;

import java.io.Closeable;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import static de.intektor.duckgames.common.CommonCode.packetHelper;

/**
 * @author Intektor
 */
public class DuckGamesServer implements Closeable {

    private volatile ServerSocket serverSocket;

    private final int port;

    private volatile boolean serverRunning;

    private volatile List<Socket> socketList = Collections.synchronizedList(new ArrayList<Socket>());

    private volatile ServerState serverState;
    private final ServerType type;

    private MainServerThread mainServerThread;

    public DuckGamesServer(int port, ServerType type) {
        this.port = port;
        this.type = type;
    }

    public DuckGamesServer(ServerType type) {
        port = 19473;
        this.type = type;
    }

    public void startServer(ServerState state) {
        serverRunning = true;
        serverState = state;
        new Thread("Server Thread") {
            @Override
            public void run() {
                try {
                    serverSocket = new ServerSocket(port);
                    serverSocket.setSoTimeout(0);
                    while (serverRunning) {
                        Socket clientSocket = serverSocket.accept();
                        registerConnection(clientSocket);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();

        mainServerThread = new MainServerThread(this);
        mainServerThread.start();
    }

    private void registerConnection(final Socket clientSocket) {
        socketList.add(clientSocket);
        new Thread("Server Socket Client Thread -> " + clientSocket.getInetAddress()) {
            @Override
            public void run() {
                try {
                    DataInputStream in = new DataInputStream(clientSocket.getInputStream());
                    while (serverRunning && !clientSocket.isClosed()) {
                        IPacket packet = packetHelper.readPacket(in, Side.SERVER);
                        CommonCode.packetRegistry.getHandlerForPacketClass(packet.getClass()).newInstance().handlePacket(packet, clientSocket);
                    }
                } catch (PacketOnWrongSideException e) {
                    System.out.println("Client sent a server-to-client packet the server! Kicking client!");
                    kickClient(clientSocket);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
        CommonCode.packetHelper.sendPacket(new RequestIdentificationPacketToClient(), clientSocket);
    }

    public void kickClient(Socket socket) {
        socketList.remove(socket);
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void messageEveryone(IPacket packet) {
        for (Socket socket : socketList) {
            packetHelper.sendPacket(packet, socket);
        }
    }

    public boolean isServerReadyForConnections() {
        return serverSocket != null && serverSocket.isBound();
    }

    @Override
    public void close() throws IOException {
        serverRunning = false;
        serverSocket.close();
    }

    public enum ServerState {
        CONNECT_STATE,
        PLAY_STATE,
        LOBBY_STATE
    }

    public enum ServerType {
        UPNP,
        LAN,
        BLUETOOTH
    }

    public MainServerThread getMainServerThread() {
        return mainServerThread;
    }

    public class MainServerThread extends Thread {

        private volatile Queue<Runnable> scheduledTasks = new LinkedBlockingQueue<Runnable>();

        private Map<Socket, PlayerProfile> profileMap = new ConcurrentHashMap<Socket, PlayerProfile>();

        MainServerThread(DuckGamesServer server) {
            super("Main Server Thread");
        }

        private volatile long lastTimeTick;

        WorldServer world;
        EditableGameMap backup;

        @Override
        public void run() {
            while (serverRunning) {
                if (System.currentTimeMillis() - lastTimeTick >= 15.625D) {
                    lastTimeTick = System.currentTimeMillis();
                    Runnable task;
                    while ((task = scheduledTasks.poll()) != null) {
                        task.run();
                    }
                    if (world != null) {
                        world.updateWorld();
                    }
                }
            }
        }

        public void addScheduledTask(Runnable task) {
            scheduledTasks.offer(task);
        }

        public void registrationMessageFromClient(Socket socket, String username) {
            if (serverState == ServerState.CONNECT_STATE || serverState == ServerState.LOBBY_STATE) {
                PlayerProfile profile = new PlayerProfile(username, socket, UUID.randomUUID());
                profileMap.put(socket, profile);
                packetHelper.sendPacket(new IdentificationSuccessfulPacketToClient(), socket);
                DuckGamesServer.this.messageEveryone(new PlayerProfilesPacketToClient(profile));
                if (serverState == ServerState.LOBBY_STATE) {
                    DuckGamesServer.this.messageEveryone(new PlayerJoinLobbyPacketToClient(profile.profileUUID));
                }
                for (PlayerProfile playerProfile : profileMap.values()) {
                    packetHelper.sendPacket(new PlayerProfilesPacketToClient(playerProfile), socket);
                }
            } else {
                packetHelper.sendPacket(new KickClientFromServerPacketToClient("Can't join while game is running!"), socket);
                socketList.remove(socket);
            }
        }

        public Map<Socket, PlayerProfile> getProfileMap() {
            return profileMap;
        }

        public void launchGame(EditableGameMap map) {
            backup = map;
            serverState = ServerState.PLAY_STATE;
            world = map.convertToWorld(DuckGamesServer.this);
            messageEveryone(new WorldPacketToClient(world.getWidth(), world.getHeight(), world.getBlockTable()));
            world.spawnPlayers();
            world.spawnEntities();
            messageEveryone(new FinishedWorldTransmissionPacketToClient());
        }

        public EditableGameMap getBackup() {
            return backup;
        }
    }
}
