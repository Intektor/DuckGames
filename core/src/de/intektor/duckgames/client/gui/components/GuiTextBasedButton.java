package de.intektor.duckgames.client.gui.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import de.intektor.duckgames.client.rendering.RenderUtils;

/**
 * @author Intektor
 */
public class GuiTextBasedButton extends GuiButton {

    private String text;

    public GuiTextBasedButton(int x, int y, int width, int height, String text) {
        super(x, y, width, height);
        this.text = text;
    }

    @Override
    protected void renderButton(float drawX, float drawY, int mouseX, int mouseY, OrthographicCamera camera, ShapeRenderer sR, SpriteBatch sB, float partialTicks) {
        sR.begin();
        sR.set(ShapeType.Filled);
        sR.setColor(isHovered(mouseX, mouseY) ? Color.GRAY : Color.WHITE);
        sR.rect(drawX, drawY, width, height);
        sR.end();
        sB.begin();
        RenderUtils.drawString(text, dg.defaultFont16, drawX + width / 2, drawY + height / 2, sB, Color.BLACK, true);
        sB.end();
    }
}
