package de.intektor.duckgames.client.gui.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
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

    public static MousePos unprojectMousePosition(OrthographicCamera camera, int mouseX, int mouseY) {
        Vector3 unproject = camera.unproject(new Vector3(mouseX, mouseY, 0));
        return new MousePos(unproject.x, unproject.y);
    }

    public static MousePos unprojectMousePosition(OrthographicCamera camera) {
        return unprojectMousePosition(camera, Gdx.input.getX(), Gdx.input.getY());
    }
}
