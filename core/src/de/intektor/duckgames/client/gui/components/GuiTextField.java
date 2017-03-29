package de.intektor.duckgames.client.gui.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import de.intektor.duckgames.client.gui.GuiComponent;
import de.intektor.duckgames.client.gui.util.GuiUtils;
import de.intektor.duckgames.client.rendering.FontUtils;
import de.intektor.duckgames.client.rendering.RenderUtils;

/**
 * @author Intektor
 */
public class GuiTextField extends GuiComponent {

    private String currentlyWritten;
    private String infoText;
    private BitmapFont font = dg.defaultFont28;

    private boolean isActive;
    private boolean showCursor;
    private long lastSwitchCursorTime;

    private int cursorPosition;

    public GuiTextField(int x, int y, int width, int height, String infoText) {
        this(x, y, width, height, infoText, "");
    }

    public GuiTextField(int x, int y, int width, int height, String infoText, String defaultText) {
        super(x, y, width, height);
        this.infoText = infoText;
        currentlyWritten = defaultText;
    }

    @Override
    protected void renderComponent(float drawX, float drawY, int mouseX, int mouseY, OrthographicCamera camera, ShapeRenderer sR, SpriteBatch sB, float partialTicks) {
        super.renderComponent(drawX, drawY, mouseX, mouseY, camera, sR, sB, partialTicks);

        if (System.currentTimeMillis() - lastSwitchCursorTime > 500) {
            lastSwitchCursorTime = System.currentTimeMillis();
            showCursor = !showCursor;
        }

        sR.begin();
        sR.set(ShapeRenderer.ShapeType.Filled);
        sR.setColor(Color.BLACK);
        sR.rect(drawX, drawY, width, height);
        sR.set(ShapeRenderer.ShapeType.Line);
        sR.setColor(Color.WHITE);
        sR.rect(drawX, drawY, width, height);
        sR.end();

        sB.begin();
        String text;
        Color color;
        float y = drawY + height / 2;
        if (currentlyWritten.length() == 0 && !isActive) {
            text = infoText;
            color = new Color(1, 1, 1, 0.5f);
        } else {
            text = currentlyWritten;
            color = Color.WHITE;
            if (showCursor && isActive) {
                RenderUtils.drawString("|", font, drawX + FontUtils.getStringWidth(currentlyWritten.substring(0, cursorPosition), font), y, sB, Color.WHITE, false, true);
            }
        }
        RenderUtils.drawString(text, font, drawX + 3, y, sB, color, false, true);
        sB.end();
    }

    @Override
    public void clickDown(int mouseX, int mouseY, int pointer, int button, float drawX, float drawY) {
        super.clickDown(mouseX, mouseY, pointer, button, drawX, drawY);
        isActive = GuiUtils.isPointInRegion(x, y, width, height, mouseX, mouseY);
        if (isActive) {
            cursorPosition = FontUtils.getCharPosition(currentlyWritten, font, mouseX);
        }
    }

    @Override
    public void clickUp(int mouseX, int mouseY, int pointer, int button, float drawX, float drawY) {
        super.clickUp(mouseX, mouseY, pointer, button, drawX, drawY);
    }

    @Override
    public void charTyped(int mouseX, int mouseY, char character) {
        super.charTyped(mouseX, mouseY, character);
        if (isActive) {
            switch (character) {
                case '\b':
                    if (currentlyWritten.length() > 0 && cursorPosition > 0) {
                        currentlyWritten = currentlyWritten.substring(0, cursorPosition - 1) + currentlyWritten.substring(cursorPosition);
                        cursorPosition--;
                        lastSwitchCursorTime = System.currentTimeMillis();
                        showCursor = true;
                    }
                    break;
                default:
                    if (Character.isLetterOrDigit(character) || character == ' '
                            || character == '?' || character == '!' || character == '.' || character == ',' || character == ':') {
                        if (FontUtils.getStringWidth(currentlyWritten + character, font) <= width) {
                            currentlyWritten = currentlyWritten.substring(0, cursorPosition) + character + currentlyWritten.substring(cursorPosition);
                            cursorPosition++;
                            lastSwitchCursorTime = System.currentTimeMillis();
                            showCursor = true;
                        }
                    }
                    break;
            }
        }
    }

    public String getText() {
        return currentlyWritten;
    }

    public void setText(String text) {
        this.currentlyWritten = text;
        cursorPosition = 0;
    }
}
