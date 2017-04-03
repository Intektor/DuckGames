package de.intektor.duckgames.common.net.server_to_client;

import de.intektor.duckgames.common.CommonCode;
import de.intektor.duckgames.common.net.AbstractSocket;
import de.intektor.duckgames.common.net.NetworkUtils;
import de.intektor.duckgames.entity.EntityEquipmentSlot;
import de.intektor.duckgames.item.ItemStack;
import de.intektor.duckgames.common.net.IPacket;
import de.intektor.duckgames.common.net.IPacketHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * @author Intektor
 */
public class PickupEquipmentItemStackPacketToClient implements IPacket {

    public EntityEquipmentSlot slot;
    public ItemStack pickedStack;
    public UUID entityID;

    public PickupEquipmentItemStackPacketToClient(UUID entityID, EntityEquipmentSlot slot, ItemStack pickedStack) {
        this.slot = slot;
        this.pickedStack = pickedStack;
        this.entityID = entityID;
    }

    public PickupEquipmentItemStackPacketToClient() {
    }

    @Override
    public void write(DataOutputStream out) throws IOException {
        NetworkUtils.writeUUID(out, entityID);
        out.writeInt(slot.ordinal());
        out.writeBoolean(pickedStack != null);
        if (pickedStack != null) {
            pickedStack.writeItemStackToStream(out);
        }
    }

    @Override
    public void read(DataInputStream in) throws IOException {
        entityID = NetworkUtils.readUUID(in);
        slot = EntityEquipmentSlot.values()[in.readInt()];
        if (in.readBoolean()) {
            pickedStack = ItemStack.readItemStackFromSteam(in);
        }
    }

    public static class Handler implements IPacketHandler<PickupEquipmentItemStackPacketToClient> {

        @Override
        public void handlePacket(final PickupEquipmentItemStackPacketToClient packet, AbstractSocket socketFrom) {
            CommonCode.proxy.handlePacket(packet, socketFrom);
        }
    }
}
