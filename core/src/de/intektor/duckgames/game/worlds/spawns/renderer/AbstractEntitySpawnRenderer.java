package de.intektor.duckgames.game.worlds.spawns.renderer;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import de.intektor.duckgames.client.editor.EntitySpawn;
import de.intektor.duckgames.client.editor.IEntitySpawnRenderer;
import de.intektor.duckgames.client.rendering.utils.TextureUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Intektor
 */
public abstract class AbstractEntitySpawnRenderer<T extends EntitySpawn> implements IEntitySpawnRenderer<T> {

    private Map<Integer, Texture> glowingTextures = new HashMap<Integer, Texture>();

    protected Texture getOrGenGTexture(Texture texture, Color color) {
        if (!glowingTextures.containsKey(color.toIntBits())) {
            glowingTextures.put(color.toIntBits(), TextureUtils.generateGlowingTexture(texture, color.toIntBits()));
        }
        return glowingTextures.get(color.toIntBits());
    }
}
