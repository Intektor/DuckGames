package de.intektor.duckgames.common.net.server_to_client;

import de.intektor.duckgames.common.CommonCode;
import de.intektor.duckgames.common.chat.ChatMessage;
import de.intektor.duckgames.common.net.AbstractSocket;
import de.intektor.duckgames.common.net.IPacket;
import de.intektor.duckgames.common.net.IPacketHandler;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author Intektor
 */
public class ChatMessagePacketToClient implements IPacket {

    public ChatMessage message;

    public ChatMessagePacketToClient(ChatMessage message) {
        this.message = message;
    }

    public ChatMessagePacketToClient() {
    }

    @Override
    public void write(DataOutputStream out) throws IOException {
        out.writeInt(CommonCode.chatMessageRegistry.getIdentifier(message.getClass()));
        message.writeToStream(out);
    }

    @Override
    public void read(DataInputStream in) throws IOException {
        try {
            message = CommonCode.chatMessageRegistry.createMessage(in.readInt());
            message.readFromStream(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class Handler implements IPacketHandler<ChatMessagePacketToClient> {

        @Override
        public void handlePacket(ChatMessagePacketToClient packet, AbstractSocket socket) {
            CommonCode.proxy.handlePacket(packet, socket);
        }
    }
}
