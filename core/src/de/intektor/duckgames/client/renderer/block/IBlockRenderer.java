package de.intektor.duckgames.client.renderer.block;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import de.intektor.duckgames.block.Block;

/**
 * @author Intektor
 */
public interface IBlockRenderer<T extends Block> {

    void renderBlockInWorld(T block, ShapeRenderer sR, SpriteBatch sB, OrthographicCamera camera, int x, int y);

    void renderBlockInEditor(T block, ShapeRenderer sR, SpriteBatch sB, OrthographicCamera camera, float x, float y, float width, float height);

    void renderBlockInScrollTool(T block, ShapeRenderer sR, SpriteBatch sB, OrthographicCamera camera, float x, float y, float width, float height);
}
