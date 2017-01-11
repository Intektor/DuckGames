package de.intektor.duckgames.editor.spawns.renderer;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import de.intektor.duckgames.DuckGamesClient;
import de.intektor.duckgames.editor.IEntitySpawnRenderer;
import de.intektor.duckgames.editor.spawns.ItemSpawner;

/**
 * @author Intektor
 */
public class ItemSpawnRenderer implements IEntitySpawnRenderer<ItemSpawner> {

    @Override
    public void renderInEditor(ItemSpawner spawn, float mouseX, float mouseY, float x, float y, float width, float height, boolean drawnOnMouse, ShapeRenderer shapeRenderer, SpriteBatch spriteBatch, OrthographicCamera camera, float partialTicks) {
        ItemSpawner.ItemSpawn itemSpawn = spawn.getSpawnList().get(0);
        DuckGamesClient.getDuckGames().getItemRendererRegistry().getRenderer(itemSpawn.getItem()).
                renderItemInEditor(itemSpawn.generateStack(), itemSpawn.getItem(), shapeRenderer, spriteBatch, camera, x, y, width, height, partialTicks);
    }

    @Override
    public void renderInScrollTool(ItemSpawner spawn, float x, float y, float width, float height, ShapeRenderer shapeRenderer, SpriteBatch spriteBatch, OrthographicCamera camera, float partialTicks) {
        ItemSpawner.ItemSpawn itemSpawn = spawn.getSpawnList().get(0);
        DuckGamesClient.getDuckGames().getItemRendererRegistry().getRenderer(itemSpawn.getItem()).
                renderItemInScrollTool(itemSpawn.generateStack(), itemSpawn.getItem(), shapeRenderer, spriteBatch, camera, x, y, width, height, partialTicks);
    }
}
