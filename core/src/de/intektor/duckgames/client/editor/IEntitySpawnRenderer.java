package de.intektor.duckgames.client.editor;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * @author Intektor
 */
public interface IEntitySpawnRenderer<T extends EntitySpawn> {

    void renderInEditor(T spawn, float mouseX, float mouseY, float x, float y, float width, float height, boolean drawnOnMouse, ShapeRenderer shapeRenderer, SpriteBatch spriteBatch, EditableGameMap map, OrthographicCamera camera, float partialTicks);

    void renderInScrollTool(T spawn, float x, float y, float width, float height, ShapeRenderer shapeRenderer, SpriteBatch spriteBatch, OrthographicCamera camera, float partialTicks);
}
