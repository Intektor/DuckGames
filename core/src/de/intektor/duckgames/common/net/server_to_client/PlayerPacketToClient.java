package de.intektor.duckgames.common.net.server_to_client;

import de.intektor.duckgames.common.CommonCode;
import de.intektor.duckgames.common.net.AbstractSocket;
import de.intektor.duckgames.common.net.NetworkUtils;
import de.intektor.duckgames.common.net.IPacket;
import de.intektor.duckgames.common.net.IPacketHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * @author Intektor
 */
public class PlayerPacketToClient implements IPacket {

    public UUID playerUUID;

    public PlayerPacketToClient(UUID playerUUID) {
        this.playerUUID = playerUUID;
    }

    public PlayerPacketToClient() {
    }

    @Override
    public void write(DataOutputStream out) throws IOException {
        NetworkUtils.writeUUID(out, playerUUID);
    }

    @Override
    public void read(DataInputStream in) throws IOException {
        playerUUID = NetworkUtils.readUUID(in);
    }

    public static class Handler implements IPacketHandler<PlayerPacketToClient> {

        @Override
        public void handlePacket(final PlayerPacketToClient packet, AbstractSocket socketFrom) {
            CommonCode.proxy.handlePacket(packet, socketFrom);
        }
    }
}
