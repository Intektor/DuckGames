package de.intektor.duckgames.client.rendering.block;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import de.intektor.duckgames.block.Block;
import de.intektor.duckgames.client.rendering.utils.TextureUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Intektor
 */
public class DefaultTextureBlockRenderer implements IBlockRenderer {

    private Map<Integer, Texture> glowingTexture = new HashMap<Integer, Texture>();
    private Texture fullTexture;
    private Texture scaledTexture;


    public DefaultTextureBlockRenderer(String fileName) {
        fullTexture = new Texture(fileName + ".png");
        fullTexture.getTextureData().prepare();
        Pixmap map = new Pixmap(fullTexture.getWidth() + 2, fullTexture.getHeight() + 2, Pixmap.Format.RGBA8888);
        map.setColor(new Color(0));
        map.fillRectangle(0, 0, map.getWidth(), map.getHeight());
        map.drawPixmap(fullTexture.getTextureData().consumePixmap(), 1, 1);
        scaledTexture = new Texture(map);
    }

    @Override
    public void renderBlockInWorld(Block block, ShapeRenderer sR, SpriteBatch sB, OrthographicCamera camera, float x, float y, float width, float height, float partialTicks) {
        TextureRegion region = new TextureRegion(fullTexture);
        sB.draw(region, x, y, width, height);
    }

    @Override
    public void renderBlockInEditor(Block block, ShapeRenderer sR, SpriteBatch sB, OrthographicCamera camera, float x, float y, float width, float height, float partialTicks) {
        TextureRegion region = new TextureRegion(fullTexture);
        sB.draw(region, x, y, width, height);
    }

    @Override
    public void renderBlockInScrollTool(Block block, ShapeRenderer sR, SpriteBatch sB, OrthographicCamera camera, float x, float y, float width, float height, float partialTicks, Color gColor) {
        if (gColor != null) {
            if (!glowingTexture.containsKey(gColor.toIntBits())) {
                glowingTexture.put(gColor.toIntBits(), TextureUtils.generateGlowingTexture(scaledTexture, gColor.toIntBits()));
            }
            sB.begin();
            sB.draw(glowingTexture.get(gColor.toIntBits()), x, y, width, height);
            sB.end();
        } else {
            sB.begin();
            sB.draw(scaledTexture, x, y, width, height);
            sB.end();
        }
    }
}
