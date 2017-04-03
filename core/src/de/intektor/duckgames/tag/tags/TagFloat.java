package de.intektor.duckgames.tag.tags;

import de.intektor.duckgames.tag.TagBase;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author Intektor
 */
public class TagFloat extends TagBase<Float> {

    public TagFloat(String key) {
        super(key);
        value = 0F;
    }

    @Override
    public void writeToFile(DataOutputStream out) throws IOException {
        out.writeFloat(value);
    }

    @Override
    public void readFromFile(DataInputStream in) throws IOException {
        value = in.readFloat();
    }
}
