package de.intektor.duckgames.client.rendering;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.Pools;

/**
 * @author Intektor
 */
public class FontUtils {

    public static float getStringWidth(String text, BitmapFont font) {
        GlyphLayout layout = Pools.obtain(GlyphLayout.class);
        layout.setText(font, text);
        return layout.width;
    }

    public static float getStringHeight(String text, BitmapFont font) {
        GlyphLayout layout = Pools.obtain(GlyphLayout.class);
        layout.setText(font, text);
        return layout.height;
    }
}
