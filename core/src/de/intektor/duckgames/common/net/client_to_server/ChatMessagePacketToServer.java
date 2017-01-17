package de.intektor.duckgames.common.net.client_to_server;

import de.intektor.duckgames.common.DuckGamesServer;
import de.intektor.duckgames.common.PlayerProfile;
import de.intektor.duckgames.common.CommonCode;
import de.intektor.duckgames.common.net.server_to_client.ChatMessagePacketToClient;
import de.intektor.network.IPacket;
import de.intektor.network.IPacketHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * @author Intektor
 */
public class ChatMessagePacketToServer implements IPacket {

    private String message;

    public ChatMessagePacketToServer(String message) {
        this.message = message;
    }

    public ChatMessagePacketToServer() {
    }

    @Override
    public void write(DataOutputStream out) throws IOException {
        out.writeUTF(message);
    }

    @Override
    public void read(DataInputStream in) throws IOException {
        message = in.readUTF();
    }

    public static class Handler implements IPacketHandler<ChatMessagePacketToServer> {

        @Override
        public void handlePacket(final ChatMessagePacketToServer chatMessagePacketToServer, final Socket socket) {
            final DuckGamesServer server = CommonCode.getDuckGamesServer();
            server.getMainServerThread().addScheduledTask(new Runnable() {
                @Override
                public void run() {
                    PlayerProfile playerProfile = server.getMainServerThread().getProfileMap().get(socket);
                    server.messageEveryone(new ChatMessagePacketToClient(playerProfile.profileUUID, chatMessagePacketToServer.message));
                }
            });
        }
    }
}
