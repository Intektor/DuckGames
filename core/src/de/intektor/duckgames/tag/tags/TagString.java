package de.intektor.duckgames.tag.tags;

import de.intektor.duckgames.tag.TagBase;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author Intektor
 */
public class TagString extends TagBase<String> {

    public TagString(String key) {
        super(key);
        value = "";
    }

    @Override
    public void writeToFile(DataOutputStream out) throws IOException {
        out.writeUTF(value);
    }

    @Override
    public void readFromFile(DataInputStream in) throws IOException {
        value = in.readUTF();
    }
}
