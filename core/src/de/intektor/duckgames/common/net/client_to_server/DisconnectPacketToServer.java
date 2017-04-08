package de.intektor.duckgames.common.net.client_to_server;

import com.badlogic.gdx.graphics.Color;
import de.intektor.duckgames.common.CommonCode;
import de.intektor.duckgames.common.DuckGamesServer;
import de.intektor.duckgames.common.PlayerProfile;
import de.intektor.duckgames.common.chat.ServerInfoMessage;
import de.intektor.duckgames.common.net.AbstractSocket;
import de.intektor.duckgames.common.net.IPacket;
import de.intektor.duckgames.common.net.IPacketHandler;
import de.intektor.duckgames.common.net.server_to_client.ChatMessagePacketToClient;
import de.intektor.duckgames.common.net.server_to_client.RemoveProfilePacketToClient;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author Intektor
 */
public class DisconnectPacketToServer implements IPacket {
    @Override
    public void write(DataOutputStream dataOutputStream) throws IOException {

    }

    @Override
    public void read(DataInputStream dataInputStream) throws IOException {

    }

    public static class Handler implements IPacketHandler<DisconnectPacketToServer> {

        @Override
        public void handlePacket(final DisconnectPacketToServer packet, final AbstractSocket socket) {
            final DuckGamesServer server = CommonCode.getDuckGamesServer();
            server.getMainServerThread().addScheduledTask(new Runnable() {
                @Override
                public void run() {
                    PlayerProfile playerProfile = server.getMainServerThread().getProfileMap().get(socket);
                    server.broadcast(new ChatMessagePacketToClient(new ServerInfoMessage(playerProfile.gameProfile.username + " left the server!", Color.RED)));
                    server.broadcast(new RemoveProfilePacketToClient(playerProfile.gameProfile));
                    server.kickClient(socket);
                }
            });
        }
    }
}
