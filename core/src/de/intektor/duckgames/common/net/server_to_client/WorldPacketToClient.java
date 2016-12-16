package de.intektor.duckgames.common.net.server_to_client;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import de.intektor.duckgames.DuckGamesClient;
import de.intektor.duckgames.block.Block;
import de.intektor.duckgames.block.BlockRegistry;
import de.intektor.duckgames.client.gui.guis.GuiDownloadingMap;
import de.intektor.duckgames.world.WorldClient;
import de.intektor.network.IPacket;
import de.intektor.network.IPacketHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * @author Intektor
 */
public class WorldPacketToClient implements IPacket {

    private int width;
    private int height;
    private Table<Integer, Integer, Block> blockTable;

    public WorldPacketToClient() {
        blockTable = HashBasedTable.create();
    }

    public WorldPacketToClient(int width, int height, Table<Integer, Integer, Block> blockTable) {
        this.width = width;
        this.height = height;
        this.blockTable = blockTable;
    }

    @Override
    public void write(DataOutputStream out) throws IOException {
        BlockRegistry blockRegistry = DuckGamesClient.getDuckGames().getBlockRegistry();
        out.writeInt(width);
        out.writeInt(height);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                out.writeByte(blockRegistry.getID(blockTable.get(x, y)));
            }
        }
    }

    @Override
    public void read(DataInputStream in) throws IOException {
        width = in.readInt();
        height = in.readInt();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Block block = DuckGamesClient.getDuckGames().getBlockRegistry().getBlock(in.readByte());
                blockTable.put(x, y, block);
            }
        }
    }

    public static class Handler implements IPacketHandler<WorldPacketToClient> {

        @Override
        public void handlePacket(final WorldPacketToClient packet, Socket socketFrom) {
            final DuckGamesClient duckGames = DuckGamesClient.getDuckGames();
            duckGames.addScheduledTask(new Runnable() {
                @Override
                public void run() {
                    duckGames.showGui(new GuiDownloadingMap());
                    duckGames.theWorld = new WorldClient(packet.blockTable, packet.width, packet.height);
                }
            });
        }
    }
}
