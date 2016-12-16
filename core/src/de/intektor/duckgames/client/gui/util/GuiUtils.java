package de.intektor.duckgames.client.gui.util;

import com.badlogic.gdx.Gdx;
import de.intektor.duckgames.DuckGamesClient;

/**
 * @author Intektor
 */
public class GuiUtils {

    private static DuckGamesClient dg = DuckGamesClient.getDuckGames();

    public static boolean isPointInRegion(float x, float y, float width, float height, float pX, float pY) {
        return pX >= x && pX <= x + width && pY >= y && pY <= y + height;
    }

    public static int unscaleScreenCoordX(float x) {
        double scale = dg.getPreferredScreenWidth() / (double) Gdx.graphics.getWidth();
        return (int) (x / scale);
    }

    public static int unscaleScreenCoordY(float y) {
        double scale = dg.getPreferredScreenHeight() / (double) Gdx.graphics.getHeight();
        return (int) (y / scale);
    }
}
