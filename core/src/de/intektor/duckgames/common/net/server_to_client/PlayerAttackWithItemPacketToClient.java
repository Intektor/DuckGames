package de.intektor.duckgames.common.net.server_to_client;

import de.intektor.duckgames.common.CommonCode;
import de.intektor.duckgames.common.Status;
import de.intektor.duckgames.common.net.NetworkUtils;
import de.intektor.network.IPacket;
import de.intektor.network.IPacketHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.UUID;

/**
 * @author Intektor
 */
public class PlayerAttackWithItemPacketToClient implements IPacket {

    public UUID playerID;
    public float ingameClickX;
    public float ingameClickY;
    public Status status;

    public PlayerAttackWithItemPacketToClient(UUID playerID, float ingameClickX, float ingameClickY, Status status) {
        this.playerID = playerID;
        this.ingameClickX = ingameClickX;
        this.ingameClickY = ingameClickY;
        this.status = status;
    }

    public PlayerAttackWithItemPacketToClient() {
    }

    @Override
    public void write(DataOutputStream out) throws IOException {
        NetworkUtils.writeUUID(out, playerID);
        out.writeFloat(ingameClickX);
        out.writeFloat(ingameClickY);
        out.writeInt(status.ordinal());    }

    @Override
    public void read(DataInputStream in) throws IOException {
        playerID = NetworkUtils.readUUID(in);
        ingameClickX = in.readFloat();
        ingameClickY = in.readFloat();
        status = Status.values()[in.readInt()];
    }

    public static class Handler implements IPacketHandler<PlayerAttackWithItemPacketToClient> {

        @Override
        public void handlePacket(final PlayerAttackWithItemPacketToClient packet, Socket socketFrom) {
            CommonCode.clientProxy.handlePacket(packet, socketFrom);
        }
    }
}
