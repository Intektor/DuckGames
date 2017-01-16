package de.intektor.duckgames.common.net.client_to_server;

import de.intektor.duckgames.common.DuckGamesServer;
import de.intektor.duckgames.common.PlayerProfile;
import de.intektor.duckgames.common.CommonCode;
import de.intektor.duckgames.entity.EntityEquipmentSlot;
import de.intektor.duckgames.entity.entities.EntityPlayer;
import de.intektor.duckgames.item.ItemStack;
import de.intektor.duckgames.item.items.gun.ItemGun;
import de.intektor.network.IPacket;
import de.intektor.network.IPacketHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * @author Intektor
 */
public class ReloadPacketToServer implements IPacket {

    @Override
    public void write(DataOutputStream dataOutputStream) throws IOException {

    }

    @Override
    public void read(DataInputStream dataInputStream) throws IOException {

    }

    public static class Handler implements IPacketHandler<ReloadPacketToServer> {

        @Override
        public void handlePacket(ReloadPacketToServer reloadPacketToServer, final Socket socket) {
            final DuckGamesServer.MainServerThread thread = CommonCode.getDuckGamesServer().getMainServerThread();
            thread.addScheduledTask(new Runnable() {
                @Override
                public void run() {
                    PlayerProfile playerProfile = thread.getProfileMap().get(socket);
                    if (playerProfile != null) {
                        EntityPlayer player = playerProfile.player;
                        ItemStack equipment = player.getEquipment(EntityEquipmentSlot.MAIN_HAND);
                        if (equipment != null && equipment.getItem() instanceof ItemGun) {
                            ItemGun gun = (ItemGun) equipment.getItem();
                            gun.reload(equipment, player, player.worldObj);
                        }
                    }
                }
            });
        }
    }
}
