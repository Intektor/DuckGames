package de.intektor.duckgames.client.rendering.block;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import de.intektor.duckgames.block.Block;

/**
 * @author Intektor
 */
public class DefaultTextureBlockRenderer implements IBlockRenderer {

    private Texture scaledTexture;
    private Texture fullTexture;

    public DefaultTextureBlockRenderer(String fileName) {
        fullTexture = new Texture("assets/" + fileName + ".png");
        fullTexture.getTextureData().prepare();
        Pixmap map = new Pixmap(fullTexture.getWidth() + 2, fullTexture.getHeight() + 2, Pixmap.Format.RGBA8888);
        map.setColor(new Color(0x000000));
        map.fillRectangle(0, 0, map.getWidth(), map.getHeight());
        map.drawPixmap(fullTexture.getTextureData().consumePixmap(), 1, 1);
        this.scaledTexture = new Texture(map);
        map.dispose();
    }

    @Override
    public void renderBlockInWorld(Block block, ShapeRenderer sR, SpriteBatch sB, OrthographicCamera camera, float x, float y, float width, float height, float partialTicks) {
        TextureRegion region = new TextureRegion(fullTexture);
        sB.draw(region, x, y, width, height);
    }

    @Override
    public void renderBlockInEditor(Block block, ShapeRenderer sR, SpriteBatch sB, OrthographicCamera camera, float x, float y, float width, float height, float partialTicks) {
        sB.begin();
        TextureRegion region = new TextureRegion(fullTexture);
        sB.draw(region, x, y, width, height);
        sB.end();
    }

    @Override
    public void renderBlockInScrollTool(Block block, ShapeRenderer sR, SpriteBatch sB, OrthographicCamera camera, float x, float y, float width, float height, float partialTicks) {
        sB.begin();
        sB.draw(scaledTexture, x, y, width, height);
        sB.end();
    }
}
