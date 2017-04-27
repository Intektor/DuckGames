package de.intektor.duckgames.data_storage.storages;

import de.intektor.duckgames.data_storage.StorageBase;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author Intektor
 */
public class StorageShort<K> extends StorageBase<K, Short> {

    public StorageShort(K key) {
        super(key);
        value = 0;
    }

    @Override
    public void writeToStream(DataOutputStream out) throws IOException {
        out.writeShort(value);
    }

    @Override
    public void readFromFile(DataInputStream in) throws IOException {
        value = in.readShort();
    }
}
