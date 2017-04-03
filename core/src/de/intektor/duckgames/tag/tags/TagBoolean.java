package de.intektor.duckgames.tag.tags;

import de.intektor.duckgames.tag.TagBase;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author Intektor
 */
public class TagBoolean extends TagBase<Boolean> {

    public TagBoolean(String key) {
        super(key);
        value = false;
    }

    @Override
    public void writeToFile(DataOutputStream out) throws IOException {
        out.writeBoolean(value);
    }

    @Override
    public void readFromFile(DataInputStream in) throws IOException {
        value = in.readBoolean();
    }
}
