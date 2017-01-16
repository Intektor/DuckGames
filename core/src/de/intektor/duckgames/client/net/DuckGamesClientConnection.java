package de.intektor.duckgames.client.net;

import de.intektor.duckgames.common.CommonCode;
import de.intektor.duckgames.common.PlayerProfile;
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

    private Map<UUID, PlayerProfile> playerProfiles = new HashMap<UUID, PlayerProfile>();

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
        clientSocket.close();
    }

    public Map<UUID, PlayerProfile> getPlayerProfiles() {
        return playerProfiles;
    }
}
