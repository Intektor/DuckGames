package de.intektor.duckgames.common.net.client_to_server;

import de.intektor.duckgames.common.DuckGamesServer;
import de.intektor.duckgames.common.CommonCode;
import de.intektor.duckgames.common.net.AbstractSocket;
import de.intektor.duckgames.common.net.IPacket;
import de.intektor.duckgames.common.net.IPacketHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author Intektor
 */
public class IdentificationPacketToServer implements IPacket {

    private String username;

    public IdentificationPacketToServer() {
    }

    public IdentificationPacketToServer(String username) {
        this.username = username;
    }

    @Override
    public void write(DataOutputStream out) throws IOException {
        out.writeUTF(username);
    }

    @Override
    public void read(DataInputStream in) throws IOException {
        username = in.readUTF();
    }

    public static class Handler implements IPacketHandler<IdentificationPacketToServer> {

        @Override
        public void handlePacket(final IdentificationPacketToServer packet, final AbstractSocket socketFrom) {
            final DuckGamesServer.MainServerThread mainServerThread = CommonCode.getDuckGamesServer().getMainServerThread();
            mainServerThread.addScheduledTask(new Runnable() {
                @Override
                public void run() {
                    mainServerThread.registrationMessageFromClient(socketFrom, packet.username);
                }
            });
        }
    }
}
