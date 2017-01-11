package de.intektor.duckgames.client;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;

/**
 * @author Intektor
 */
public class DeviceUtils {

    public static boolean isDeviceTouch() {
        Application.ApplicationType type = Gdx.app.getType();
        return type == Application.ApplicationType.Android || type == Application.ApplicationType.iOS;
    }
}
