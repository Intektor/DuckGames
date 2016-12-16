package de.intektor.duckgames.entity;

import de.intektor.duckgames.collision.CollisionRect;
import de.intektor.duckgames.common.GamePacketCombination;
import de.intektor.duckgames.common.net.NetworkHelper;
import de.intektor.duckgames.world.World;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * @author Intektor
 */
public abstract class Entity {

    public float posX, posY;
    public float motionX, motionY;

    public UUID uuid;

    public World worldObj;

    protected CollisionRect collision;

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
        collision = new CollisionRect(posX, posY, getWidth(), getHeight());
    }

    public final void update() {
        move();
        updateEntity();
    }

    protected void move() {

    }

    protected abstract void updateEntity();

    public abstract float getWidth();

    public abstract float getHeight();

    public final void writeEntityToStream(DataOutputStream out) throws IOException {
        out.writeInt(GamePacketCombination.entityRegistry.getIdentifier(getClass()));
        NetworkHelper.writeUUID(out, uuid);
        out.writeFloat(posX);
        out.writeFloat(posY);
        out.writeFloat(motionX);
        out.writeFloat(motionY);
    }

    public static Entity readEntityFromStream(DataInputStream in) throws IOException {
        Entity entity = GamePacketCombination.entityRegistry.createEntity(in.readInt(), NetworkHelper.readUUID(in));
        entity.posX = in.readFloat();
        entity.posY = in.readFloat();
        entity.motionX = in.readFloat();
        entity.motionY = in.readFloat();
        return entity;
    }

}
