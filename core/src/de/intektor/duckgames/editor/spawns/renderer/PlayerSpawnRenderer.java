package de.intektor.duckgames.editor.spawns.renderer;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import de.intektor.duckgames.editor.EntitySpawnRenderer;
import de.intektor.duckgames.editor.spawns.PlayerSpawn;

/**
 * @author Intektor
 */
public class PlayerSpawnRenderer implements EntitySpawnRenderer<PlayerSpawn> {

    @Override
    public void render(PlayerSpawn spawn, float mouseX, float mouseY, float x, float y, float width, float height, boolean drawnOnMouse, ShapeRenderer shapeRenderer, SpriteBatch spriteBatch) {
        shapeRenderer.begin();
        shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.YELLOW);
        shapeRenderer.rect(x, y, width, height);
        shapeRenderer.end();
    }
}
