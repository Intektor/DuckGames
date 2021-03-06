package de.intektor.duckgames.client.rendering.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import de.intektor.duckgames.DuckGamesClient;
import de.intektor.duckgames.client.gui.util.GuiUtils;
import de.intektor.duckgames.client.rendering.RenderUtils;
import de.intektor.duckgames.entity.entities.EntityRail;

import javax.vecmath.Point2f;

/**
 * @author Intektor
 */
public class EntityRailRenderer implements IEntityRenderer<EntityRail> {

    @Override
    public void renderEntityInWorld(EntityRail entity, OrthographicCamera camera, ShapeRenderer shapeRenderer, SpriteBatch spriteBatch, float partialTicks, float scaleX, float scaleY) {

    }

    @Override
    public void renderEntityOnScreen(EntityRail entity, OrthographicCamera screenCamera, OrthographicCamera worldCamera, ShapeRenderer shapeRenderer, SpriteBatch spriteBatch, float partialTicks, float scaleX, float scaleY) {
        Point2f pos = GuiUtils.projectWorldPosition(worldCamera, entity.posX, entity.posY);
        spriteBatch.begin();
        RenderUtils.drawString("RRRR", DuckGamesClient.getDuckGames().defaultFont12, pos.x, pos.y, spriteBatch, Color.WHITE);
        spriteBatch.end();
    }
}
