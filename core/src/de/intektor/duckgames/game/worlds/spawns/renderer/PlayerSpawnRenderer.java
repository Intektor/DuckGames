package de.intektor.duckgames.game.worlds.spawns.renderer;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import de.intektor.duckgames.client.editor.EditableGameMap;
import de.intektor.duckgames.game.worlds.spawns.PlayerSpawn;

/**
 * @author Intektor
 */
public class PlayerSpawnRenderer extends AbstractEntitySpawnRenderer<PlayerSpawn> {

    private Texture texture;

    public PlayerSpawnRenderer() {
        this.texture = new Texture("duck.png");
    }

    @Override
    public void renderInEditor(PlayerSpawn spawn, float mouseX, float mouseY, float x, float y, float width, float height, boolean drawnOnMouse, ShapeRenderer shapeRenderer, SpriteBatch spriteBatch, EditableGameMap map, OrthographicCamera camera, float partialTicks, Color gColor) {
        Texture t = texture;
        if (gColor != null) t = getOrGenGTexture(texture, gColor);
        spriteBatch.begin();
        spriteBatch.draw(t, x, y, width, height);
        spriteBatch.end();
    }

    @Override
    public void renderInScrollTool(PlayerSpawn spawn, float x, float y, float width, float height, ShapeRenderer shapeRenderer, SpriteBatch spriteBatch, OrthographicCamera camera, float partialTicks, Color gColor) {
        Texture t = texture;
        if (gColor != null) t = getOrGenGTexture(texture, gColor);
        spriteBatch.begin();
        spriteBatch.draw(t, x, y, width, height);
        spriteBatch.end();
    }
}
