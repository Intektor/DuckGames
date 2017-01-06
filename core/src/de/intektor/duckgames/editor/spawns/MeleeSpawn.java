package de.intektor.duckgames.editor.spawns;

import de.intektor.duckgames.collision.Collision2D;
import de.intektor.duckgames.item.Items;

import java.util.Arrays;

/**
 * @author Intektor
 */
public class MeleeSpawn extends ItemSpawner {

    public MeleeSpawn(float x, float y) {
        super(x, y, Arrays.asList(new ItemSpawn(Items.SWORD)));
    }

    @Override
    public float getWidth() {
        return 0.95f;
    }

    @Override
    public float getHeight() {
        return 0.95f;
    }

    @Override
    public Collision2D getCollision() {
        return new Collision2D(x, y, getWidth(), getHeight());
    }
}
