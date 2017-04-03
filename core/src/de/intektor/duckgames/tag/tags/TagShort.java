package de.intektor.duckgames.tag.tags;

import de.intektor.duckgames.tag.TagBase;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author Intektor
 */
public class TagShort extends TagBase<Short> {

    public TagShort(String key) {
        super(key);
        value = 0;
    }

    @Override
    public void writeToFile(DataOutputStream out) throws IOException {
        out.writeShort(value);
    }

    @Override
    public void readFromFile(DataInputStream in) throws IOException {
        value = in.readShort();
    }
}
