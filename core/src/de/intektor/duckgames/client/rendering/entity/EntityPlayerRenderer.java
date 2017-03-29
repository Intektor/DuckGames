package de.intektor.duckgames.client.rendering.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import de.intektor.duckgames.DuckGamesClient;
import de.intektor.duckgames.client.entity.EntityPlayerSP;
import de.intektor.duckgames.client.gui.util.GuiUtils;
import de.intektor.duckgames.client.rendering.RenderUtils;
import de.intektor.duckgames.entity.EntityDirection;

import javax.vecmath.Point2f;

/**
 * @author Intektor
 */
public class EntityPlayerRenderer implements IEntityRenderer<EntityPlayerSP> {

    private Texture playerTexture;

    private DuckGamesClient dg = DuckGamesClient.getDuckGames();

    EntityPlayerRenderer() {
        playerTexture = new Texture("assets/duck.png");
    }

    @Override
    public void renderEntityInWorld(EntityPlayerSP entity, OrthographicCamera camera, ShapeRenderer shapeRenderer, SpriteBatch spriteBatch, float partialTicks, float scaleX, float scaleY) {
        Point2f pos = RenderUtils.getInterpolatedEntityPos(entity);
        float drawX = pos.x;
        float drawY = pos.y;
        spriteBatch.begin();
        TextureRegion region = new TextureRegion(playerTexture);
        region.flip(entity.getDirection() == EntityDirection.LEFT, false);
        spriteBatch.draw(region, drawX * scaleX, drawY * scaleY, entity.getWidth() * scaleX, entity.getHeight() * scaleY);
        spriteBatch.end();

    }

    @Override
    public void renderEntityOnScreen(EntityPlayerSP entity, OrthographicCamera screenCamera, OrthographicCamera worldCamera, ShapeRenderer shapeRenderer, SpriteBatch spriteBatch, float partialTicks, float scaleX, float scaleY) {
        Point2f pos = RenderUtils.getInterpolatedEntityPos(entity);
        Point2f p = GuiUtils.projectWorldPosition(worldCamera, pos.x, pos.y);
        spriteBatch.begin();
        RenderUtils.drawString(entity.getHealth() + "", dg.defaultFont16, p.x, p.y, spriteBatch, Color.WHITE);
        spriteBatch.end();
    }
}
