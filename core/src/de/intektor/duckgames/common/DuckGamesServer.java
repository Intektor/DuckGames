package de.intektor.duckgames.common;

import com.badlogic.gdx.graphics.Color;
import de.intektor.duckgames.client.editor.EditableGameMap;
import de.intektor.duckgames.common.chat.ServerInfoMessage;
import de.intektor.duckgames.common.entity.EntityPlayerMP;
import de.intektor.duckgames.common.net.*;
import de.intektor.duckgames.common.net.lan.ThreadLanServerPing;
import de.intektor.duckgames.common.net.server_to_client.*;
import de.intektor.duckgames.game.GameProfile;
import de.intektor.duckgames.game.GameScore;
import de.intektor.duckgames.world.WorldServer;
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
import java.net.SocketException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import static de.intektor.duckgames.common.CommonCode.packetHelper;

/**
 * @author Intektor
 */
public class DuckGamesServer implements Closeable {

    private volatile AbstractServerSocket serverSocket;

    private final int port;

    private volatile boolean serverRunning;

    private volatile List<AbstractSocket> socketList = Collections.synchronizedList(new ArrayList<AbstractSocket>());

    private volatile ServerState serverState;

    private MainServerThread mainServerThread;
    private ThreadLanServerPing pingThread;

    private volatile UpnpService upnpService;

    private AbstractSocket host;

    private boolean openedToLan;

    private HostingInfo hostingInfo;

    public DuckGamesServer(int port) {
        this.port = port;
    }

    public DuckGamesServer() {
        port = HttpUtils.getSuitableLanPort();
    }

