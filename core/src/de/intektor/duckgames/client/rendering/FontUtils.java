package de.intektor.duckgames.client.rendering;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

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
}
