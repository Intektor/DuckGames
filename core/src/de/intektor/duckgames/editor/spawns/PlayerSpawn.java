package de.intektor.duckgames.editor.spawns;

import de.intektor.duckgames.collision.CollisionRect;
import de.intektor.duckgames.editor.EntitySpawn;

/**
 * @author Intektor
 */
public class PlayerSpawn extends EntitySpawn {

    public PlayerSpawn(float x, float y) {
        super(x, y);
    }

    @Override
    public float getWidth() {
        return 2f;
    }

    @Override
    public float getHeight() {
        return 3.8f;
    }

    @Override
    public CollisionRect getCollision() {
        return new CollisionRect(x, y, getWidth(), getHeight());
    }
}
