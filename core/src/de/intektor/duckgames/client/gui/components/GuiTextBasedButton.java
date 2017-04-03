package de.intektor.duckgames.client.gui.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import de.intektor.duckgames.client.rendering.RenderUtils;

/**
 * @author Intektor
 */
public class GuiTextBasedButton extends GuiButton {

    private String text;
    private boolean textCentered;
    private Color color, hoveredColor, textColor;

    public GuiTextBasedButton(int x, int y, int width, int height, String text, boolean textCentered) {
        this(x, y, width, height, text, textCentered, new Color(0.2f, 0.2f, 0.2f, 1), new Color(0.25f, 0.25f, 0.25f, 1), Color.WHITE);
    }

    public GuiTextBasedButton(int x, int y, int width, int height, String text, boolean textCentered, Color color, Color hoveredColor, Color textColor) {
        super(x, y, width, height);
        this.text = text;
        this.textCentered = textCentered;
        this.color = color;
        this.hoveredColor = hoveredColor;
        this.textColor = textColor;
    }

    @Override
    protected void renderButton(float drawX, float drawY, int mouseX, int mouseY, OrthographicCamera camera, ShapeRenderer sR, SpriteBatch sB, float partialTicks) {
        RenderUtils.enableBlending();
        sR.begin();
        sR.set(ShapeRenderer.ShapeType.Filled);
        sR.setColor(isHovered(mouseX, mouseY) ? hoveredColor : color);
        sR.rect(drawX, drawY, width, height);
        sR.end();
        sB.begin();
        RenderUtils.drawString(text, dg.defaultFont28, drawX + (textCentered ? width / 2 : 10), drawY + height / 2, sB, textColor, textCentered, true);
        sB.end();
        RenderUtils.disableBlending();
    }

    public void setTextColor(Color textColor) {
        this.textColor = textColor;
    }
}
