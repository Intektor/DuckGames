package de.intektor.duckgames.client.gui.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import de.intektor.duckgames.DuckGamesClient;

import javax.vecmath.Point2f;

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

    public static float scaleScreenCoordX(float x) {
        double scale = dg.getPreferredScreenWidth() / (double) Gdx.graphics.getWidth();
        return (float) (x * scale);
    }

    public static int unscaleScreenCoordY(float y) {
        double scale = dg.getPreferredScreenHeight() / (double) Gdx.graphics.getHeight();
        return (int) (y / scale);
    }

    public static float scaleScreenCoordY(float y) {
        double scale = dg.getPreferredScreenHeight() / (double) Gdx.graphics.getHeight();
        return (float) (y * scale);
    }

    public static MousePos unprojectMousePosition(OrthographicCamera camera, int mouseX, int mouseY) {
        Vector3 unproject = camera.unproject(new Vector3(mouseX, mouseY, 0));
        return new MousePos(unproject.x, unproject.y);
    }

    public static MousePos unprojectMousePosition(OrthographicCamera camera) {
        return unprojectMousePosition(camera, Gdx.input.getX(), Gdx.input.getY());
    }

    public static Point2f  projectWorldPosition(OrthographicCamera camera, float posX, float posY) {
        Vector3 project = camera.project(new Vector3(posX, posY, 0));
        float scaleX = (float) (dg.getPreferredScreenWidth() / (double) Gdx.graphics.getWidth());
        float scaleY = (float) (dg.getPreferredScreenHeight() / (double) Gdx.graphics.getHeight());
        return new Point2f(project.x * scaleX, project.y * scaleY);
    }

    public static int scaleMouseX() {
        return scaleMouseX(Gdx.input.getX());
    }

    public static int scaleMouseY() {
        return scaleMouseY(Gdx.input.getY());
    }

    public static int scaleMouseX(int mouseX) {
        double scale = DuckGamesClient.getDuckGames().getPreferredScreenWidth() / (double) Gdx.graphics.getWidth();
        return (int) (mouseX * scale);
    }

    public static int scaleMouseY(int mouseY) {
        double scale = DuckGamesClient.getDuckGames().getPreferredScreenHeight() / (double) Gdx.graphics.getHeight();
        return DuckGamesClient.getDuckGames().getPreferredScreenHeight() - (int) ((mouseY * scale));
    }


}
