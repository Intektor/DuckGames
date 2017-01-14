package de.intektor.duckgames.client.rendering.entity;

import de.intektor.duckgames.entity.Entity;
import de.intektor.duckgames.entity.entities.EntityBullet;
import de.intektor.duckgames.entity.entities.EntityItem;
import de.intektor.duckgames.entity.entities.EntityPlayer;
import de.intektor.duckgames.entity.entities.EntityRail;

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
        register(EntityItem.class, new EntityItemRenderer());
        register(EntityBullet.class, new EntityBulletRenderer());
        register(EntityRail.class, new EntityRailRenderer());
    }

}
