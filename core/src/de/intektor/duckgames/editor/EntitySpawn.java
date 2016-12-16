package de.intektor.duckgames.editor;

import de.intektor.duckgames.collision.CollisionRect;

/**
 * @author Intektor
 */
public abstract class EntitySpawn {

    protected float x, y;

    public EntitySpawn(float x, float y) {
        this.x = x - getWidth() / 2;
        this.y = y - getHeight() / 2;
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

    public abstract float getWidth();

    public abstract float getHeight();

    public abstract CollisionRect getCollision();

    public enum EntitySpawnType {
        PLAYER_SPAWN
    }
}
