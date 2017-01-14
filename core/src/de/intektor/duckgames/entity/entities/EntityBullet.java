package de.intektor.duckgames.entity.entities;

import de.intektor.duckgames.common.net.NetworkUtils;
import de.intektor.duckgames.entity.Entity;
import de.intektor.duckgames.game.damage.DamageSource;
import de.intektor.duckgames.util.EnumAxis;
import de.intektor.duckgames.world.World;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author Intektor
 */
public class EntityBullet extends Entity {

    private UUID ownerUUID;

    protected Map<Entity, HitRegister> registeredHits = new HashMap<Entity, HitRegister>();

    private float damage;

    public EntityBullet(World world, float posX, float posY, EntityPlayer owner, float motionX, float motionY, float damage) {
        super(world, posX, posY);
        this.motionX = motionX;
        this.motionY = motionY;
        this.ownerUUID = owner.uuid;
        this.damage = damage;
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
                        onHitEntity(entity);
                    }
                }
            }
        }
    }

    public void onHitEntity(Entity entity) {
        entity.damageEntity(new DamageSource(this, 1));
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
        out.writeFloat(damage);
    }

    @Override
    protected void readAdditionalSpawnData(DataInputStream in) throws IOException {
        super.readAdditionalSpawnData(in);
        ownerUUID = NetworkUtils.readUUID(in);
        damage = in.readFloat();
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
