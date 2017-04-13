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
import de.intektor.duckgames.entity.EntityEquipmentSlot;
import de.intektor.duckgames.item.ItemStack;

import javax.vecmath.Point2f;

/**
 * @author Intektor
 */
public class EntityPlayerRenderer implements IEntityRenderer<EntityPlayerSP> {

    private Texture playerTexture;

    private DuckGamesClient dg = DuckGamesClient.getDuckGames();

    EntityPlayerRenderer() {
        playerTexture = new Texture("duck.png");
    }

    @Override
    public void renderEntityInWorld(EntityPlayerSP entity, OrthographicCamera camera, ShapeRenderer shapeRenderer, SpriteBatch spriteBatch, float partialTicks, float scaleX, float scaleY) {
        Point2f pos = RenderUtils.getInterpolatedEntityPos(entity, partialTicks);
        float drawX = pos.x;
        float drawY = pos.y;
        spriteBatch.begin();
        TextureRegion region = new TextureRegion(playerTexture);
        region.flip(entity.getDirection() == EntityDirection.LEFT, false);
        spriteBatch.draw(region, drawX * scaleX, drawY * scaleY, entity.getWidth() * scaleX, entity.getHeight() * scaleY);
        spriteBatch.end();
        ItemStack mainHand = entity.getEquipment(EntityEquipmentSlot.MAIN_HAND);
        if (mainHand != null) {
            dg.getItemRendererRegistry().getRenderer(mainHand.getItem()).renderItemOnPlayer(mainHand, mainHand.getItem(), entity, shapeRenderer, spriteBatch, camera, partialTicks);
        }
    }

    @Override
    public void renderEntityOnScreen(EntityPlayerSP entity, OrthographicCamera screenCamera, OrthographicCamera worldCamera, ShapeRenderer shapeRenderer, SpriteBatch spriteBatch, float partialTicks, float scaleX, float scaleY) {
        Point2f pos = RenderUtils.getInterpolatedEntityPos(entity, partialTicks);
        Point2f p = GuiUtils.projectWorldPosition(worldCamera, pos.x, pos.y);
        spriteBatch.begin();
        RenderUtils.drawString(entity.getHealth() + "", dg.defaultFont16, p.x, p.y, spriteBatch, Color.WHITE);
        spriteBatch.end();
    }
}
