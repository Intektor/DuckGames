package de.intektor.duckgames.common.net.client_to_server;

import de.intektor.duckgames.common.DuckGamesServer;
import de.intektor.duckgames.common.CommonCode;
import de.intektor.duckgames.common.Status;
import de.intektor.duckgames.common.net.server_to_client.PlayerAttackWithItemPacketToClient;
import de.intektor.duckgames.entity.EntityEquipmentSlot;
import de.intektor.duckgames.entity.entities.EntityPlayer;
import de.intektor.duckgames.item.ItemStack;
import de.intektor.network.IPacket;
import de.intektor.network.IPacketHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * @author Intektor
 */
public class PlayerAttackWithItemPacketToServer implements IPacket {

    private float ingameClickX;
    private float ingameClickY;
    private Status status;

    public PlayerAttackWithItemPacketToServer(float ingameClickX, float ingameClickY, Status status) {
        this.ingameClickX = ingameClickX;
        this.ingameClickY = ingameClickY;
        this.status = status;
    }

    public PlayerAttackWithItemPacketToServer() {
    }

    @Override
    public void write(DataOutputStream out) throws IOException {
        out.writeFloat(ingameClickX);
        out.writeFloat(ingameClickY);
        out.writeInt(status.ordinal());
    }

    @Override
    public void read(DataInputStream in) throws IOException {
        ingameClickX = in.readFloat();
        ingameClickY = in.readFloat();
        status = Status.values()[in.readInt()];
    }

    public static class Handler implements IPacketHandler<PlayerAttackWithItemPacketToServer> {

        @Override
        public void handlePacket(final PlayerAttackWithItemPacketToServer packet, final Socket socketFrom) {
            final DuckGamesServer server = CommonCode.getDuckGamesServer();
            final DuckGamesServer.MainServerThread mainServerThread = server.getMainServerThread();
            mainServerThread.addScheduledTask(new Runnable() {
                @Override
                public void run() {
                    EntityPlayer player = mainServerThread.getProfileMap().get(socketFrom).player;
                    ItemStack mainHand = player.getEquipment(EntityEquipmentSlot.MAIN_HAND);
                    if (mainHand != null) {
                        player.setAttacking(packet.status, packet.ingameClickX, packet.ingameClickY);
                        switch (packet.status) {
                            case START:
                                mainHand.getItem().onAttackWithItemBegin(mainHand, player, player.world, packet.ingameClickX, packet.ingameClickY);
                                break;
                            case END:
                                mainHand.getItem().onAttackWithItemEnd(mainHand, player, player.world, packet.ingameClickX, packet.ingameClickY);
                                break;
                        }
                        server.broadcast(new PlayerAttackWithItemPacketToClient(player.uuid, packet.ingameClickX, packet.ingameClickY, packet.status));
                    }
                }
            });
        }
    }
}
