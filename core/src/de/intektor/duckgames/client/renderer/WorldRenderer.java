package de.intektor.duckgames.client.renderer;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import de.intektor.duckgames.DuckGamesClient;
import de.intektor.duckgames.block.Block;
import de.intektor.duckgames.block.BlockRegistry;
import de.intektor.duckgames.client.renderer.block.BlockRendererRegistry;
import de.intektor.duckgames.entity.EntityPlayer;
import de.intektor.duckgames.world.World;

/**
 * @author Intektor
 */
public class WorldRenderer {

    private DuckGamesClient dg;
    private BlockRegistry blockRegistry;
    private BlockRendererRegistry blockRenderingRegistry;

    {
        dg = DuckGamesClient.getDuckGames();
        blockRegistry = dg.getBlockRegistry();
        blockRenderingRegistry = dg.getBlockRendererRegistry();
    }

    public void renderWorld(World world, OrthographicCamera camera, ShapeRenderer shapeRenderer, SpriteBatch spriteBatch, EntityPlayer player) {
        for (int x = 0; x < world.getWidth(); x++) {
            for (int y = 0; y < world.getHeight(); y++) {
                Block block = world.getBlock(x, y);
                blockRenderingRegistry.getRenderer(block).renderBlockInWorld(block, shapeRenderer, spriteBatch, camera, x, y);
            }
        }
    }

}
