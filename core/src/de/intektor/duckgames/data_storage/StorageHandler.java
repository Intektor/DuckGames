package de.intektor.duckgames.data_storage;

import de.intektor.duckgames.data_storage.tag.TagCompound;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Intektor
 */
public abstract class StorageHandler<T> {

    protected Map<T, StorageBase> tags = new HashMap<T, StorageBase>();

    public void writeToStream(DataOutputStream out) throws IOException {
        out.writeInt(tags.size());
        for (StorageBase base : tags.values()) {
            assert base.key != null;
            writeBaseToStream(base, out);
        }
    }

    public void readFromStream(DataInputStream in) throws IOException {
        int tagSize = in.readInt();
        for (int i = 0; i < tagSize; i++) {
            StorageBase base = readBaseFromStream(in);
            tags.put((T) base.key, base);
        }
    }

    protected abstract void writeBaseToStream(StorageBase base, DataOutputStream out) throws IOException;

    protected abstract StorageBase readBaseFromStream(DataInputStream in) throws IOException;

    protected abstract StorageBase createBase(T key, Class type);

    protected <K> void setValue(T key, K value) {
        if (!tags.containsKey(key)) {
            StorageBase tag = createBase(key, value.getClass());
            tag.setValue(value);
            tags.put((T) tag.key, tag);
        } else {
            tags.get(key).setValue(value);
        }
    }

    protected <K> K getValue(T key, Class<K> type) {
        if (!tags.containsKey(key)) {
            StorageBase tag = createBase(key, type);
            tags.put(key, tag);
            return (K) tag.getValue();
        } else {
            return (K) tags.get(key).getValue();
        }
    }

    public void setBoolean(T key, boolean value) {
        setValue(key, value);
    }

    public boolean getBoolean(T key) {
        return (Boolean) getValue(key, Boolean.class);
    }

    public void setByte(T key, byte value) {
        setValue(key, value);
    }

    public byte getByte(T key) {
        return (Byte) getValue(key, Byte.class);
    }

    public void setShort(T key, short value) {
        setValue(key, value);
    }

    public short getShort(T key) {
        return (Short) getValue(key, Short.class);
    }

    public void setInteger(T key, int value) {
        setValue(key, value);
    }

    public int getInteger(T key) {
        return (Integer) getValue(key, Integer.class);
    }

    public void setFloat(T key, float value) {
        setValue(key, value);
    }

    public float getFloat(T key) {
        return (Float) getValue(key, Float.class);
    }

    public void setLong(T key, long value) {
        setValue(key, value);
    }

    public long getLong(T key) {
        return (Long) getValue(key, Long.class);
    }

    public void setDouble(T key, double value) {
        setValue(key, value);
    }

    public double getDouble(T key) {
        return (Double) getValue(key, Double.class);
    }

    public void setString(T key, String value) {
        setValue(key, value);
    }

    public String getString(T key) {
        return (String) getValue(key, String.class);
    }

    public void setTag(T key, TagCompound value) {
        setValue(key, value);
    }

    public TagCompound getTag(T key) {
        return (TagCompound) getValue(key, TagCompound.class);
    }

    public StorageBase getBase(T key) {
        return tags.get(key);
    }

    public Map<T, StorageBase> getTags() {
        return tags;
    }
}