    public void startServer(ServerState state, final HostingInfo hostingInfo) {
        this.hostingInfo = hostingInfo;
        serverRunning = true;
        serverState = state;
        new Thread("Server Thread") {
            @Override
            public void run() {
                try {
                    serverSocket = CommonCode.networking.createServerSocket(hostingInfo.hostingType, port);
                    serverSocket.setSoTimeout(0);
                    while (serverRunning) {
                        AbstractSocket clientSocket = serverSocket.accept();
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

    private void registerConnection(final AbstractSocket clientSocket) {
        socketList.add(clientSocket);
        new Thread("Server Socket Client Thread -> " + clientSocket.getInetAddress()) {
            @Override
            public void run() {
                try {
                    boolean socketRunning = true;
                    DataInputStream in = new DataInputStream(clientSocket.getInputStream());
                    while (serverRunning && !clientSocket.isClosed() && socketRunning) {
                        try {
                            IPacket packet = packetHelper.readPacket(in, Side.SERVER);
                            CommonCode.packetRegistry.getHandlerForPacketClass(packet.getClass()).newInstance().handlePacket(packet, clientSocket);
                        } catch (SocketException e) {
                            e.printStackTrace();
                            socketRunning = false;
                        }
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

    public void kickClient(AbstractSocket socket) {
        socketList.remove(socket);
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void broadcast(IPacket packet) {
        for (AbstractSocket socket : socketList) {
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

    public AbstractSocket getHost() {
        return host;
    }

    @Override
    public void close() throws IOException {
        try {
            broadcast(new KickClientFromServerPacketToClient("Server Shut Down!"));
        } catch (Exception e) {

        }
        serverRunning = false;
        if (upnpService != null) upnpService.shutdown();
        try {
            serverSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public enum ServerState {
        CONNECT_STATE,
        PLAY_STATE,
        LOBBY_STATE
    }

    public enum GameMode {
        COMPETITIVE_SOLO,
        TEST_WORLD,
        NONE
    }

    public MainServerThread getMainServerThread() {
        return mainServerThread;
    }

    public class MainServerThread extends Thread {

        private volatile Queue<Runnable> scheduledTasks = new LinkedBlockingQueue<Runnable>();

        private Map<AbstractSocket, PlayerProfile> profileMap = new ConcurrentHashMap<AbstractSocket, PlayerProfile>();

        private volatile long lastTimeTick;

        private GameScore currentGameScore;

        private GameMode gameMode = GameMode.NONE;

        WorldServer world;
        EditableGameMap backup;

        private long ticksAtRoundEnd;
        private boolean startNextRound;

        MainServerThread(DuckGamesServer server) {
            super("Main Server Thread");
        }

        @Override
        public void run() {
            while (serverRunning) {
                if (System.nanoTime() - lastTimeTick >= 50000000D) {
                    lastTimeTick = System.nanoTime();
                    Runnable task;
                    while ((task = scheduledTasks.poll()) != null) {
                        task.run();
                    }
                    serverTick();
                }
            }
        }

        public void addScheduledTask(Runnable task) {
            scheduledTasks.offer(task);
        }

        public void serverTick() {
            if (world != null) {
                world.updateWorld();
                if (startNextRound && world.getWorldTime() - ticksAtRoundEnd >= 60) {
                    startNextRound = false;
                    startNextRound(backup);
                }
            }
        }

        public void registrationMessageFromClient(AbstractSocket socket, String username) {
            if (serverState == ServerState.CONNECT_STATE || serverState == ServerState.LOBBY_STATE) {
                boolean isHost = profileMap.size() == 0;
                PlayerProfile profile = new PlayerProfile(new GameProfile(UUID.randomUUID(), username, isHost), socket);
                profileMap.put(socket, profile);
                packetHelper.sendPacket(new IdentificationSuccessfulPacketToClient(), socket);
                DuckGamesServer.this.broadcast(new PlayerProfilesPacketToClient(profile.gameProfile));
                if (serverState == ServerState.LOBBY_STATE) {
                    DuckGamesServer.this.broadcast(new PlayerJoinLobbyPacketToClient(profile.gameProfile.profileUUID));
                }
                for (PlayerProfile playerProfile : profileMap.values()) {
                    packetHelper.sendPacket(new PlayerProfilesPacketToClient(playerProfile.gameProfile), socket);
                }
                if (isHost) {
                    host = socket;
                    broadcast(new ChatMessagePacketToClient(new ServerInfoMessage("This server is running on port: " + port, Color.CYAN)));
                }
                broadcast(new ChatMessagePacketToClient(new ServerInfoMessage(username + " joined the server.", Color.GREEN)));
            } else {
                packetHelper.sendPacket(new KickClientFromServerPacketToClient("Can't join while game is running!"), socket);
                socketList.remove(socket);
            }
        }

        public Map<AbstractSocket, PlayerProfile> getProfileMap() {
            return profileMap;
        }

        public void launchGame(EditableGameMap map, GameMode gameMode) {
            this.gameMode = gameMode;
            backup = map;
            serverState = ServerState.PLAY_STATE;
            List<GameProfile> profileList = new ArrayList<GameProfile>();
            for (PlayerProfile playerProfile : profileMap.values()) {
                profileList.add(playerProfile.gameProfile);
            }
            currentGameScore = new GameScore(profileList);

            startNextRound(map);
        }

        void startNextRound(EditableGameMap map) {
            world = map.convertToWorld(DuckGamesServer.this);
            broadcast(new WorldPacketToClient(world.getWidth(), world.getHeight(), world.getBlockTable()));

            world.spawnPlayers();
            world.spawnEntities();

            broadcast(new GameScorePacketToClient(currentGameScore));
            broadcast(new FinishedWorldTransmissionPacketToClient());
        }

        public void endRound(WorldServer world, EntityPlayerMP winner) {
            if (gameMode != GameMode.TEST_WORLD && !startNextRound) {
                GameScore.PlayerScore winnersScore = currentGameScore.getScoreForProfile(winner.getProfile().gameProfile);
                winnersScore.setWonRounds(winnersScore.getWonRounds() + 1);
                broadcast(new GameScorePacketToClient(currentGameScore));
                broadcast(new RoundEndedPacketToClient(winner.getProfile().gameProfile));
                ticksAtRoundEnd = world.getWorldTime();
                startNextRound = true;
            }
        }

        public EditableGameMap getBackup() {
            return backup;
        }

        public GameMode getGameMode() {
            return gameMode;
        }
    }
}
