package de.intektor.duckgames.client.rendering.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import de.intektor.duckgames.DuckGamesClient;
import de.intektor.duckgames.client.gui.util.GuiUtils;
import de.intektor.duckgames.client.rendering.RenderUtils;
import de.intektor.duckgames.entity.entities.EntityBullet;

import javax.vecmath.Point2f;

/**
 * @author Intektor
 */
public class EntityBulletRenderer implements IEntityRenderer<EntityBullet> {

    @Override
    public void renderEntityInWorld(EntityBullet entity, OrthographicCamera camera, ShapeRenderer shapeRenderer, SpriteBatch spriteBatch, float partialTicks, float scaleX, float scaleY) {

    }

    @Override
    public void renderEntityOnScreen(EntityBullet entity, OrthographicCamera screenCamera, OrthographicCamera worldCamera, ShapeRenderer shapeRenderer, SpriteBatch spriteBatch, float partialTicks, float scaleX, float scaleY) {
        Point2f pos = GuiUtils.projectWorldPosition(worldCamera, entity.posX, entity.posY);
        spriteBatch.begin();
        RenderUtils.drawString("B", DuckGamesClient.getDuckGames().defaultFont12, pos.x, pos.y, spriteBatch, Color.WHITE);
        spriteBatch.end();
    }
}
