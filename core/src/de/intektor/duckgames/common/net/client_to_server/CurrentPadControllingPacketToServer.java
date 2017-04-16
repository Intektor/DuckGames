package de.intektor.duckgames.common.net.client_to_server;

import de.intektor.duckgames.common.CommonCode;
import de.intektor.duckgames.common.server.DuckGamesServer;
import de.intektor.duckgames.common.PlayerInputHandler;
import de.intektor.duckgames.common.PlayerProfile;
import de.intektor.duckgames.common.net.AbstractSocket;
import de.intektor.duckgames.common.net.IPacket;
import de.intektor.duckgames.common.net.IPacketHandler;
import de.intektor.duckgames.entity.entities.EntityPlayer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author Intektor
 */
public class CurrentPadControllingPacketToServer implements IPacket {

    private float movementAngle, movementStrength;
    private float aimingAngle, aimingStrength;

    public CurrentPadControllingPacketToServer(float movementAngle, float movementStrength, float aimingAngle, float aimingStrength) {
        this.movementAngle = movementAngle;
        this.movementStrength = movementStrength;
        this.aimingAngle = aimingAngle;
        this.aimingStrength = aimingStrength;
    }

    public CurrentPadControllingPacketToServer() {
    }

    @Override
    public void write(DataOutputStream out) throws IOException {
        out.writeFloat(movementAngle);
        out.writeFloat(movementStrength);
        out.writeFloat(aimingAngle);
        out.writeFloat(aimingStrength);
    }

    @Override
    public void read(DataInputStream in) throws IOException {
        movementAngle = in.readFloat();
        movementStrength = in.readFloat();
        aimingAngle = in.readFloat();
        aimingStrength = in.readFloat();
    }

    public static class Handler implements IPacketHandler<CurrentPadControllingPacketToServer> {

        @Override
        public void handlePacket(final CurrentPadControllingPacketToServer packet, final AbstractSocket socket) {
            final DuckGamesServer server = CommonCode.getDuckGamesServer();
            server.getMainServerThread().addScheduledTask(new Runnable() {
                @Override
                public void run() {
                    PlayerProfile playerProfile = server.getMainServerThread().getProfileMap().get(socket);
                    EntityPlayer player = playerProfile.gameProfile.player;
                    PlayerInputHandler.handlePadControl(player, packet.movementAngle, packet.movementStrength, packet.aimingAngle, packet.aimingStrength);
                }
            });
        }
    }
}
