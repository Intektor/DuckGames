package de.intektor.duckgames.editor;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Intektor
 */
public class EntitySpawnCreationRegistry {

    private Map<EntitySpawn.EntitySpawnType, Constructor<? extends EntitySpawn>> constructorRegistry = new HashMap<EntitySpawn.EntitySpawnType, Constructor<? extends EntitySpawn>>();

    public void register(EntitySpawn.EntitySpawnType type, Class<? extends EntitySpawn> clazz) {
        try {
            constructorRegistry.put(type, clazz.getConstructor(float.class, float.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public EntitySpawn createSpawn(EntitySpawn.EntitySpawnType type, float x, float y) {
        try {
            return constructorRegistry.get(type).newInstance(x, y);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Class<? extends EntitySpawn> getClass(EntitySpawn.EntitySpawnType type) {
        return constructorRegistry.get(type).getDeclaringClass();
    }
}
