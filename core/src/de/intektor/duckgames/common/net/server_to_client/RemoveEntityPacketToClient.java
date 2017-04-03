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
public class RemoveEntityPacketToClient implements IPacket {

    public UUID entity;

    public RemoveEntityPacketToClient() {
    }

    public RemoveEntityPacketToClient(UUID entity) {
        this.entity = entity;
    }

    @Override
    public void write(DataOutputStream out) throws IOException {
        NetworkUtils.writeUUID(out, entity);
    }

    @Override
    public void read(DataInputStream in) throws IOException {
        entity = NetworkUtils.readUUID(in);
    }

    public static class Handler implements IPacketHandler<RemoveEntityPacketToClient> {

        @Override
        public void handlePacket(final RemoveEntityPacketToClient packet, AbstractSocket socketFrom) {
            CommonCode.proxy.handlePacket(packet, socketFrom);
        }
    }
}
