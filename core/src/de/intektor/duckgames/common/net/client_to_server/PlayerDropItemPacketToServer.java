package de.intektor.duckgames.common.net.client_to_server;

import de.intektor.duckgames.common.DuckGamesServer;
import de.intektor.duckgames.common.CommonCode;
import de.intektor.duckgames.common.net.AbstractSocket;
import de.intektor.duckgames.common.net.server_to_client.DropEquipmentItemStackPacketToClient;
import de.intektor.duckgames.entity.EntityEquipmentSlot;
import de.intektor.duckgames.entity.entities.EntityItem;
import de.intektor.duckgames.entity.entities.EntityPlayer;
import de.intektor.duckgames.item.ItemStack;
import de.intektor.duckgames.common.net.IPacket;
import de.intektor.duckgames.common.net.IPacketHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author Intektor
 */
public class PlayerDropItemPacketToServer implements IPacket {

    private EntityEquipmentSlot slot;

    public PlayerDropItemPacketToServer(EntityEquipmentSlot slot) {
        this.slot = slot;
    }

    public PlayerDropItemPacketToServer() {
    }

    @Override
    public void write(DataOutputStream out) throws IOException {
        out.writeInt(slot.ordinal());
    }

    @Override
    public void read(DataInputStream in) throws IOException {
        slot = EntityEquipmentSlot.values()[in.readInt()];
    }

    public static class Handler implements IPacketHandler<PlayerDropItemPacketToServer> {

        @Override
        public void handlePacket(final PlayerDropItemPacketToServer packet, final AbstractSocket socketFrom) {
            final DuckGamesServer server = CommonCode.getDuckGamesServer();
            final DuckGamesServer.MainServerThread mainThread = server.getMainServerThread();
            mainThread.addScheduledTask(new Runnable() {
                @Override
                public void run() {
                    EntityPlayer player = mainThread.getProfileMap().get(socketFrom).gameProfile.player;
                    ItemStack equipment = player.getEquipment(packet.slot);
                    if (equipment != null) {
                        server.broadcast(new DropEquipmentItemStackPacketToClient(packet.slot, player.uuid));
                        EntityItem entity = new EntityItem(player.world, player.posX, player.posY, equipment, player);
                        player.world.spawnEntityInWorld(entity);
                        player.setEquipment(packet.slot, null);
                        equipment.getItem().onItemThrownAway(equipment, player, player.world);
                    }
                }
            });
        }
    }
}
