package de.intektor.duckgames.data_storage.box;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import de.intektor.duckgames.data_storage.StorageBase;
import de.intektor.duckgames.data_storage.StorageHandler;
import de.intektor.duckgames.data_storage.tag.TagCompound;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

/**
 * A box used to store data and to save it in a data box.
 * The goal of this class is to write later usable data to streams.
 * The max size of data written is limited by a {@link Character#MAX_VALUE}, while it is not limited by bytes but by fields written to this box.
 * The implementation of this class is almost the same as it is in {@link TagCompound}, the difference is, that instead of saving data by {@link String} keys,
 * they are saved by a number, which saves a lot of resources!
 * This class supports a counter, you can still use the default {@link StorageHandler} methods for setting and getting data, but to make
 * the API of {@link DataInputStream} and {@link DataOutputStream} more similar, reading and writing methods are supported!
 *
 * @author Intektor
 */
public class DataBox extends StorageHandler<Character> {

    private static BiMap<Byte, Class<? extends StorageBase>> registry = HashBiMap.create();
    private static BiMap<Class<? extends StorageBase>, Class> objectMap = HashBiMap.create();
    private static Map<Class<? extends StorageBase>, Constructor<? extends StorageBase>> constructorMap = new HashMap<Class<? extends StorageBase>, Constructor<? extends StorageBase>>();

    private static void registerTagBase(Class<? extends StorageBase> tagClass, Class typeClass) {
        registry.put((byte) registry.size(), tagClass);
        objectMap.put(tagClass, typeClass);
        try {
            constructorMap.put(tagClass, tagClass.getConstructor(Character.class));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public static byte getIdentifier(Class<? extends StorageBase> clazz) {
        return registry.inverse().get(clazz);
    }

    public static Class<? extends StorageBase> getClass(byte identifier) {
        return registry.get(identifier);
    }

    static {
        registerTagBase(BoxBoolean.class, Boolean.class);
        registerTagBase(BoxByte.class, Byte.class);
        registerTagBase(BoxShort.class, Short.class);
        registerTagBase(BoxInteger.class, Integer.class);
        registerTagBase(BoxFloat.class, Float.class);
        registerTagBase(BoxLong.class, Long.class);
        registerTagBase(BoxDouble.class, Double.class);
        registerTagBase(BoxString.class, String.class);
        registerTagBase(BoxTagCompound.class, TagCompound.class);
    }

    private int counter;

    @Override
    protected void writeBaseToStream(StorageBase base, DataOutputStream out) throws IOException {
        out.writeByte(getIdentifier(base.getClass()));
        base.writeToStream(out);
    }

    @Override
    protected StorageBase readBaseFromStream(DataInputStream in) throws IOException {
        Class<? extends StorageBase> baseClass = getClass(in.readByte());
        try {
            Constructor<? extends StorageBase> constructor = constructorMap.get(baseClass);
            StorageBase base = constructor.newInstance((char) tags.size());
            base.readFromFile(in);
            return base;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected StorageBase createBase(Character key, Class type) {
        Class<? extends StorageBase> baseClass = objectMap.inverse().get(type);
        try {
            Constructor<? extends StorageBase> constructor = baseClass.getConstructor(Character.class);
            return constructor.newInstance(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void writeBoolean(boolean b) {
        setBoolean((char) counter++, b);
    }

    public boolean readBoolean() {
        return getBoolean((char) counter++);
    }

    public void writeByte(byte b) {
        setByte((char) counter++, b);
    }

    public byte readByte() {
        return getByte((char) counter++);
    }

    public void writeDouble(double d) {
        setDouble((char) counter++, d);
    }

    public double readDouble(double d) {
        return getDouble((char) counter++);
    }

    public void writeFloat(float f) {
        setFloat((char) counter++, f);
    }

    public float readFloat() {
        return getFloat((char) counter++);
    }

    public void writeInt(int i) {
        setInteger((char) counter++, i);
    }

    public int readInt() {
        return getInteger((char) counter++);
    }

    public void writeLong(long l) {
        setLong((char) counter++, l);
    }

    public long readLong() {
        return getLong((char) counter++);
    }

    public void writeShort(short s) {
        setShort((char) counter++, s);
    }

    public short readShort() {
        return getShort((char) counter++);
    }

    public void writeString(String s) {
        setString((char) counter++, s);
    }

    public String readString() {
        return getString((char) counter++);
    }
}