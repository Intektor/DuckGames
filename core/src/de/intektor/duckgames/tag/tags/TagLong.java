package de.intektor.duckgames.tag.tags;

import de.intektor.duckgames.tag.TagBase;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author Intektor
 */
public class TagLong extends TagBase<Long> {

    public TagLong(String key) {
        super(key);
        value = 0L;
    }

    @Override
    public void writeToFile(DataOutputStream out) throws IOException {
        out.writeLong(value);
    }

    @Override
    public void readFromFile(DataInputStream in) throws IOException {
        value = in.readLong();
    }
}
