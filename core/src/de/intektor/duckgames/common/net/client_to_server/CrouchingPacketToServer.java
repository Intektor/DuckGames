package de.intektor.duckgames.common.net.client_to_server;

import de.intektor.duckgames.common.CommonCode;
import de.intektor.duckgames.common.net.AbstractSocket;
import de.intektor.duckgames.common.net.IPacket;
import de.intektor.duckgames.common.net.IPacketHandler;
import de.intektor.duckgames.common.server.DuckGamesServer;
import de.intektor.duckgames.entity.entities.EntityPlayer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author Intektor
 */
public class CrouchingPacketToServer implements IPacket {

    private boolean start;

    public CrouchingPacketToServer(boolean start) {
        this.start = start;
    }

    public CrouchingPacketToServer() {
    }

    @Override
    public void write(DataOutputStream out) throws IOException {
        out.writeBoolean(start);
    }

    @Override
    public void read(DataInputStream in) throws IOException {
        start = in.readBoolean();
    }

    public static class Handler implements IPacketHandler<CrouchingPacketToServer> {

        @Override
        public void handlePacket(final CrouchingPacketToServer packet, final AbstractSocket socketFrom) {
            DuckGamesServer server = CommonCode.getDuckGamesServer();
            final DuckGamesServer.MainServerThread mainServerThread = server.getMainServerThread();
            mainServerThread.addScheduledTask(new Runnable() {
                @Override
                public void run() {
                    EntityPlayer player = mainServerThread.getProfileMap().get(socketFrom).gameProfile.player;
                    player.isCrouching = packet.start;
                }
            });
        }
    }
}
