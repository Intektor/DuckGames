package de.intektor.duckgames.client.renderer.entity;

import de.intektor.duckgames.entity.Entity;
import de.intektor.duckgames.entity.EntityPlayer;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Intektor
 */
public class EntityRendererRegistry {

    private Map<Class<? extends Entity>, IEntityRenderer> registry = new HashMap<Class<? extends Entity>, IEntityRenderer>();

    public <ENTITY extends Entity> void register(Class<ENTITY> clazz, IEntityRenderer<ENTITY> renderer) {
        registry.put(clazz, renderer);
    }

    public IEntityRenderer getRenderer(Class<? extends Entity> clazz) {
        return registry.get(clazz);
    }

    public void initDefaultEntities() {
        register(EntityPlayer.class, new EntityPlayerRenderer());
    }

}
