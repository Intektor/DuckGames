package de.intektor.duckgames.editor.spawns.renderer;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import de.intektor.duckgames.editor.IEntitySpawnRenderer;
import de.intektor.duckgames.editor.spawns.MeleeSpawn;

/**
 * @author Intektor
 */
public class MeleeSpawnRenderer implements IEntitySpawnRenderer<MeleeSpawn> {

    private Texture swordTexture = new Texture("assets/iron_sword.png");

    @Override
    public void renderInEditor(MeleeSpawn spawn, float mouseX, float mouseY, float x, float y, float width, float height, boolean drawnOnMouse, ShapeRenderer shapeRenderer, SpriteBatch spriteBatch) {
        spriteBatch.begin();
        spriteBatch.draw(swordTexture, x, y, width, height);
        spriteBatch.end();
    }

    @Override
    public void renderInScrollTool(MeleeSpawn spawn, float x, float y, float width, float height, ShapeRenderer shapeRenderer, SpriteBatch spriteBatch) {
        spriteBatch.begin();
        spriteBatch.draw(swordTexture, x, y, width, height);
        spriteBatch.end();
    }
}
