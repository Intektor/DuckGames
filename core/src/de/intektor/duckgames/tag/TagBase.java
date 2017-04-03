package de.intektor.duckgames.tag;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author Intektor
 */
public abstract class TagBase<T> {

    public final String key;

    protected T value;

    protected TagBase(String key) {
        this.key = key;
    }

    public abstract void writeToFile(DataOutputStream out) throws IOException;

    public abstract void readFromFile(DataInputStream in) throws IOException;

    public void setValue(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }
}
