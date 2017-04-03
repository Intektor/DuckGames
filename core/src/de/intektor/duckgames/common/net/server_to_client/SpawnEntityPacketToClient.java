package de.intektor.duckgames.common.net.server_to_client;

import de.intektor.duckgames.common.CommonCode;
import de.intektor.duckgames.common.net.AbstractSocket;
import de.intektor.duckgames.entity.Entity;
import de.intektor.duckgames.common.net.IPacket;
import de.intektor.duckgames.common.net.IPacketHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author Intektor
 */
public class SpawnEntityPacketToClient implements IPacket {

    public Entity entity;

    public SpawnEntityPacketToClient(Entity entity) {
        this.entity = entity;
    }

    public SpawnEntityPacketToClient() {
    }

    @Override
    public void write(DataOutputStream out) throws IOException {
        entity.writeEntityToStream(out);
    }

    @Override
    public void read(DataInputStream in) throws IOException {
        entity = Entity.readEntityFromStream(in, CommonCode.proxy.getWorld());
    }

    public static class Handler implements IPacketHandler<SpawnEntityPacketToClient> {

        @Override
        public void handlePacket(final SpawnEntityPacketToClient packet, AbstractSocket socketFrom) {
            CommonCode.proxy.handlePacket(packet, socketFrom);
        }
    }
}
