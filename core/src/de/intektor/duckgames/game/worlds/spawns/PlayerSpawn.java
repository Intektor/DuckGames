package de.intektor.duckgames.game.worlds.spawns;

import de.intektor.duckgames.collision.Collision2D;
import de.intektor.duckgames.client.editor.EntitySpawn;
import de.intektor.duckgames.tag.TagCompound;

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

    @Override
    public void writeToTag(TagCompound tag) {

    }

    @Override
    public void readFromTag(TagCompound tag) {

    }
}
