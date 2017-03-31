package de.intektor.duckgames.client.net;

import de.intektor.duckgames.common.CommonCode;
import de.intektor.duckgames.game.GameProfile;
import de.intektor.network.IPacket;
import de.intektor.network.PacketOnWrongSideException;
import de.intektor.network.Side;

import java.io.Closeable;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Intektor
 */
public class DuckGamesClientConnection implements Closeable {

    private volatile Socket clientSocket;
    private volatile boolean running;
    private boolean identificationSuccessful;

    private boolean connectionFailed;
    private Throwable connectionFailedProblem;

    private Map<UUID, GameProfile> playerProfiles = new HashMap<UUID, GameProfile>();

    public void connect(final String ip, final int port) {
        running = true;
        new Thread("Client Connection Thread to Server -> " + ip) {
            @Override
            public void run() {
                try {
                    clientSocket = new Socket(ip, port);
                    DataInputStream in = new DataInputStream(clientSocket.getInputStream());
                    while (running && !clientSocket.isClosed()) {
                        IPacket packet = CommonCode.packetHelper.readPacket(in, Side.CLIENT);
                        CommonCode.packetRegistry.getHandlerForPacketClass(packet.getClass()).newInstance().handlePacket(packet, clientSocket);
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

    public Socket getClientSocket() {
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
}
