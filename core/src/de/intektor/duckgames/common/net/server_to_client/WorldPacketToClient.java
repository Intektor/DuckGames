package de.intektor.duckgames.common.net.server_to_client;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import de.intektor.duckgames.block.Block;
import de.intektor.duckgames.common.GameRegistry;
import de.intektor.duckgames.common.CommonCode;
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

    public int width;
    public int height;
    public Table<Integer, Integer, Block> blockTable;

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
        GameRegistry gameRegistry = CommonCode.gameRegistry;
        out.writeInt(width);
        out.writeInt(height);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                out.writeByte(gameRegistry.getBlockID(blockTable.get(x, y)));
            }
        }
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
    }

    public static class Handler implements IPacketHandler<WorldPacketToClient> {

        @Override
        public void handlePacket(final WorldPacketToClient packet, Socket socketFrom) {
            CommonCode.clientProxy.handlePacket(packet, socketFrom);
        }
    }
}
