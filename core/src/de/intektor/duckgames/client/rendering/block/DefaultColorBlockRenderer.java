package de.intektor.duckgames.client.rendering.block;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import de.intektor.duckgames.block.Block;

/**
 * @author Intektor
 */
public class DefaultColorBlockRenderer implements IBlockRenderer {

    private Color color;

    public DefaultColorBlockRenderer(Color color) {
        this.color = color;
    }

    @Override
    public void renderBlockInWorld(Block block, ShapeRenderer sR, SpriteBatch sB, OrthographicCamera camera, float x, float y, float width, float height, float partialTicks) {
        sR.begin();
        sR.setColor(color);
        sR.set(ShapeRenderer.ShapeType.Filled);
        sR.rect(x, y, width, height);
        sR.end();
    }

    @Override
    public void renderBlockInEditor(Block block, ShapeRenderer sR, SpriteBatch sB, OrthographicCamera camera, float x, float y, float width, float height, float partialTicks) {
        sR.begin();
        sR.setColor(color);
        sR.set(ShapeRenderer.ShapeType.Filled);
        sR.rect(x, y, width, height);
        sR.end();
    }

    @Override
    public void renderBlockInScrollTool(Block block, ShapeRenderer sR, SpriteBatch sB, OrthographicCamera camera, float x, float y, float width, float height, float partialTicks) {
        renderBlockInEditor(block, sR, sB, camera, x, y, width, height, partialTicks);
    }
}
