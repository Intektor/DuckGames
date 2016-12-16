package de.intektor.duckgames.common.net.server_to_client;

import de.intektor.duckgames.DuckGamesClient;
import de.intektor.duckgames.common.net.NetworkHelper;
import de.intektor.duckgames.entity.EntityPlayer;
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
public class PlayerPacketToClient implements IPacket {

    private UUID playerUUID;

    public PlayerPacketToClient(UUID playerUUID) {
        this.playerUUID = playerUUID;
    }

    public PlayerPacketToClient() {
    }

    @Override
    public void write(DataOutputStream out) throws IOException {
        NetworkHelper.writeUUID(out, playerUUID);
    }

    @Override
    public void read(DataInputStream in) throws IOException {
        playerUUID = NetworkHelper.readUUID(in);
    }

    public static class Handler implements IPacketHandler<PlayerPacketToClient> {

        @Override
        public void handlePacket(final PlayerPacketToClient packet, Socket socketFrom) {
            final DuckGamesClient duckGames = DuckGamesClient.getDuckGames();
            duckGames.addScheduledTask(new Runnable() {
                @Override
                public void run() {
                    duckGames.thePlayer = (EntityPlayer) duckGames.theWorld.getEntityByUUID(packet.playerUUID);
                }
            });
        }
    }
}
