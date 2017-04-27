package de.intektor.duckgames.client.rendering;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
        spriteBatch.enableBlending();
        font.setColor(color);
        float rx = centerX ? x - FontUtils.getStringWidth(text, font) / 2 : x;
        float ry = centerY ? y + FontUtils.getStringHeight(text, font) / 2 : y;
        font.draw(spriteBatch, text, rx, ry);
        spriteBatch.disableBlending();
    }

    public static void drawRotatedTexture(TextureRegion texture, SpriteBatch sB, float x, float y, float scale, float rotation) {
        sB.draw(texture, x, y, scale / 2, scale / 2, scale, scale, 1, 1, rotation);
    }

    public static void drawRotatedTexture(Texture texture, SpriteBatch sB, float x, float y, float scale, float rotation) {
        drawRotatedTexture(new TextureRegion(texture), sB, x, y, scale, rotation);
    }

    /**
     * @return the interpolated point using {@link DuckGamesClient#getPartialTicks()}
     */
    public static Point2f getInterpolatedPoint(double prevX, double prevY, double x, double y) {
        return getInterpolatedPoint(prevX, prevY, x, y, dg.getPartialTicks());
    }

    public static Point2f getInterpolatedPoint(double prevX, double prevY, double x, double y, float partialTicks) {
        return new Point2f((float) (prevX + (x - prevX) * partialTicks), (float) (prevY + (y - prevY) * partialTicks));
    }

    public static Point2f getInterpolatedEntityPos(Entity entity, float partialTicks) {
        return getInterpolatedPoint(entity.prevPosX, entity.prevPosY, entity.posX, entity.posY, partialTicks);
    }

    public static void enableBlending() {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
    }

    public static void disableBlending() {
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

}
