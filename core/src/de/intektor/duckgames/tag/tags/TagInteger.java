package de.intektor.duckgames.tag.tags;

import de.intektor.duckgames.tag.TagBase;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author Intektor
 */
public class TagInteger extends TagBase<Integer> {

    public TagInteger(String key) {
        super(key);
        value = 0;
    }

    @Override
    public void writeToFile(DataOutputStream out) throws IOException {
        out.writeInt(value);
    }

    @Override
    public void readFromFile(DataInputStream in) throws IOException {
        value = in.readInt();
    }
}
