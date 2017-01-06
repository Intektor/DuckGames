package de.intektor.duckgames.client.rendering.entity;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import de.intektor.duckgames.DuckGamesClient;
import de.intektor.duckgames.client.rendering.RenderUtils;
import de.intektor.duckgames.entity.EntityPlayer;

/**
 * @author Intektor
 */
public class EntityPlayerRenderer implements IEntityRenderer<EntityPlayer> {

    private Texture playerTexture;

    private DuckGamesClient dg = DuckGamesClient.getDuckGames();

    EntityPlayerRenderer() {
        playerTexture = new Texture("assets/duck.png");
    }

    @Override
    public void renderEntity(EntityPlayer entity, OrthographicCamera camera, ShapeRenderer shapeRenderer, SpriteBatch spriteBatch, float partialTicks, float scaleX, float scaleY) {
        float drawX = entity.prevPosX + (entity.posX - entity.prevPosX) * partialTicks;
        float drawY = entity.prevPosY + (entity.posY - entity.prevPosY) * partialTicks;
        spriteBatch.begin();
        TextureRegion region = new TextureRegion(playerTexture);
        region.flip(entity.isLeft(), false);
        spriteBatch.draw(region, drawX * scaleX, drawY * scaleY, entity.getWidth() * scaleX, entity.getHeight() * scaleY);
        RenderUtils.drawString(entity.getHealth() + "h", dg.defaultFont12, drawX * scaleX, drawY * scaleY, spriteBatch, true);
        spriteBatch.end();

    }
}
