package de.intektor.duckgames.data_storage.storages;

import de.intektor.duckgames.data_storage.StorageBase;
import de.intektor.duckgames.data_storage.tag.TagCompound;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author Intektor
 */
public class StorageTagCompound<K> extends StorageBase<K, TagCompound> {

    public StorageTagCompound(K key) {
        super(key);
        value = new TagCompound();
    }

    @Override
    public void writeToStream(DataOutputStream out) throws IOException {
        value.writeToStream(out);
    }

    @Override
    public void readFromFile(DataInputStream in) throws IOException {
        value = new TagCompound();
        value.readFromStream(in);
    }
}
