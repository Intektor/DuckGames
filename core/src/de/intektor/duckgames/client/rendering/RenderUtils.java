package de.intektor.duckgames.client.rendering;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * @author Intektor
 */
public class RenderUtils {

    public static void drawString(String text, BitmapFont font, float x, float y, SpriteBatch spriteBatch) {
        drawString(text, font, x, y, spriteBatch, false);
    }

    public static void drawString(String text, BitmapFont font, float x, float y, SpriteBatch spriteBatch, boolean centered) {
        drawString(text, font, x, y, spriteBatch, centered, centered);
    }

    public static void drawString(String text, BitmapFont font, float x, float y, SpriteBatch spriteBatch, boolean centerX, boolean centerY) {
        if (centerX) {
            float stringWidth = FontUtils.getStringWidth(text, font);
            x -= stringWidth / 2;
        }
        if (centerY) {
            float stringHeight = FontUtils.getStringHeight(text, font);
            y -= stringHeight / 2;
        }
        font.draw(spriteBatch, text, x, y);
    }

}
