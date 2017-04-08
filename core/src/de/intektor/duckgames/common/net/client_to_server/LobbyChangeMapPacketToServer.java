package de.intektor.duckgames.common.net.client_to_server;

import de.intektor.duckgames.common.CommonCode;
import de.intektor.duckgames.common.DuckGamesServer;
import de.intektor.duckgames.common.net.AbstractSocket;
import de.intektor.duckgames.common.net.IPacket;
import de.intektor.duckgames.common.net.IPacketHandler;
import de.intektor.duckgames.common.net.server_to_client.LobbyChangeMapPacketToClient;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author Intektor
 */
public class LobbyChangeMapPacketToServer implements IPacket {

    private String mapName;

    public LobbyChangeMapPacketToServer(String mapName) {
        this.mapName = mapName;
    }

    public LobbyChangeMapPacketToServer() {
    }

    @Override
    public void write(DataOutputStream out) throws IOException {
        out.writeUTF(mapName);
    }

    @Override
    public void read(DataInputStream in) throws IOException {
        mapName = in.readUTF();
    }

    public static class Handler implements IPacketHandler<LobbyChangeMapPacketToServer> {

        @Override
        public void handlePacket(final LobbyChangeMapPacketToServer packet, final AbstractSocket socketFrom) {
            final DuckGamesServer server = CommonCode.getDuckGamesServer();
            DuckGamesServer.MainServerThread mainThread = server.getMainServerThread();
            mainThread.addScheduledTask(new Runnable() {
                @Override
                public void run() {
                    if (socketFrom == server.getHost()) {
                        server.broadcast(new LobbyChangeMapPacketToClient(packet.mapName));
                    }
                }
            });
        }
    }
}
