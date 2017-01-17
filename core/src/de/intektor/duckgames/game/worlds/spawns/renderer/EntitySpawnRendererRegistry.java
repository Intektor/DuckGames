package de.intektor.duckgames.game.worlds.spawns.renderer;

import de.intektor.duckgames.client.editor.EntitySpawn;
import de.intektor.duckgames.client.editor.IEntitySpawnRenderer;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Intektor
 */
public class EntitySpawnRendererRegistry {

    private Map<Class<? extends EntitySpawn>, IEntitySpawnRenderer> registry = new HashMap<Class<? extends EntitySpawn>, IEntitySpawnRenderer>();

    public void register(Class<? extends EntitySpawn> clazz, IEntitySpawnRenderer renderer) {
        registry.put(clazz, renderer);
    }

    public IEntitySpawnRenderer getRenderer(Class<? extends EntitySpawn> clazz) {
        return registry.get(clazz);
    }

}
