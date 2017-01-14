package de.intektor.duckgames.entity;

import de.intektor.duckgames.block.Block;
import de.intektor.duckgames.collision.Collision2D;
import de.intektor.duckgames.common.SharedGameRegistries;
import de.intektor.duckgames.common.net.NetworkUtils;
import de.intektor.duckgames.common.net.server_to_client.DamageEntityPacketToClient;
import de.intektor.duckgames.common.net.server_to_client.RemoveEntityPacketToClient;
import de.intektor.duckgames.game.damage.DamageSource;
import de.intektor.duckgames.util.EnumAxis;
import de.intektor.duckgames.world.World;
import de.intektor.duckgames.world.WorldServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author Intektor
 */
public abstract class Entity {

    public float posX, posY;
    public float prevPosX, prevPosY;
    public float motionX, motionY;

    public float motionMultiplier = 0.2f;

    public UUID uuid;

    public World worldObj;

    public float stepHeight = 0;
    protected boolean onGround;
    private float health;
    public long ticksAlive;
    public boolean isDead;

    protected Collision2D collision;

    protected EntityDirection direction = EntityDirection.RIGHT;

    public Entity(World world, float posX, float posY) {
        this.worldObj = world;
        this.posX = posX;
        this.posY = posY;
        this.uuid = UUID.randomUUID();
        initBasicEntity();
    }

    public Entity(UUID uuid) {
        this.uuid = uuid;
        initBasicEntity();
    }

    private void initBasicEntity() {
        health = getDefaultHealth();
        initEntity();
    }

    protected void initEntity() {
        collision = new Collision2D(posX, posY, getWidth(), getHeight());
    }

    public final void update() {
        motionY -= getGravitationalVelocity();
        move();
        updateEntity();
        ticksAlive++;
        if (posY < -64) kill();
        if (posX < -64 || posX > worldObj.getWidth() + 64) kill();
    }

    private void move() {
        prevPosX = posX;
        prevPosY = posY;

        Collision2D potentialCollisionX = collision.copy().move(motionX * motionMultiplier, 0);
        Collision2D potentialCollisionY = collision.copy().move(0, motionY * motionMultiplier);

        List<Collision2D> xCollided = new ArrayList<Collision2D>();
        List<Collision2D> yCollided = new ArrayList<Collision2D>();

        for (int x = 0; x < worldObj.getWidth(); x++) {
            for (int y = 0; y < worldObj.getHeight(); y++) {
                Block block = worldObj.getBlock(x, y);
                if (block.isSolidBlock()) {
                    Collision2D blockCollision = new Collision2D(x, y, 1, 1);
                    if (blockCollision.collidesWith(potentialCollisionX)) {
                        xCollided.add(blockCollision);
                    }
                    if (blockCollision.collidesWith(potentialCollisionY)) {
                        yCollided.add(blockCollision);
                    }
                }
            }
        }

        if (xCollided.isEmpty()) {
            posX += motionX * motionMultiplier;
        } else {
            label:
            for (Collision2D collision2D : xCollided) {
                for (int y = (int) collision2D.y; y < worldObj.getHeight(); y++) {
                    if (!worldObj.getBlock((int) collision2D.x, y).isSolidBlock()) {
                        if (y - posY <= stepHeight && canBeAtPosition(posX + motionX * motionMultiplier, y)) {
                            posX += motionX * motionMultiplier;
                            posY = y;
                            break label;
                        }
                    }
                }
            }
            collidedInAxis(EnumAxis.X, potentialCollisionX.x, potentialCollisionY.y, motionX * motionMultiplier, motionY * motionMultiplier);
        }

        if (yCollided.isEmpty()) {
            posY += motionY * motionMultiplier;
            onGround = false;
        } else {
            if (motionY < 0) onGround = true;
            collidedInAxis(EnumAxis.Y, potentialCollisionY.x, potentialCollisionY.y, motionX * motionMultiplier, motionY * motionMultiplier);
        }

        adjustCollision();
    }

    protected boolean canBeAtPosition(float posX, float posY) {
        Collision2D potColl = new Collision2D(posX, posY, getWidth(), getHeight());
        for (int x = 0; x < worldObj.getWidth(); x++) {
            for (int y = 0; y < worldObj.getHeight(); y++) {
                Block block = worldObj.getBlock(x, y);
                if (block.isSolidBlock()) {
                    Collision2D blockCollision = new Collision2D(x, y, 1, 1);
                    if (blockCollision.collidesWith(potColl)) return false;
                    if (blockCollision.collidesWith(potColl)) return false;
                }
            }
        }
        return true;
    }

    protected void collidedInAxis(EnumAxis axis, float collisionPointX, float collisionPointY, float motionX, float motionY) {
        switch (axis) {
            case X:
                this.motionX = 0;
                break;
            case Y:
                this.motionY = 0;
                break;
        }
    }

    protected abstract void updateEntity();

    public abstract float getWidth();

    public abstract float getHeight();

    public abstract float getDefaultHealth();

    public abstract float getEyeHeight();

    public float getGravitationalVelocity() {
        return 0.07f;
    }

    public void adjustCollision() {
        collision.set(posX, posY);
    }

    public void damageEntity(DamageSource source) {
        health -= source.getDamage();
        if (!worldObj.isRemote) {
            WorldServer worldServer = (WorldServer) worldObj;
            worldServer.getServer().messageEveryone(new DamageEntityPacketToClient(this, source));
        }
    }

    public float getHealth() {
        return health;
    }

    public void setHealth(float health) {
        this.health = health;
    }

    public final void writeEntityToStream(DataOutputStream out) throws IOException {
        out.writeInt(SharedGameRegistries.gameRegistry.getEntityID(getClass()));
        NetworkUtils.writeUUID(out, uuid);
        out.writeFloat(posX);
        out.writeFloat(posY);
        out.writeFloat(motionX);
        out.writeFloat(motionY);
        writeAdditionalSpawnData(out);
    }

    public static Entity readEntityFromStream(DataInputStream in) throws IOException {
        Entity entity = SharedGameRegistries.gameRegistry.createEntity(in.readInt(), NetworkUtils.readUUID(in));
        entity.posX = in.readFloat();
        entity.posY = in.readFloat();
        entity.motionX = in.readFloat();
        entity.motionY = in.readFloat();
        entity.readAdditionalSpawnData(in);
        return entity;
    }

    protected void writeAdditionalSpawnData(DataOutputStream out) throws IOException {

    }

    protected void readAdditionalSpawnData(DataInputStream in) throws IOException {

    }

    public EntityDirection getDirection() {
        return direction;
    }

    public void setDirection(EntityDirection direction) {
        this.direction = direction;
    }

    public float getDistanceSq(float x, float y) {
        float dX = posX - x;
        float dY = posY - y;
        return dX * dX + dY * dY;
    }

    public float getDistance(float x, float y) {
        return (float) Math.sqrt(getDistanceSq(x, y));
    }

    public float getDistanceToEntitySq(Entity entity) {
        return getDistanceSq(entity.posX, entity.posY);
    }

    public float getDistanceToEntity(Entity entity) {
        return (float) Math.sqrt(getDistanceToEntitySq(entity));
    }

    public Collision2D getCollision() {
        return collision;
    }

    public void kill() {
        isDead = true;
        if (!worldObj.isRemote) {
            WorldServer serverWorld = (WorldServer) worldObj;
            serverWorld.getServer().messageEveryone(new RemoveEntityPacketToClient(uuid));
            serverWorld.removeEntity(this);
        }
    }
}
