package de.intektor.duckgames.client.rendering;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.intektor.duckgames.DuckGamesClient;
import de.intektor.duckgames.entity.Entity;

import javax.vecmath.Point2f;

/**
 * @author Intektor
 */
public class RenderUtils {

    private static DuckGamesClient dg = DuckGamesClient.getDuckGames();

    public static void drawString(String text, BitmapFont font, float x, float y, SpriteBatch spriteBatch, Color color) {
        drawString(text, font, x, y, spriteBatch, color, false);
    }

    public static void drawString(String text, BitmapFont font, float x, float y, SpriteBatch spriteBatch, Color color, boolean centered) {
        drawString(text, font, x, y, spriteBatch, color, centered, centered);
    }

    public static void drawString(String text, BitmapFont font, float x, float y, SpriteBatch spriteBatch, Color color, boolean centerX, boolean centerY) {
        font.setColor(color);
        float rx = centerX ? x - FontUtils.getStringWidth(text, font) / 2 : x;
        float ry = centerY ? y + FontUtils.getStringHeight(text, font) / 2 : y;
        font.draw(spriteBatch, text, rx, ry);
    }

    /**
     * @return the interpolated point using {@link DuckGamesClient#getPartialTicks()}
     */
    public static Point2f getInterpolatedPoint(double prevX, double prevY, double x, double y) {
        return new Point2f((float) (prevX + (x - prevX) * dg.getPartialTicks()), (float) (prevY + (y - prevY) * dg.getPartialTicks()));
    }

    public static Point2f getInterpolatedEntityPos(Entity entity) {
        return getInterpolatedPoint(entity.prevPosX, entity.prevPosY, entity.posX, entity.posY);
    }
}
