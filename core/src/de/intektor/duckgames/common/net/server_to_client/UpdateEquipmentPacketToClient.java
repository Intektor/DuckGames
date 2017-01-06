package de.intektor.duckgames.common.net.server_to_client;

import de.intektor.duckgames.common.SharedGameRegistries;
import de.intektor.duckgames.common.net.NetworkUtils;
import de.intektor.duckgames.entity.EntityEquipmentSlot;
import de.intektor.duckgames.item.ItemStack;
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
public class UpdateEquipmentPacketToClient implements IPacket {

    public UUID playerUUID;
    public ItemStack stack;
    public EntityEquipmentSlot slot;

    public UpdateEquipmentPacketToClient(UUID playerUUID, ItemStack stack, EntityEquipmentSlot slot) {
        this.playerUUID = playerUUID;
        this.stack = stack;
        this.slot = slot;
    }

    public UpdateEquipmentPacketToClient() {
    }

    @Override
    public void write(DataOutputStream out) throws IOException {
        NetworkUtils.writeUUID(out, playerUUID);
        out.writeInt(slot.ordinal());
        stack.writeItemStackToStream(out);
    }

    @Override
    public void read(DataInputStream in) throws IOException {
        playerUUID = NetworkUtils.readUUID(in);
        slot = EntityEquipmentSlot.values()[in.readInt()];
        stack = ItemStack.readItemStackFromSteam(in);
    }

    public static class Handler implements IPacketHandler<UpdateEquipmentPacketToClient> {

        @Override
        public void handlePacket(UpdateEquipmentPacketToClient packet, Socket socketFrom) {
            SharedGameRegistries.clientProxy.handlePacket(packet, socketFrom);
        }
    }
}
