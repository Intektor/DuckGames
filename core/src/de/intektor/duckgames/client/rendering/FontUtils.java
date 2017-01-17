package de.intektor.duckgames.client.rendering;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.Align;

/**
 * @author Intektor
 */
public class FontUtils {

    private static GlyphLayout layout = new GlyphLayout();

    public static float getStringWidth(String text, BitmapFont font) {
        layout.setText(font, text);
        return layout.width;
    }

    public static float getStringHeight(String text, BitmapFont font) {
        layout.setText(font, text);
        return layout.height;
    }

    public static void splitString(String text, BitmapFont font, float maxWidth) {
        layout.setText(font, text, Color.WHITE, maxWidth, Align.left, true);
    }

    public static int getCharPosition(String text, BitmapFont font, int x) {
        int pos = 0;
        for (int i = 0; i < text.length(); i++) {
            String substring = text.substring(0, i);
            if (getStringWidth(substring, font) <= x) {
                pos = i;
            } else {
                break;
            }
        }
        if (getStringWidth(text, font) < x && text.length() > 0) pos++;
        return pos;
    }
}
