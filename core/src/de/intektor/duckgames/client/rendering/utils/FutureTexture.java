package de.intektor.duckgames.client.rendering.utils;

import com.badlogic.gdx.graphics.Texture;

/**
 * @author Intektor
 */
public class FutureTexture {

    private Texture texture;
    private boolean initialized;
    private String path;

    FutureTexture(String path) {
        this.path = path;
    }

    void initialize() {
        texture = new Texture(path);
        initialized = true;
    }

    public boolean isInitisalized() {
        return initialized;
    }

    public Texture getTexture() {
        return texture;
    }
}
