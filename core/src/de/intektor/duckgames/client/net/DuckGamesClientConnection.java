package de.intektor.duckgames.client.net;

import de.intektor.duckgames.common.CommonCode;
import de.intektor.duckgames.common.HostingType;
import de.intektor.duckgames.common.net.AbstractSocket;
import de.intektor.duckgames.common.net.IPacket;
import de.intektor.duckgames.common.net.PacketOnWrongSideException;
import de.intektor.duckgames.common.net.Side;
import de.intektor.duckgames.game.GameProfile;
import de.intektor.duckgames.game.GameScore;

import java.io.Closeable;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Intektor
 */
public class DuckGamesClientConnection implements Closeable {

    private volatile AbstractSocket clientSocket;
    private volatile boolean running;
    private boolean identificationSuccessful;

    private boolean connectionFailed;
    private Throwable connectionFailedProblem;

    private Map<UUID, GameProfile> playerProfiles = new HashMap<UUID, GameProfile>();

    private GameScore currentGameScore;

    public void connect(final String ip, final int port, final HostingType hostingType) {
        running = true;
        new Thread("Client Networking Thread to Server -> " + ip) {
            @Override
            public void run() {
                try {
                    clientSocket = CommonCode.networking.connect(hostingType, port, ip);

                    DataInputStream in = new DataInputStream(clientSocket.getInputStream());
                    while (running && !clientSocket.isClosed()) {
                        try {
                            IPacket packet = CommonCode.packetHelper.readPacket(in, Side.CLIENT);
                            CommonCode.packetRegistry.getHandlerForPacketClass(packet.getClass()).newInstance().handlePacket(packet, clientSocket);
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                            running = false;
                        }
                    }
                } catch (PacketOnWrongSideException e) {
                    System.out.println("Server sent a client-to-server packet! Disconnecting!");
                    try {
                        close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    connectionFailed = true;
                    connectionFailedProblem = e;
                }
            }
        }.start();
    }

    public boolean isConnected() {
        return clientSocket != null && clientSocket.isConnected();
    }

    public AbstractSocket getClientSocket() {
        return clientSocket;
    }

    public void setIdentificationSuccessful(boolean identificationSuccessful) {
        this.identificationSuccessful = identificationSuccessful;
    }

    public boolean isIdentificationSuccessful() {
        return identificationSuccessful;
    }

    @Override
    public void close() throws IOException {
        running = false;
        if (clientSocket != null) clientSocket.close();
    }

    public Map<UUID, GameProfile> getPlayerProfiles() {
        return playerProfiles;
    }

    public boolean connectionFailed() {
        return connectionFailed;
    }

    public Throwable getConnectionFailedProblem() {
        return connectionFailedProblem;
    }

    public void setCurrentGameScore(GameScore currentGameScore) {
        this.currentGameScore = currentGameScore;
    }

    public GameScore getCurrentGameScore() {
        return currentGameScore;
    }
}
