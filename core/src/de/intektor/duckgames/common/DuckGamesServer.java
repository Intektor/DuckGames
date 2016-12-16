package de.intektor.duckgames.common;

import de.intektor.duckgames.common.net.server_to_client.FinishedWorldTransmissionPacketToClient;
import de.intektor.duckgames.common.net.server_to_client.KickClientFromServerPacketToClient;
import de.intektor.duckgames.common.net.server_to_client.WorldPacketToClient;
import de.intektor.duckgames.editor.EditableGameMap;
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
import java.util.concurrent.LinkedBlockingQueue;

import static de.intektor.duckgames.common.GamePacketCombination.packetHelper;

/**
 * @author Intektor
 */
public class DuckGamesServer implements Closeable {

    private volatile ServerSocket serverSocket;

    private final int port;

    private volatile boolean serverRunning;

    private volatile List<Socket> socketList = Collections.synchronizedList(new ArrayList<Socket>());

    private ServerState serverState;

    private MainServerThread mainServerThread;

    public DuckGamesServer(int port) {
        this.port = port;
    }

    public DuckGamesServer() {
        port = 19473;
    }

    public void startServer() {
        serverRunning = true;
        serverState = ServerState.CONNECT_STATE;
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
                        GamePacketCombination.packetRegistry.getHandlerForPacketClass(packet.getClass()).newInstance().handlePacket(packet, clientSocket);
                    }
                } catch (PacketOnWrongSideException e) {
                    System.out.println("Client sent a server-to-client packet the server! Kicking client!");
                    kickClient(clientSocket);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }

    public void kickClient(Socket socket) {
        socketList.remove(socket);
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

    private enum ServerState {
        CONNECT_STATE,
        PLAY_STATE
    }

    public MainServerThread getMainServerThread() {
        return mainServerThread;
    }

    public class MainServerThread extends Thread {

        private volatile Queue<Runnable> scheduledTasks = new LinkedBlockingQueue<Runnable>();

        private Map<Socket, PlayerProfile> profileMap = new HashMap<Socket, PlayerProfile>();

        MainServerThread(DuckGamesServer server) {
            super("Main Server Thread");
        }

        @Override
        public void run() {
            Runnable task;
            while ((task = scheduledTasks.poll()) != null) {
                task.run();
            }
        }

        public void addScheduledTask(Runnable task) {
            scheduledTasks.offer(task);
        }

        public void registrationMessageFromClient(Socket socket, String username) {
            if (serverState == ServerState.CONNECT_STATE) {
                PlayerProfile profile = new PlayerProfile(username, socket);
                profileMap.put(socket, profile);
            } else {
                packetHelper.sendPacket(new KickClientFromServerPacketToClient("Can't join while game is running!"), socket);
                socketList.remove(socket);
            }
        }

        public Map<Socket, PlayerProfile> getProfileMap() {
            return profileMap;
        }

        public void launchGame(EditableGameMap map) {
            WorldServer world = map.convertToWorld(DuckGamesServer.this);
            messageEveryone(new WorldPacketToClient(world.getWidth(), world.getHeight(), world.getBlockTable()));
            world.spawnPlayers();
            messageEveryone(new FinishedWorldTransmissionPacketToClient());
        }
    }
}