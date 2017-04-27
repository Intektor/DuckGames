package de.intektor.duckgames.common.net.server_to_client;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import de.intektor.duckgames.block.Block;
import de.intektor.duckgames.common.GameRegistry;
import de.intektor.duckgames.common.CommonCode;
import de.intektor.duckgames.common.net.AbstractSocket;
import de.intektor.duckgames.common.net.IPacket;
import de.intektor.duckgames.common.net.IPacketHandler;
import de.intektor.duckgames.common.server.DuckGamesServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author Intektor
 */
public class WorldPacketToClient implements IPacket {

    public int width;
    public int height;
    public Table<Integer, Integer, Block> blockTable;

    public DuckGamesServer.GameMode gameMode;

    public WorldPacketToClient() {
        blockTable = HashBasedTable.create();
    }

    public WorldPacketToClient(int width, int height, Table<Integer, Integer, Block> blockTable, DuckGamesServer.GameMode gameMode) {
        this.width = width;
        this.height = height;
        this.blockTable = blockTable;
        this.gameMode = gameMode;
    }

    @Override
    public void write(DataOutputStream out) throws IOException {
        GameRegistry gameRegistry = CommonCode.gameRegistry;
        out.writeInt(width);
        out.writeInt(height);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                out.writeByte(gameRegistry.getBlockID(blockTable.get(x, y)));
            }
        }
        out.writeInt(gameMode.ordinal());
    }

    @Override
    public void read(DataInputStream in) throws IOException {
        width = in.readInt();
        height = in.readInt();
        GameRegistry gameRegistry = CommonCode.gameRegistry;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Block block = gameRegistry.getBlock(in.readByte());
                blockTable.put(x, y, block);
            }
        }
        gameMode = DuckGamesServer.GameMode.values()[in.readInt()];
    }

    public static class Handler implements IPacketHandler<WorldPacketToClient> {

        @Override
        public void handlePacket(final WorldPacketToClient packet, AbstractSocket socketFrom) {
            CommonCode.proxy.handlePacket(packet, socketFrom);
        }
    }
}
