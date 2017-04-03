package de.intektor.duckgames.common.chat;

import com.badlogic.gdx.graphics.Color;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author Intektor
 */
public class ServerInfoMessage implements ChatMessage {

    private String message;
    private Color chatColor;

    public ServerInfoMessage(String message, Color chatColor) {
        this.message = message;
        this.chatColor = chatColor;
    }

    public ServerInfoMessage() {
    }

    @Override
    public Color getMessageColor() {
        return chatColor;
    }

    @Override
    public String getMessage() {
        return "[Server]: " + message;
    }

    @Override
    public void writeToStream(DataOutputStream out) throws IOException {
        out.writeUTF(message);
        out.writeFloat(chatColor.r);
        out.writeFloat(chatColor.g);
        out.writeFloat(chatColor.b);
        out.writeFloat(chatColor.a);
    }

    @Override
    public void readFromStream(DataInputStream in) throws IOException {
        message = in.readUTF();
        chatColor = new Color(in.readFloat(), in.readFloat(), in.readFloat(), in.readFloat());
    }
}
