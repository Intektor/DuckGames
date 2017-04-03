package de.intektor.duckgames.tag;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import de.intektor.duckgames.tag.tags.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Intektor
 */
public class TagCompound {

    private static BiMap<Integer, Class<? extends TagBase>> registry = HashBiMap.create();
    private static BiMap<Class<? extends TagBase>, Class> objectMap = HashBiMap.create();
    private static Map<Class<? extends TagBase>, Constructor<? extends TagBase>> constructorMap = new HashMap<Class<? extends TagBase>, Constructor<? extends TagBase>>();

    private static void registerTagBase(Class<? extends TagBase> tagClass, Class typeClass) {
        registry.put(registry.size(), tagClass);
        objectMap.put(tagClass, typeClass);
        try {
            constructorMap.put(tagClass, tagClass.getConstructor(String.class));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public static int getIdentifier(Class<? extends TagBase> clazz) {
        return registry.inverse().get(clazz);
    }

    public static Class<? extends TagBase> getClass(int identifier) {
        return registry.get(identifier);
    }

    static {
        registerTagBase(TagBoolean.class, Boolean.class);
        registerTagBase(TagByte.class, Byte.class);
        registerTagBase(TagShort.class, Short.class);
        registerTagBase(TagInteger.class, Integer.class);
        registerTagBase(TagFloat.class, Float.class);
        registerTagBase(TagLong.class, Long.class);
        registerTagBase(TagDouble.class, Double.class);
        registerTagBase(TagString.class, String.class);
        registerTagBase(TagTagCompound.class, TagCompound.class);
    }

    protected Map<String, TagBase> tags = new HashMap<String, TagBase>();

    public void writeToStream(DataOutputStream out) throws IOException {
        out.writeInt(tags.size());
        for (TagBase base : tags.values()) {
            assert base.key != null;
            out.writeInt(getIdentifier(base.getClass()));
            out.writeUTF(base.key);
            base.writeToFile(out);
        }
    }

    public void readFromStream(DataInputStream in) throws IOException {
        int tagSize = in.readInt();
        for (int i = 0; i < tagSize; i++) {
            Class<? extends TagBase> baseClass = getClass(in.readInt());
            try {
                Constructor<? extends TagBase> constructor = constructorMap.get(baseClass);
                TagBase base = constructor.newInstance(in.readUTF());
                base.readFromFile(in);
                tags.put(base.key, base);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected TagBase createTag(String key, Class type) {
        Class<? extends TagBase> baseClass = objectMap.inverse().get(type);
        try {
            Constructor<? extends TagBase> constructor = baseClass.getConstructor(String.class);
            return constructor.newInstance(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    protected <T> void setValue(String key, T value) {
        if (!tags.containsKey(key)) {
            TagBase tag = createTag(key, value.getClass());
            tag.setValue(value);
            tags.put(tag.key, tag);
        } else {
            tags.get(key).setValue(value);
        }
    }

    protected Object getValue(String key, Class type) {
        if (!tags.containsKey(key)) {
            TagBase tag = createTag(key, type);
            tags.put(key, tag);
            return tag.getValue();
        } else {
            return tags.get(key).getValue();
        }
    }

    public void setBoolean(String key, boolean value) {
        setValue(key, value);
    }

    public boolean getBoolean(String key) {
        return (Boolean) getValue(key, Boolean.class);
    }

    public void setByte(String key, byte value) {
        setValue(key, value);
    }

    public byte getByte(String key) {
        return (Byte) getValue(key, Byte.class);
    }

    public void setShort(String key, short value) {
        setValue(key, value);
    }

    public short getShort(String key) {
        return (Short) getValue(key, Short.class);
    }

    public void setInteger(String key, int value) {
        setValue(key, value);
    }

    public int getInteger(String key) {
        return (Integer) getValue(key, Integer.class);
    }

    public void setFloat(String key, float value) {
        setValue(key, value);
    }

    public float getFloat(String key) {
        return (Float) getValue(key, Float.class);
    }

    public void setLong(String key, long value) {
        setValue(key, value);
    }

    public long getLong(String key) {
        return (Long) getValue(key, Long.class);
    }

    public void setDouble(String key, double value) {
        setValue(key, value);
    }

    public double getDouble(String key) {
        return (Double) getValue(key, Double.class);
    }

    public void setString(String key, String value) {
        setValue(key, value);
    }

    public String getString(String key) {
        return (String) getValue(key, String.class);
    }

    public void setTag(String key, TagCompound value) {
        setValue(key, value);
    }

    public TagCompound getTag(String key) {
        return (TagCompound) getValue(key, TagCompound.class);
    }

    public TagBase getBase(String key) {
        return tags.get(key);
    }

    public Map<String, TagBase> getTags() {
        return tags;
    }
}
