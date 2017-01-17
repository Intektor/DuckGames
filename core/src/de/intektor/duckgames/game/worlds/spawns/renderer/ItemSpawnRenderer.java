package de.intektor.duckgames.game.worlds.spawns.renderer;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import de.intektor.duckgames.DuckGamesClient;
import de.intektor.duckgames.client.rendering.item.IItemRenderer;
import de.intektor.duckgames.client.editor.EditableGameMap;
import de.intektor.duckgames.client.editor.IEntitySpawnRenderer;
import de.intektor.duckgames.game.worlds.spawns.ItemSpawner;

/**
 * @author Intektor
 */
public class ItemSpawnRenderer implements IEntitySpawnRenderer<ItemSpawner> {

    @Override
    public void renderInEditor(ItemSpawner spawn, float mouseX, float mouseY, float x, float y, float width, float height, boolean drawnOnMouse, ShapeRenderer shapeRenderer, SpriteBatch spriteBatch, EditableGameMap map, OrthographicCamera camera, float partialTicks) {
        ItemSpawner.ItemSpawn itemSpawn = spawn.getMostImportantItemSpawn();
        DuckGamesClient.getDuckGames().getItemRendererRegistry().getRenderer(itemSpawn.getItem()).
                renderItemInEditor(itemSpawn.generateStack(), itemSpawn.getItem(), shapeRenderer, spriteBatch, camera, x, y, width, height, partialTicks);
        Color color;
        if (drawnOnMouse) {
            color = map.canPlaceEntitySpawnAtPosition(spawn, x + width / 2, y + height / 2) ? Color.GREEN : Color.RED;
        } else {
            color = map.canPlaceEntitySpawnAtPosition(spawn) ? Color.ORANGE : Color.RED;
        }
        shapeRenderer.begin();
        shapeRenderer.set(ShapeType.Line);
        shapeRenderer.setColor(color);
        shapeRenderer.rect(x, y, width, height);
        shapeRenderer.end();
    }

    @Override
    public void renderInScrollTool(ItemSpawner spawn, float x, float y, float width, float height, ShapeRenderer shapeRenderer, SpriteBatch spriteBatch, OrthographicCamera camera, float partialTicks) {
        ItemSpawner.ItemSpawn itemSpawn = spawn.getMostImportantItemSpawn();
        IItemRenderer renderer = DuckGamesClient.getDuckGames().getItemRendererRegistry().getRenderer(itemSpawn.getItem());
        renderer.renderItemInScrollTool(itemSpawn.generateStack(), itemSpawn.getItem(), shapeRenderer, spriteBatch, camera, x, y, width, height, 0, partialTicks);
    }
}
