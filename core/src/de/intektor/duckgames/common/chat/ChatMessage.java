package de.intektor.duckgames.common.chat;

import com.badlogic.gdx.graphics.Color;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author Intektor
 */
public interface ChatMessage {

    Color getMessageColor();

    String getMessage();

    void writeToStream(DataOutputStream out) throws IOException;

    void readFromStream(DataInputStream in) throws IOException;
}
