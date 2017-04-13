package de.intektor.duckgames.client.rendering.item;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import de.intektor.duckgames.client.entity.EntityPlayerSP;
import de.intektor.duckgames.item.Item;
import de.intektor.duckgames.item.ItemStack;

/**
 * @author Intektor
 */
public interface IItemRenderer<T extends Item> {

    void renderItemInWorld(ItemStack stack, T item, ShapeRenderer sR, SpriteBatch sB, OrthographicCamera camera, float x, float y, float width, float height, float partialTicks);

    void renderItemOnPlayer(ItemStack stack, T item, EntityPlayerSP player, ShapeRenderer sR, SpriteBatch sB, OrthographicCamera camera, float partialTicks);

    void renderItemInEditor(ItemStack stack, T item, ShapeRenderer sR, SpriteBatch sB, OrthographicCamera camera, float x, float y, float width, float height, float partialTicks, Color gColor);

    void renderItemInScrollTool(ItemStack stack, T item, ShapeRenderer sR, SpriteBatch sB, OrthographicCamera camera, float x, float y, float width, float height, float rotation, float partialTicks, Color gColor);

    Texture getItemTexture();
}
