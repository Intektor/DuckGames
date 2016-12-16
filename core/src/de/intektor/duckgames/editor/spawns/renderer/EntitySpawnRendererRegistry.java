package de.intektor.duckgames.editor.spawns.renderer;

import de.intektor.duckgames.editor.EntitySpawn;
import de.intektor.duckgames.editor.EntitySpawnRenderer;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Intektor
 */
public class EntitySpawnRendererRegistry {

    private Map<Class<? extends EntitySpawn>, EntitySpawnRenderer> registry = new HashMap<Class<? extends EntitySpawn>, EntitySpawnRenderer>();

    public void register(Class<? extends EntitySpawn> clazz, EntitySpawnRenderer renderer) {
        registry.put(clazz, renderer);
    }

    public EntitySpawnRenderer getRenderer(Class<? extends EntitySpawn> clazz) {
        return registry.get(clazz);
    }

}
