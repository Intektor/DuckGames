package de.intektor.duckgames.common.net.server_to_client;

import de.intektor.duckgames.common.CommonCode;
import de.intektor.duckgames.common.net.AbstractSocket;
import de.intektor.duckgames.common.net.IPacket;
import de.intektor.duckgames.common.net.IPacketHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author Intektor
 */
public class LobbyChangeMapPacketToClient implements IPacket {

    public String mapName;

    public LobbyChangeMapPacketToClient(String mapName) {
        this.mapName = mapName;
    }

    public LobbyChangeMapPacketToClient() {
    }

    @Override
    public void write(DataOutputStream out) throws IOException {
        out.writeUTF(mapName);
    }

    @Override
    public void read(DataInputStream in) throws IOException {
        mapName = in.readUTF();
    }

    public static class Handler implements IPacketHandler<LobbyChangeMapPacketToClient> {

        @Override
        public void handlePacket(LobbyChangeMapPacketToClient packet, AbstractSocket socketFrom) {
            CommonCode.proxy.handlePacket(packet, socketFrom);
        }
    }
}
