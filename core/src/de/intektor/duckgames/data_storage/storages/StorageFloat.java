package de.intektor.duckgames.data_storage.storages;

import de.intektor.duckgames.data_storage.StorageBase;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author Intektor
 */
public class StorageFloat<K> extends StorageBase<K, Float> {

    public StorageFloat(K key) {
        super(key);
        value = 0F;
    }

    @Override
    public void writeToStream(DataOutputStream out) throws IOException {
        out.writeFloat(value);
    }

    @Override
    public void readFromFile(DataInputStream in) throws IOException {
        value = in.readFloat();
    }
}
