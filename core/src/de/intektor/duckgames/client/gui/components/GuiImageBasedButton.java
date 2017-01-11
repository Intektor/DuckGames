package de.intektor.duckgames.client.gui.components;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;


/**
 * @author Intektor
 */
public class GuiImageBasedButton extends GuiButton {

    private Texture texture;

    public GuiImageBasedButton(int x, int y, int width, int height, Texture texture) {
        super(x, y, width, height);
        this.texture = texture;
    }

    @Override
    protected void renderButton(float drawX, float drawY, int mouseX, int mouseY, OrthographicCamera camera, ShapeRenderer sR, SpriteBatch sB, float partialTicks) {
        sB.begin();
        sB.draw(texture, drawX, drawY, width, height);
        sB.end();
    }
}
