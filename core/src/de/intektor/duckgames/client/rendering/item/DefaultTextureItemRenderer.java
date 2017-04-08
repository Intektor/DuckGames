package de.intektor.duckgames.client.rendering.item;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import de.intektor.duckgames.client.rendering.utils.TextureUtils;
import de.intektor.duckgames.entity.entities.EntityPlayer;
import de.intektor.duckgames.item.Item;
import de.intektor.duckgames.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Intektor
 */
public class DefaultTextureItemRenderer implements IItemRenderer {

    private Texture fullTexture;
    private Texture scaledTexture;

    private Map<Integer, Texture> glowingTextures = new HashMap<Integer, Texture>();

    public DefaultTextureItemRenderer(String fileName) {
        fullTexture = new Texture(fileName + ".png");
        fullTexture.getTextureData().prepare();
        Pixmap map = new Pixmap(fullTexture.getWidth() + 2, fullTexture.getHeight() + 2, Pixmap.Format.RGBA8888);
        map.setColor(new Color(0x000000));
        map.fillRectangle(0, 0, map.getWidth(), map.getHeight());
        map.drawPixmap(fullTexture.getTextureData().consumePixmap(), 1, 1);
        this.scaledTexture = new Texture(map);
    }

    @Override
    public void renderItemInWorld(ItemStack stack, Item item, ShapeRenderer sR, SpriteBatch sB, OrthographicCamera camera, float x, float y, float width, float height, float partialTicks) {
        sB.begin();
        sB.draw(scaledTexture, x, y, width, height);
        sB.end();
    }

    @Override
    public void renderItemOnPlayer(ItemStack stack, Item item, EntityPlayer player, ShapeRenderer sR, SpriteBatch sB, OrthographicCamera camera, float x, float y, float width, float height, float partialTicks) {
        sB.begin();
        sB.draw(scaledTexture, x, y, width, height);
        sB.end();
    }

    @Override
    public void renderItemInEditor(ItemStack stack, Item item, ShapeRenderer sR, SpriteBatch sB, OrthographicCamera camera, float x, float y, float width, float height, float partialTicks, Color gColor) {
        sB.begin();
        sB.draw(gColor == null ? scaledTexture : getOrGenGTexture(scaledTexture, gColor), x, y, width, height);
        sB.end();
    }

    @Override
    public void renderItemInScrollTool(ItemStack stack, Item item, ShapeRenderer sR, SpriteBatch sB, OrthographicCamera camera, float x, float y, float width, float height, float rotation, float partialTicks, Color gColor) {
        sB.begin();
        sB.draw(new TextureRegion(gColor == null ? scaledTexture : getOrGenGTexture(scaledTexture, gColor)), x, y, 0, 0, width, height, 1, 1, rotation);
        sB.end();
    }

    @Override
    public Texture getItemTexture() {
        return fullTexture;
    }


    protected Texture getOrGenGTexture(Texture texture, Color color) {
        if (!glowingTextures.containsKey(color.toIntBits())) {
            glowingTextures.put(color.toIntBits(), TextureUtils.generateGlowingTexture(texture, color.toIntBits()));
        }
        return glowingTextures.get(color.toIntBits());
    }
}
