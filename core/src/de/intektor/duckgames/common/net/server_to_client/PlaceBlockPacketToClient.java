package de.intektor.duckgames.common.net.server_to_client;

import de.intektor.duckgames.block.Block;
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
public class PlaceBlockPacketToClient implements IPacket {

    public Block block;
    public int x, y;

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
        out.writeByte(CommonCode.gameRegistry.getBlockID(block));
    }

    @Override
    public void read(DataInputStream in) throws IOException {
        x = in.readInt();
        y = in.readInt();
        block = CommonCode.gameRegistry.getBlock(in.readByte());
    }

    public static class Handler implements IPacketHandler<PlaceBlockPacketToClient> {

        @Override
        public void handlePacket(final PlaceBlockPacketToClient packet, Socket socketFrom) {
            CommonCode.clientProxy.handlePacket(packet, socketFrom);
        }
    }
}
