package de.intektor.duckgames.data_storage.storages;

import de.intektor.duckgames.data_storage.StorageBase;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author Intektor
 */
public class StorageLong<K> extends StorageBase<K, Long> {

    public StorageLong(K key) {
        super(key);
        value = 0L;
    }

    @Override
    public void writeToStream(DataOutputStream out) throws IOException {
        out.writeLong(value);
    }

    @Override
    public void readFromFile(DataInputStream in) throws IOException {
        value = in.readLong();
    }
}
