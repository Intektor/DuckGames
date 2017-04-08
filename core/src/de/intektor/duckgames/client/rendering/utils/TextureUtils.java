package de.intektor.duckgames.client.rendering.utils;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;

/**
 * @author Intektor
 */
public class TextureUtils {

    public static Texture generateGlowingTexture(Texture texture, int color) {
        if (!texture.getTextureData().isPrepared()) texture.getTextureData().prepare();
        return generateGlowingTexture(texture.getTextureData().consumePixmap(), color);
    }

    public static Texture generateGlowingTexture(Pixmap map, int color) {
        map.setBlending(Pixmap.Blending.None);
        int transCode = 0;
        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                if (map.getPixel(x, y) == transCode) {
                    int pixelUp = map.getPixel(x, y + 1);
                    boolean up = pixelUp != transCode && pixelUp != color;
                    int pixelRight = map.getPixel(x + 1, y);
                    boolean right = pixelRight != transCode && pixelRight != color;
                    int pixelDown = map.getPixel(x, y - 1);
                    boolean down = pixelDown != transCode && pixelDown != color;
                    int pixelLeft = map.getPixel(x - 1, y);
                    boolean left = pixelLeft != transCode && pixelLeft != color;
                    if (up || right || down || left) {
                        map.setColor(color);
                        map.drawPixel(x, y);
                    }
                }
            }
        }
        Texture glowingTexture = new Texture(map);
        map.dispose();
        return glowingTexture;
    }
}
