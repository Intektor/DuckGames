package de.intektor.duckgames.client.rendering.block;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import de.intektor.duckgames.block.Block;

/**
 * @author Intektor
 */
public interface IBlockRenderer<T extends Block> {

    void renderBlockInWorld(T block, ShapeRenderer sR, SpriteBatch sB, OrthographicCamera camera, float x, float y, float width, float height, float partialTicks);

    void renderBlockInEditor(T block, ShapeRenderer sR, SpriteBatch sB, OrthographicCamera camera, float x, float y, float width, float height, float partialTicks);

    void renderBlockInScrollTool(T block, ShapeRenderer sR, SpriteBatch sB, OrthographicCamera camera, float x, float y, float width, float height, float partialTicks, Color gColor);
}
