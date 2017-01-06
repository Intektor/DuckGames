package de.intektor.duckgames.editor.spawns;

import de.intektor.duckgames.collision.Collision2D;
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
        return 3f;
    }

    @Override
    public Collision2D getCollision() {
        return new Collision2D(x, y, getWidth(), getHeight());
    }
}
