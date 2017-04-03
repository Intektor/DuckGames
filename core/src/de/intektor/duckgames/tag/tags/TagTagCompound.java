package de.intektor.duckgames.tag.tags;

import de.intektor.duckgames.tag.TagBase;
import de.intektor.duckgames.tag.TagCompound;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author Intektor
 */
public class TagTagCompound extends TagBase<TagCompound> {

    public TagTagCompound(String key) {
        super(key);
        value = new TagCompound();
    }

    @Override
    public void writeToFile(DataOutputStream out) throws IOException {
        value.writeToStream(out);
    }

    @Override
    public void readFromFile(DataInputStream in) throws IOException {
        value = new TagCompound();
        value.readFromStream(in);
    }
}
