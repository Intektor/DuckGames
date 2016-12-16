package de.intektor.duckgames.client.renderer.block;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import de.intektor.duckgames.block.Block;

/**
 * @author Intektor
 */
public class DefaultTextureBlockRenderer implements IBlockRenderer {

    private Texture texture;

    public DefaultTextureBlockRenderer(String fileName) {
        this.texture = new Texture("assets/" + fileName + ".png");
    }

    @Override
    public void renderBlockInWorld(Block block, ShapeRenderer sR, SpriteBatch sB, OrthographicCamera camera, int x, int y) {
        sB.begin();
        sB.draw(texture, x, y, 1, 1);
        sB.end();
    }

    @Override
    public void renderBlockInEditor(Block block, ShapeRenderer sR, SpriteBatch sB, OrthographicCamera camera, float x, float y, float width, float height) {
        sB.begin();
        sB.draw(texture, x, y, width, height);
        sB.end();
    }

    @Override
    public void renderBlockInScrollTool(Block block, ShapeRenderer sR, SpriteBatch sB, OrthographicCamera camera, float x, float y, float width, float height) {
        renderBlockInEditor(block, sR, sB, camera, x, y, width, height);
    }

    public Texture getTexture() {
        return texture;
    }
}
