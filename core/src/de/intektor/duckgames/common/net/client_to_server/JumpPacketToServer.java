package de.intektor.duckgames.common.net.client_to_server;

import de.intektor.duckgames.common.server.DuckGamesServer;
import de.intektor.duckgames.common.CommonCode;
import de.intektor.duckgames.common.net.AbstractSocket;
import de.intektor.duckgames.entity.entities.EntityPlayer;
import de.intektor.duckgames.common.net.IPacket;
import de.intektor.duckgames.common.net.IPacketHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author Intektor
 */
public class JumpPacketToServer implements IPacket {

    private boolean start;

    public JumpPacketToServer(boolean start) {
        this.start = start;
    }

    public JumpPacketToServer() {
    }

    @Override
    public void write(DataOutputStream out) throws IOException {
        out.writeBoolean(start);
    }

    @Override
    public void read(DataInputStream in) throws IOException {
        start = in.readBoolean();
    }

    public static class Handler implements IPacketHandler<JumpPacketToServer> {

        @Override
        public void handlePacket(final JumpPacketToServer packet, final AbstractSocket socketFrom) {
            final DuckGamesServer.MainServerThread main = CommonCode.getDuckGamesServer().getMainServerThread();
            main.addScheduledTask(new Runnable() {
                @Override
                public void run() {
                    EntityPlayer player = main.getProfileMap().get(socketFrom).gameProfile.player;
                    player.setJumping(packet.start);
                }
            });
        }
    }
}
