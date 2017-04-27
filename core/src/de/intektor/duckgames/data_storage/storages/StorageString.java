package de.intektor.duckgames.data_storage.storages;

import de.intektor.duckgames.data_storage.StorageBase;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author Intektor
 */
public class StorageString<K> extends StorageBase<K, String> {

    public StorageString(K key) {
        super(key);
        value = "";
    }

    @Override
    public void writeToStream(DataOutputStream out) throws IOException {
        out.writeUTF(value);
    }

    @Override
    public void readFromFile(DataInputStream in) throws IOException {
        value = in.readUTF();
    }
}
