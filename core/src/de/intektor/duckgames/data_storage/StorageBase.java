package de.intektor.duckgames.data_storage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author Intektor
 */
public abstract class StorageBase<K, T> {

    public final K key;

    protected T value;

    protected StorageBase(K key) {
        this.key = key;
    }

    public abstract void writeToStream(DataOutputStream out) throws IOException;

    public abstract void readFromFile(DataInputStream in) throws IOException;

    public void setValue(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }
}
