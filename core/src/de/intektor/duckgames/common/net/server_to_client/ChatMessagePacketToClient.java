package de.intektor.duckgames.common.net.server_to_client;

import de.intektor.duckgames.common.CommonCode;
import de.intektor.duckgames.common.net.NetworkUtils;
import de.intektor.network.IPacket;
import de.intektor.network.IPacketHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.UUID;

/**
 * @author Intektor
 */
public class ChatMessagePacketToClient implements IPacket {

    public UUID profileUUID;
    public String message;

    public ChatMessagePacketToClient(UUID profileUUID, String message) {
        this.profileUUID = profileUUID;
        this.message = message;
    }

    public ChatMessagePacketToClient() {
    }

    @Override
    public void write(DataOutputStream out) throws IOException {
        NetworkUtils.writeUUID(out, profileUUID);
        out.writeUTF(message);
    }

    @Override
    public void read(DataInputStream in) throws IOException {
        profileUUID = NetworkUtils.readUUID(in);
        message = in.readUTF();
    }

    public static class Handler implements IPacketHandler<ChatMessagePacketToClient> {

        @Override
        public void handlePacket(ChatMessagePacketToClient packet, Socket socket) {
            CommonCode.clientProxy.handlePacket(packet, socket);
        }
    }
}
