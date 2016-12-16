package de.intektor.duckgames.entity;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Intektor
 */
public class EntityRegistry {

    private BiMap<Class<? extends Entity>, Integer> classRegistry = HashBiMap.create();
    private Map<Integer, Constructor<? extends Entity>> constructorRegistry = new HashMap<Integer, Constructor<? extends Entity>>();

    public void registerEntity(Class<? extends Entity> clazz, int identifier) {
        try {
            classRegistry.put(clazz, identifier);
            constructorRegistry.put(identifier, clazz.getConstructor(UUID.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Class<? extends Entity> getClass(int identifier) {
        return classRegistry.inverse().get(identifier);
    }

    public int getIdentifier(Class<? extends Entity> clazz) {
        return classRegistry.get(clazz);
    }

    public Entity createEntity(int identifier, UUID uuid) {
        try {
            return constructorRegistry.get(identifier).newInstance(uuid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
