package de.intektor.duckgames.editor;

import de.intektor.duckgames.collision.Collision2D;
import de.intektor.duckgames.world.WorldServer;

/**
 * @author Intektor
 */
public abstract class EntitySpawn {

    protected float x, y;

    public EntitySpawn(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void place(float x, float y) {
        this.x = x - getWidth() / 2;
        this.y = y - getHeight() / 2;
    }

    public abstract float getWidth();

    public abstract float getHeight();

    public abstract Collision2D getCollision();

    public void spawnInWorld(WorldServer world) {

    }

    public enum EntitySpawnType {
        PLAYER_SPAWN,
        ITEM_SPAWN
    }
}
