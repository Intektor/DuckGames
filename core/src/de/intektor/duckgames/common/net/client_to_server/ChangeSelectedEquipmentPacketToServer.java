package de.intektor.duckgames.common.net.client_to_server;

import de.intektor.duckgames.common.CommonCode;
import de.intektor.duckgames.common.net.AbstractSocket;
import de.intektor.duckgames.common.net.IPacket;
import de.intektor.duckgames.common.net.IPacketHandler;
import de.intektor.duckgames.common.server.DuckGamesServer;
import de.intektor.duckgames.entity.EntityEquipmentSlot;
import de.intektor.duckgames.entity.entities.EntityPlayer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author Intektor
 */
public class ChangeSelectedEquipmentPacketToServer implements IPacket {

    private EntityEquipmentSlot slot;

    public ChangeSelectedEquipmentPacketToServer(EntityEquipmentSlot slot) {
        this.slot = slot;
    }

    public ChangeSelectedEquipmentPacketToServer() {
    }

    @Override
    public void write(DataOutputStream out) throws IOException {
        out.writeInt(slot.ordinal());
    }

    @Override
    public void read(DataInputStream in) throws IOException {
        slot = EntityEquipmentSlot.values()[in.readInt()];
    }

    public static class Handler implements IPacketHandler<ChangeSelectedEquipmentPacketToServer> {

        @Override
        public void handlePacket(final ChangeSelectedEquipmentPacketToServer packet, final AbstractSocket socketFrom) {
            DuckGamesServer server = CommonCode.getDuckGamesServer();
            final DuckGamesServer.MainServerThread thread = server.getMainServerThread();
            thread.addScheduledTask(new Runnable() {
                @Override
                public void run() {
                    EntityPlayer player = thread.getProfileMap().get(socketFrom).gameProfile.player;
                    player.setCurrentSelectedEquipment(packet.slot);
                }
            });
        }
    }
}
