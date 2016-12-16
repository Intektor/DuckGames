package de.intektor.duckgames.editor;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * @author Intektor
 */
public interface EntitySpawnRenderer<T extends EntitySpawn> {

    void render(T spawn, float mouseX, float mouseY, float x, float y, float width, float height, boolean drawnOnMouse, ShapeRenderer shapeRenderer, SpriteBatch spriteBatch);

}
