package de.intektor.duckgames.entity;

import de.intektor.duckgames.common.net.NetworkUtils;
import de.intektor.duckgames.game.damage.DamageSource;
import de.intektor.duckgames.util.EnumAxis;
import de.intektor.duckgames.world.World;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * @author Intektor
 */
public class EntityBullet extends Entity {

    private UUID ownerUUID;

    protected Map<Entity, HitRegister> registeredHits = new HashMap<Entity, HitRegister>();

    public EntityBullet(World world, float posX, float posY, EntityPlayer owner, float motionX, float motionY) {
        super(world, posX, posY);
        this.motionX = motionX;
        this.motionY = motionY;
        this.ownerUUID = owner.uuid;
    }

    public EntityBullet(UUID uuid) {
        super(uuid);
    }

    @Override
    protected void updateEntity() {
        if (!worldObj.isRemote) {
            List<Entity> entitiesInRegion = worldObj.getEntitiesInRegion(Entity.class, getCollision());
            for (Entity entity : entitiesInRegion) {
                if (!entity.uuid.equals(ownerUUID)) {
                    HitRegister hitRegister = registeredHits.get(entity);
                    if (hitRegister == null || worldObj.getWorldTime() - hitRegister.getWorldTime() > 5) {
                        registeredHits.put(entity, new HitRegister(entity, worldObj.getWorldTime()));
                        entity.damageEntity(new DamageSource(this, 1));
                    }
                }
            }
        }
    }

    @Override
    protected void collidedInAxis(EnumAxis axis, float collisionPointX, float collisionPointY, float motionX, float motionY) {
        super.collidedInAxis(axis, collisionPointX, collisionPointY, motionX, motionY);
        this.kill();
    }

    @Override
    public float getWidth() {
        return 0.1f;
    }

    @Override
    public float getHeight() {
        return 0.1f;
    }

    @Override
    public float getDefaultHealth() {
        return 1;
    }

    @Override
    public float getEyeHeight() {
        return getHeight() * 0.88f;
    }

    @Override
    protected void writeAdditionalSpawnData(DataOutputStream out) throws IOException {
        super.writeAdditionalSpawnData(out);
        NetworkUtils.writeUUID(out, ownerUUID);
    }

    @Override
    protected void readAdditionalSpawnData(DataInputStream in) throws IOException {
        super.readAdditionalSpawnData(in);
        ownerUUID = NetworkUtils.readUUID(in);
    }

    @Override
    public float getGravitationalVelocity() {
        return 0;
    }

    protected static class HitRegister {
        private Entity hit;
        private long worldTime;

        public HitRegister(Entity hit, long worldTime) {
            this.hit = hit;
            this.worldTime = worldTime;
        }

        public Entity getHit() {
            return hit;
        }

        public long getWorldTime() {
            return worldTime;
        }
    }
}
