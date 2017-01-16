package de.intektor.duckgames.client.rendering;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import de.intektor.duckgames.DuckGamesClient;
import de.intektor.duckgames.block.Block;
import de.intektor.duckgames.client.rendering.block.BlockRendererRegistry;
import de.intektor.duckgames.client.rendering.entity.EntityRendererRegistry;
import de.intektor.duckgames.common.GameRegistry;
import de.intektor.duckgames.common.CommonCode;
import de.intektor.duckgames.entity.Entity;
import de.intektor.duckgames.entity.entities.EntityPlayer;
import de.intektor.duckgames.world.World;

/**
 * @author Intektor
 */
public class WorldRenderer {

    private DuckGamesClient dg;
    private GameRegistry gameRegistry;
    private BlockRendererRegistry blockRenderingRegistry;
    private EntityRendererRegistry entityRendererRegistry;

    {
        dg = DuckGamesClient.getDuckGames();
        blockRenderingRegistry = dg.getBlockRendererRegistry();
        entityRendererRegistry = dg.getEntityRendererRegistry();
        gameRegistry = CommonCode.gameRegistry;
    }

    public void renderWorld(World world, OrthographicCamera camera, ShapeRenderer shapeRenderer, SpriteBatch spriteBatch, EntityPlayer player, float partialTicks) {
//        boolean widthBigger = Math.max(world.getWidth(), world.getHeight()) == world.getWidth();
        float blockSize = 1;
        float drawX = player.prevPosX + (player.posX - player.prevPosX) * partialTicks;
        float drawY = player.prevPosY + (player.posY - player.prevPosY) * partialTicks;
        camera.position.set(drawX * blockSize, drawY * blockSize, 0);
//        camera.position.set(world.getWidth() / 2, world.getHeight() / 2, 0);
        camera.zoom = 0.0225f;
//        camera.zoom = 0.05f;
        camera.update();
        shapeRenderer.setProjectionMatrix(camera.combined);
        spriteBatch.setProjectionMatrix(camera.combined);
        for (int x = 0; x < world.getWidth(); x++) {
            for (int y = 0; y < world.getHeight(); y++) {
                Block block = world.getBlock(x, y);
                blockRenderingRegistry.getRenderer(block).renderBlockInWorld(block, shapeRenderer, spriteBatch, camera, x * blockSize, y * blockSize, blockSize, blockSize, partialTicks);
            }
        }

        for (Entity entity : world.getCombinedEntityList()) {
            entityRendererRegistry.getRenderer(entity.getClass()).renderEntityInWorld(entity, camera, shapeRenderer, spriteBatch, partialTicks, blockSize, blockSize);
        }

        for (Entity entity : world.getCombinedEntityList()) {
            entityRendererRegistry.getRenderer(entity.getClass()).renderEntityOnScreen(entity, dg.getDefaultCamera(), camera, dg.getDefaultShapeRenderer(), dg.getDefaultSpriteBatch(), partialTicks, blockSize, blockSize);
        }
    }
}
