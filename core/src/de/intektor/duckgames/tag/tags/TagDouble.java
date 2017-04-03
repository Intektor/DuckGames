package de.intektor.duckgames.tag.tags;

import de.intektor.duckgames.tag.TagBase;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author Intektor
 */
public class TagDouble extends TagBase<Double> {

    public TagDouble(String key) {
        super(key);
        value = 0D;
    }

    @Override
    public void writeToFile(DataOutputStream out) throws IOException {
        out.writeDouble(value);
    }

    @Override
    public void readFromFile(DataInputStream in) throws IOException {
        value = in.readDouble();
    }
}
