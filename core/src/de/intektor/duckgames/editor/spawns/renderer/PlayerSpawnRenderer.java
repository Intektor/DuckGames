package de.intektor.duckgames.editor.spawns.renderer;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import de.intektor.duckgames.editor.IEntitySpawnRenderer;
import de.intektor.duckgames.editor.spawns.PlayerSpawn;

/**
 * @author Intektor
 */
public class PlayerSpawnRenderer implements IEntitySpawnRenderer<PlayerSpawn> {

    private Texture texture;

    public PlayerSpawnRenderer() {
        this.texture = new Texture("assets/duck.png");
    }

    @Override
    public void renderInEditor(PlayerSpawn spawn, float mouseX, float mouseY, float x, float y, float width, float height, boolean drawnOnMouse, ShapeRenderer shapeRenderer, SpriteBatch spriteBatch) {
        spriteBatch.begin();
        spriteBatch.draw(texture, x, y, width, height);
        spriteBatch.end();
    }

    @Override
    public void renderInScrollTool(PlayerSpawn spawn, float x, float y, float width, float height, ShapeRenderer shapeRenderer, SpriteBatch spriteBatch) {
        spriteBatch.begin();
        spriteBatch.draw(texture, x, y, width, height);
        spriteBatch.end();
    }
}
