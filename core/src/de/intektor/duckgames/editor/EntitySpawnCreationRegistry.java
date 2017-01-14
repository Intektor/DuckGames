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
            EntitySpawn entitySpawn = constructorRegistry.get(type).newInstance(x, y);
            entitySpawn.setX(entitySpawn.getX() - entitySpawn.getWidth() / 2);
            entitySpawn.setY(entitySpawn.getY() - entitySpawn.getHeight() / 2);
            return entitySpawn;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Class<? extends EntitySpawn> getClass(EntitySpawn.EntitySpawnType type) {
        return constructorRegistry.get(type).getDeclaringClass();
    }
}
