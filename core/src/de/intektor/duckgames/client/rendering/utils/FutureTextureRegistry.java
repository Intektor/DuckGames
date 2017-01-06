package de.intektor.duckgames.client.rendering.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Intektor
 */
public class FutureTextureRegistry {

    private static List<FutureTexture> list = new ArrayList<FutureTexture>();

    public static FutureTexture register(String path) {
        FutureTexture texture = new FutureTexture(path);
        list.add(texture);
        return texture;
    }

    public static void loadTextures() {
        for (FutureTexture texture : list) {
            texture.initialize();
        }
        list.clear();
    }

    private FutureTextureRegistry() {
    }
}
