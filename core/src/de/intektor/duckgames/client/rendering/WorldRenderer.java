package de.intektor.duckgames.client.rendering;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import de.intektor.duckgames.DuckGamesClient;
import de.intektor.duckgames.block.Block;
import de.intektor.duckgames.client.rendering.block.BlockRendererRegistry;
import de.intektor.duckgames.client.rendering.entity.EntityRendererRegistry;
import de.intektor.duckgames.common.SharedGameRegistries;
import de.intektor.duckgames.common.GameRegistry;
import de.intektor.duckgames.entity.Entity;
import de.intektor.duckgames.entity.EntityPlayer;
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
        gameRegistry = SharedGameRegistries.gameRegistry;
    }

    public void renderWorld(World world, OrthographicCamera camera, ShapeRenderer shapeRenderer, SpriteBatch spriteBatch, EntityPlayer player, float partialTicks) {
//        boolean widthBigger = Math.max(world.getWidth(), world.getHeight()) == world.getWidth();
        float blockSize = camera.viewportWidth / world.getWidth();
        float drawX = player.prevPosX + (player.posX - player.prevPosX) * partialTicks;
        float drawY = player.prevPosY + (player.posY - player.prevPosY) * partialTicks;
        camera.position.set(drawX * blockSize, drawY * blockSize, 0);
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
            entityRendererRegistry.getRenderer(entity.getClass()).renderEntity(entity, camera, shapeRenderer, spriteBatch, partialTicks, blockSize, blockSize);
        }
    }

}