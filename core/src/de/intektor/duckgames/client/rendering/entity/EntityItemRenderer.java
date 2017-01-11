package de.intektor.duckgames.client.rendering.entity;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import de.intektor.duckgames.DuckGamesClient;
import de.intektor.duckgames.client.rendering.item.ItemRendererRegistry;
import de.intektor.duckgames.entity.EntityItem;

/**
 * @author Intektor
 */
public class EntityItemRenderer implements IEntityRenderer<EntityItem> {

    private ItemRendererRegistry itemRendererRegistry = DuckGamesClient.getDuckGames().getItemRendererRegistry();

    private float offsetY;

    @Override
    public void renderEntityInWorld(EntityItem entity, OrthographicCamera camera, ShapeRenderer shapeRenderer, SpriteBatch spriteBatch, float partialTicks, float scaleX, float scaleY) {
        float drawX = entity.prevPosX + (entity.posX - entity.prevPosX) * partialTicks;
        float drawY = entity.prevPosY + (entity.posY - entity.prevPosY) * partialTicks;
        offsetY = (float) Math.sin((entity.ticksAlive + partialTicks) / 40f) * 0.2f + 0.2f;
        itemRendererRegistry.getRenderer(entity.getItemStack().getItem()).renderItemInWorld(entity.getItemStack(), entity.getItemStack().getItem(), shapeRenderer, spriteBatch, camera,
                drawX * scaleX, drawY * scaleY
                        + offsetY * scaleY
                , entity.getWidth() * scaleX, entity.getHeight() * scaleY, partialTicks);
    }

    @Override
    public void renderEntityOnScreen(EntityItem entity, OrthographicCamera screenCamera, OrthographicCamera worldCamera, ShapeRenderer shapeRenderer, SpriteBatch spriteBatch, float partialTicks, float scaleX, float scaleY) {

    }
}
