package de.intektor.duckgames.common;

import de.intektor.duckgames.client.editor.EditableGameMap;
import de.intektor.duckgames.common.net.lan.ThreadLanServerPing;
import de.intektor.duckgames.common.net.server_to_client.*;
import de.intektor.duckgames.game.GameProfile;
import de.intektor.duckgames.game.GameScore;
import de.intektor.duckgames.world.WorldServer;
import de.intektor.network.IPacket;
import de.intektor.network.PacketOnWrongSideException;
import de.intektor.network.Side;
import org.fourthline.cling.UpnpService;
import org.fourthline.cling.UpnpServiceImpl;
import org.fourthline.cling.model.types.UnsignedIntegerFourBytes;
import org.fourthline.cling.model.types.UnsignedIntegerTwoBytes;
import org.fourthline.cling.registry.RegistryListener;
import org.fourthline.cling.support.igd.PortMappingListener;
import org.fourthline.cling.support.model.PortMapping;

import java.io.Closeable;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
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

    private MainServerThread mainServerThread;
    private ThreadLanServerPing pingThread;

    private volatile UpnpService upnpService;

    private boolean openedToLan;

    private HostingInfo hostingInfo;

    public DuckGamesServer(int port) {
        this.port = port;
    }

    public DuckGamesServer() {
        port = 19473;
    }

    public void startServer(ServerState state, HostingInfo hostingInfo) {
        this.hostingInfo = hostingInfo;
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

        shareToLan();
        if (hostingInfo.hostingType == HostingType.INTERNET) {
            shareToInternet(hostingInfo.port);
        }
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

    public void broadcast(IPacket packet) {
        for (Socket socket : socketList) {
            packetHelper.sendPacket(packet, socket);
        }
    }

    private void shareToLan() {
        try {
            pingThread = new ThreadLanServerPing(this);
            pingThread.start();
            openedToLan = true;
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    private void shareToInternet(int port) {
        try {
            upnpService = new UpnpServiceImpl();
            RegistryListener registryListener = new PortMappingListener(new PortMapping(true, new UnsignedIntegerFourBytes(60 * 60 * 4),
                    null, new UnsignedIntegerTwoBytes(port), new UnsignedIntegerTwoBytes(this.port),
                    InetAddress.getLocalHost().getHostAddress(), PortMapping.Protocol.TCP, "Port Mapping"));
            upnpService.getRegistry().addListener(registryListener);

            upnpService.getControlPoint().search();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public ServerState getServerState() {
        return serverState;
    }

    public int getPort() {
        return serverSocket.getLocalPort();
    }

    public boolean isServerReadyForConnections() {
        return serverSocket != null && serverSocket.isBound();
    }

    @Override
    public void close() throws IOException {
        serverRunning = false;
        serverSocket.close();
        upnpService.shutdown();
    }

    public enum ServerState {
        CONNECT_STATE,
        PLAY_STATE,
        LOBBY_STATE
    }

    public enum HostingType {
        LAN,
        INTERNET
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

        private GameScore currentGameScore;

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
                PlayerProfile profile = new PlayerProfile(new GameProfile(UUID.randomUUID(), username), socket);
                profileMap.put(socket, profile);
                packetHelper.sendPacket(new IdentificationSuccessfulPacketToClient(), socket);
                DuckGamesServer.this.broadcast(new PlayerProfilesPacketToClient(profile.gameProfile));
                if (serverState == ServerState.LOBBY_STATE) {
                    DuckGamesServer.this.broadcast(new PlayerJoinLobbyPacketToClient(profile.gameProfile.profileUUID));
                }
                for (PlayerProfile playerProfile : profileMap.values()) {
                    packetHelper.sendPacket(new PlayerProfilesPacketToClient(playerProfile.gameProfile), socket);
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
            broadcast(new WorldPacketToClient(world.getWidth(), world.getHeight(), world.getBlockTable()));
            world.spawnPlayers();
            world.spawnEntities();
            List<GameProfile> profileList = new ArrayList<GameProfile>();
            for (PlayerProfile playerProfile : profileMap.values()) {
                profileList.add(playerProfile.gameProfile);
            }
            currentGameScore = new GameScore(profileList);
            broadcast(new FinishedWorldTransmissionPacketToClient());
        }

        public EditableGameMap getBackup() {
            return backup;
        }
    }
}
