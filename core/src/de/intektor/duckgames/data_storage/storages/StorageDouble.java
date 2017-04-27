package de.intektor.duckgames.data_storage.storages;

import de.intektor.duckgames.data_storage.StorageBase;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author Intektor
 */
public class StorageDouble<K> extends StorageBase<K, Double> {

    public StorageDouble(K key) {
        super(key);
        value = 0D;
    }

    @Override
    public void writeToStream(DataOutputStream out) throws IOException {
        out.writeDouble(value);
    }

    @Override
    public void readFromFile(DataInputStream in) throws IOException {
        value = in.readDouble();
    }
}
