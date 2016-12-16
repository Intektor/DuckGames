package de.intektor.duckgames.client.renderer.entity;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import de.intektor.duckgames.entity.Entity;

/**
 * @author Intektor
 */
public interface IEntityRenderer<T extends Entity> {

    void renderEntity(T entity, OrthographicCamera camera, ShapeRenderer shapeRenderer, SpriteBatch spriteBatch, float partialTicks);
}
