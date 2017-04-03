package de.intektor.duckgames.common.net.server_to_client;

import de.intektor.duckgames.common.CommonCode;
import de.intektor.duckgames.common.net.AbstractSocket;
import de.intektor.duckgames.common.net.NetworkUtils;
import de.intektor.duckgames.entity.EntityEquipmentSlot;
import de.intektor.duckgames.common.net.IPacket;
import de.intektor.duckgames.common.net.IPacketHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * @author Intektor
 */
public class DropEquipmentItemStackPacketToClient implements IPacket {

    public EntityEquipmentSlot slot;
    public UUID entityUUID;

    public DropEquipmentItemStackPacketToClient(EntityEquipmentSlot slot, UUID entityUUID) {
        this.slot = slot;
        this.entityUUID = entityUUID;
    }

    public DropEquipmentItemStackPacketToClient() {
    }

    @Override
    public void write(DataOutputStream out) throws IOException {
        NetworkUtils.writeUUID(out, entityUUID);
        out.writeInt(slot.ordinal());
    }

    @Override
    public void read(DataInputStream in) throws IOException {
        entityUUID = NetworkUtils.readUUID(in);
        slot = EntityEquipmentSlot.values()[in.readInt()];
    }

    public static class Handler implements IPacketHandler<DropEquipmentItemStackPacketToClient> {

        @Override
        public void handlePacket(final DropEquipmentItemStackPacketToClient packet, AbstractSocket socketFrom) {
            CommonCode.proxy.handlePacket(packet, socketFrom);
        }
    }
}
