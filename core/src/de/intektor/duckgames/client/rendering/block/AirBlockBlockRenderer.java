package de.intektor.duckgames.client.rendering.block;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import de.intektor.duckgames.block.Block;

/**
 * @author Intektor
 */
public class AirBlockBlockRenderer implements IBlockRenderer {

    @Override
    public void renderBlockInWorld(Block block, ShapeRenderer sR, SpriteBatch sB, OrthographicCamera camera, float x, float y, float width, float height, float partialTicks) {

    }

    @Override
    public void renderBlockInEditor(Block block, ShapeRenderer sR, SpriteBatch sB, OrthographicCamera camera, float x, float y, float width, float height, float partialTicks) {

    }

    @Override
    public void renderBlockInScrollTool(Block block, ShapeRenderer sR, SpriteBatch sB, OrthographicCamera camera, float x, float y, float width, float height, float partialTicks, Color gColor) {
        sR.begin();
        sR.setColor(Color.WHITE);
        sR.line(x, y, x + width, y);
        sR.line(x + width, y, x + width, y + height);
        sR.line(x, y + height, x + width, y + height);
        sR.line(x, y, x, y + height);
        sR.end();
    }
}
