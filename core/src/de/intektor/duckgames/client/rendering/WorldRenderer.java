package de.intektor.duckgames.client.rendering;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import de.intektor.duckgames.DuckGamesClient;
import de.intektor.duckgames.block.Block;
import de.intektor.duckgames.client.rendering.block.BlockRendererRegistry;
import de.intektor.duckgames.client.rendering.entity.EntityRendererRegistry;
import de.intektor.duckgames.common.CommonCode;
import de.intektor.duckgames.common.GameRegistry;
import de.intektor.duckgames.entity.Entity;
import de.intektor.duckgames.entity.entities.EntityPlayer;
import de.intektor.duckgames.world.World;

import javax.vecmath.Point2f;

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
        Point2f drawPos = RenderUtils.getInterpolatedEntityPos(player, partialTicks);
        float drawX = drawPos.x;
        float drawY = drawPos.y;

        camera.position.set(drawX, drawY, 0);
        camera.zoom = 0.0225f;

        camera.update();

        shapeRenderer.setProjectionMatrix(camera.combined);
        spriteBatch.setProjectionMatrix(camera.combined);

        Vector3 v00 = camera.unproject(new Vector3(0, 0, 0));
        Vector3 v11 = camera.unproject(new Vector3(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), 0));

        spriteBatch.begin();
        for (int x = (int) Math.max(v00.x - 1, 0); x < Math.min(v11.x + 1, world.getWidth()); x++) {
            for (int y = (int) Math.max(v11.y - 1, 0); y < Math.min(v00 .y + 1, world.getHeight()); y++) {
                Block block = world.getBlock(x, y);
                blockRenderingRegistry.getRenderer(block).renderBlockInWorld(block, shapeRenderer, spriteBatch, camera, x, y, 1, 1, partialTicks);
            }
        }
        spriteBatch.end();

        for (Entity entity : world.getCombinedEntityList()) {
            entityRendererRegistry.getRenderer(entity.getClass()).renderEntityInWorld(entity, camera, shapeRenderer, spriteBatch, partialTicks, 1, 1);
        }

        for (Entity entity : world.getCombinedEntityList()) {
            entityRendererRegistry.getRenderer(entity.getClass()).renderEntityOnScreen(entity, dg.getDefaultCamera(), camera, dg.getDefaultShapeRenderer(), dg.getDefaultSpriteBatch(), partialTicks, 1, 1);
        }
    }
}
