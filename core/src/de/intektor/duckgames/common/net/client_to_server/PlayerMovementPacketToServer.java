package de.intektor.duckgames.common.net.client_to_server;

import de.intektor.duckgames.common.DuckGamesServer;
import de.intektor.duckgames.common.PlayerProfile;
import de.intektor.duckgames.common.CommonCode;
import de.intektor.duckgames.common.net.AbstractSocket;
import de.intektor.duckgames.util.EnumDirection;
import de.intektor.duckgames.common.net.IPacket;
import de.intektor.duckgames.common.net.IPacketHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author Intektor
 */
public class PlayerMovementPacketToServer implements IPacket {

    private boolean start;
    private EnumDirection direction;

    public PlayerMovementPacketToServer(boolean start, EnumDirection direction) {
        this.start = start;
        this.direction = direction;
    }

    public PlayerMovementPacketToServer() {
    }

    @Override
    public void write(DataOutputStream out) throws IOException {
        out.writeBoolean(start);
        out.writeInt(direction.ordinal());
    }

    @Override
    public void read(DataInputStream in) throws IOException {
        start = in.readBoolean();
        direction = EnumDirection.values()[in.readInt()];
    }

    public static class Handler implements IPacketHandler<PlayerMovementPacketToServer> {

        @Override
        public void handlePacket(final PlayerMovementPacketToServer packet, final AbstractSocket socketFrom) {
            final DuckGamesServer.MainServerThread mainThread = CommonCode.getDuckGamesServer().getMainServerThread();
            mainThread.addScheduledTask(new Runnable() {
                @Override
                public void run() {
                    PlayerProfile profile = mainThread.getProfileMap().get(socketFrom);
                    profile.gameProfile.player.move(packet.direction, packet.start);
                }
            });
        }
    }
}
