package de.intektor.duckgames.common.net.client_to_server;

import de.intektor.duckgames.common.CommonCode;
import de.intektor.duckgames.common.Status;
import de.intektor.duckgames.common.net.AbstractSocket;
import de.intektor.duckgames.common.net.IPacket;
import de.intektor.duckgames.common.net.IPacketHandler;
import de.intektor.duckgames.common.net.server_to_client.PlayerAttackWithItemPacketToClient;
import de.intektor.duckgames.common.server.DuckGamesServer;
import de.intektor.duckgames.entity.entities.EntityPlayer;
import de.intektor.duckgames.item.ItemStack;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

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
        public void handlePacket(final PlayerAttackWithItemPacketToServer packet, final AbstractSocket socketFrom) {
            final DuckGamesServer server = CommonCode.getDuckGamesServer();
            final DuckGamesServer.MainServerThread mainServerThread = server.getMainServerThread();
            mainServerThread.addScheduledTask(new Runnable() {
                @Override
                public void run() {
                    EntityPlayer player = mainServerThread.getProfileMap().get(socketFrom).gameProfile.player;
                    ItemStack currentEquip = player.getCurrentlySelectedEquipment();
                    if (currentEquip != null) {
                        float angle = (float) Math.atan2(packet.ingameClickY - (player.posY + player.getEyeHeight()), packet.ingameClickX - (player.posX + (player.getWidth() / 2)));
                        player.setAim(angle, packet.status == Status.START || packet.status == Status.UPDATE ? 1 : 0);
                        switch (packet.status) {
                            case START:
                                currentEquip.getItem().onAttackWithItemBegin(currentEquip, player, player.world, packet.ingameClickX, packet.ingameClickY);
                                break;
                            case END:
                                currentEquip.getItem().onAttackWithItemEnd(currentEquip, player, player.world, packet.ingameClickX, packet.ingameClickY);
                                break;
                        }
                        server.broadcast(new PlayerAttackWithItemPacketToClient(player.uuid, packet.ingameClickX, packet.ingameClickY, packet.status));
                    }
                }
            });
        }
    }
}
