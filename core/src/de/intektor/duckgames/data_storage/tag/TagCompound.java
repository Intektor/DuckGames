package de.intektor.duckgames.data_storage.tag;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import de.intektor.duckgames.data_storage.StorageBase;
import de.intektor.duckgames.data_storage.StorageHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Intektor
 */
public class TagCompound extends StorageHandler<String> {

    private static BiMap<Integer, Class<? extends StorageBase>> registry = HashBiMap.create();
    private static BiMap<Class<? extends StorageBase>, Class> objectMap = HashBiMap.create();
    private static Map<Class<? extends StorageBase>, Constructor<? extends StorageBase>> constructorMap = new HashMap<Class<? extends StorageBase>, Constructor<? extends StorageBase>>();

    private static void registerTagBase(Class<? extends StorageBase> tagClass, Class typeClass) {
        registry.put(registry.size(), tagClass);
        objectMap.put(tagClass, typeClass);
        try {
            constructorMap.put(tagClass, tagClass.getConstructor(String.class));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public static int getIdentifier(Class<? extends StorageBase> clazz) {
        return registry.inverse().get(clazz);
    }

    public static Class<? extends StorageBase> getClass(int identifier) {
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

    @Override
    protected void writeBaseToStream(StorageBase base, DataOutputStream out) throws IOException {
        out.writeInt(getIdentifier(base.getClass()));
        out.writeUTF((String) base.key);
        base.writeToStream(out);
    }

    @Override
    protected StorageBase readBaseFromStream(DataInputStream in) throws IOException {
        Class<? extends StorageBase> baseClass = getClass(in.readInt());
        try {
            Constructor<? extends StorageBase> constructor = constructorMap.get(baseClass);
            StorageBase base = constructor.newInstance(in.readUTF());
            base.readFromFile(in);
            return base;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected StorageBase createBase(String key, Class type) {
        Class<? extends StorageBase> baseClass = objectMap.inverse().get(type);
        try {
            Constructor<? extends StorageBase> constructor = baseClass.getConstructor(String.class);
            return constructor.newInstance(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
