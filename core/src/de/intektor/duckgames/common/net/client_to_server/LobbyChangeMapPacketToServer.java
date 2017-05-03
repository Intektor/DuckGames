package de.intektor.duckgames.common.net.client_to_server;

import de.intektor.duckgames.client.editor.EditableGameMap;
import de.intektor.duckgames.common.CommonCode;
import de.intektor.duckgames.common.server.DuckGamesServer;
import de.intektor.duckgames.common.net.AbstractSocket;
import de.intektor.duckgames.common.net.IPacket;
import de.intektor.duckgames.common.net.IPacketHandler;
import de.intektor.duckgames.common.net.server_to_client.LobbyChangeMapPacketToClient;
import de.intektor.duckgames.data_storage.tag.TagCompound;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author Intektor
 */
public class LobbyChangeMapPacketToServer implements IPacket {

    private EditableGameMap map;

    public LobbyChangeMapPacketToServer(EditableGameMap map) {
        this.map = map;
    }

    public LobbyChangeMapPacketToServer() {
    }

    @Override
    public void write(DataOutputStream out) throws IOException {
        TagCompound tag = new TagCompound();
        map.writeToTag(tag);
        tag.writeToStream(out);
    }

    @Override
    public void read(DataInputStream in) throws IOException {
        TagCompound tag = new TagCompound();
        tag.readFromStream(in);
        map = EditableGameMap.readMapFromCompound(tag);
    }

    public static class Handler implements IPacketHandler<LobbyChangeMapPacketToServer> {

        @Override
        public void handlePacket(final LobbyChangeMapPacketToServer packet, final AbstractSocket socketFrom) {
            final DuckGamesServer server = CommonCode.getDuckGamesServer();
            final DuckGamesServer.MainServerThread mainThread = server.getMainServerThread();
            mainThread.addScheduledTask(new Runnable() {
                @Override
                public void run() {
                    if (socketFrom == server.getHost()) {
                        mainThread.changeMap(packet.map);
                        server.broadcast(new LobbyChangeMapPacketToClient(packet.map.getSaveName()));
                    }
                }
            });
        }
    }
}
