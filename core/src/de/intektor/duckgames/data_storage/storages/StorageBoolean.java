package de.intektor.duckgames.data_storage.storages;

import de.intektor.duckgames.data_storage.StorageBase;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author Intektor
 */
public class StorageBoolean<K> extends StorageBase<K, Boolean> {

    public StorageBoolean(K key) {
        super(key);
        value = false;
    }

    @Override
    public void writeToStream(DataOutputStream out) throws IOException {
        out.writeBoolean(value);
    }

    @Override
    public void readFromFile(DataInputStream in) throws IOException {
        value = in.readBoolean();
    }
}
