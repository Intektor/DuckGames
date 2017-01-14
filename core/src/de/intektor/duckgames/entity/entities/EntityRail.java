package de.intektor.duckgames.entity.entities;

import de.intektor.duckgames.entity.Entity;
import de.intektor.duckgames.world.World;

import java.util.UUID;

/**
 * @author Intektor
 */
public class EntityRail extends EntityBullet {

    public EntityRail(World world, float posX, float posY, EntityPlayer owner, float motionX, float motionY) {
        super(world, posX, posY, owner, motionX, motionY, Float.MAX_VALUE);
    }

    public EntityRail(UUID uuid) {
        super(uuid);
    }

    @Override
    public void onHitEntity(Entity entity) {
        super.onHitEntity(entity);
    }
}
