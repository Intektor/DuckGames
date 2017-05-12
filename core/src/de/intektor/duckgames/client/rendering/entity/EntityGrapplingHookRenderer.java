package de.intektor.duckgames.client.rendering.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import de.intektor.duckgames.client.rendering.RenderUtils;
import de.intektor.duckgames.entity.entities.EntityGrapplingHook;

import javax.vecmath.Point2f;

/**
 * @author Intektor
 */
public class EntityGrapplingHookRenderer implements IEntityRenderer<EntityGrapplingHook> {

    @Override
    public void renderEntityInWorld(EntityGrapplingHook entity, OrthographicCamera camera, ShapeRenderer shapeRenderer, SpriteBatch spriteBatch, float partialTicks, float scaleX, float scaleY) {
        shapeRenderer.begin();
        Point2f playerPos = RenderUtils.getInterpolatedEntityPos(entity.getShooter(), partialTicks);
        Point2f hookPos = RenderUtils.getInterpolatedEntityPos(entity, partialTicks);
        float x = playerPos.x + entity.getShooter().getWidth() / 2;
        float y = playerPos.y + entity.getShooter().getEyeHeight();
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.set(ShapeRenderer.ShapeType.Line);
        shapeRenderer.line(x, y, hookPos.x + entity.getWidth() / 2, hookPos.y + entity.getHeight() / 2);
        shapeRenderer.end();
    }

    @Override
    public void renderEntityOnScreen(EntityGrapplingHook entity, OrthographicCamera screenCamera, OrthographicCamera worldCamera, ShapeRenderer shapeRenderer, SpriteBatch spriteBatch, float partialTicks, float scaleX, float scaleY) {

    }
}
