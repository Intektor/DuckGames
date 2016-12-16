package de.intektor.duckgames.common.net.server_to_client;

import de.intektor.duckgames.DuckGamesClient;
import de.intektor.duckgames.block.Block;
import de.intektor.network.IPacket;
import de.intektor.network.IPacketHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * @author Intektor
 */
public class PlaceBlockPacketToClient implements IPacket {

    private Block block;
    private int x, y;

    public PlaceBlockPacketToClient() {
    }

    public PlaceBlockPacketToClient(Block block, int x, int y) {
        this.block = block;
        this.x = x;
        this.y = y;
    }

    @Override
    public void write(DataOutputStream out) throws IOException {
        out.writeInt(x);
        out.writeInt(y);
        out.writeByte(DuckGamesClient.getDuckGames().getBlockRegistry().getID(block));
    }

    @Override
    public void read(DataInputStream in) throws IOException {
        x = in.readInt();
        y = in.readInt();
        block = DuckGamesClient.getDuckGames().getBlockRegistry().getBlock(in.readByte());
    }

    public static class Handler implements IPacketHandler<PlaceBlockPacketToClient> {

        @Override
        public void handlePacket(final PlaceBlockPacketToClient packet, Socket socketFrom) {
            final DuckGamesClient duckGames = DuckGamesClient.getDuckGames();
            duckGames.addScheduledTask(new Runnable() {
                @Override
                public void run() {
                    duckGames.theWorld.setBlock(packet.x, packet.y, packet.block);
                }
            });
        }
    }
}
