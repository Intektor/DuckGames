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

    /**
     * Splits the String at the given width with \n
     *
     * @param text     the text to be split
     * @param font     the font used
     * @param maxWidth the max width of the string per line
     * @return a string containing the source text split by \n
     */
    public static String splitString(String text, BitmapFont font, float maxWidth) {
        StringBuilder builder = new StringBuilder();
        int currentIndex = 1;
        while (getStringWidth(text, font) > maxWidth) {
            while (getStringWidth(text.substring(0, currentIndex), font) < maxWidth) {
                currentIndex++;
            }
            CharSequence local = text.substring(0, currentIndex);
            builder.append(local).append("\n");
            text = text.substring(currentIndex, text.length());
            currentIndex = 0;
        }
        if (text.trim().length() > 0) {
            builder.append(text);
        }
        return builder.toString();
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
