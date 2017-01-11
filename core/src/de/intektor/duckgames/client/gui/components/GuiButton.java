package de.intektor.duckgames.client.gui.components;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import de.intektor.duckgames.client.gui.GuiComponent;
import de.intektor.duckgames.client.gui.util.GuiUtils;

/**
 * @author Intektor
 */
public abstract class GuiButton extends GuiComponent {

    /**
     * Indicates whether the user started the click on this button
     */
    private boolean clickStarted;

    private GuiButtonCallback callback;

    public GuiButton(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    protected void renderComponent(float drawX, float drawY, int mouseX, int mouseY, OrthographicCamera camera, ShapeRenderer sR, SpriteBatch sB, float partialTicks) {
        super.renderComponent(drawX, drawY, mouseX, mouseY, camera, sR, sB, partialTicks);
        renderButton(drawX, drawY, mouseX, mouseY, camera, sR, sB, partialTicks);
    }

    protected abstract void renderButton(float drawX, float drawY, int mouseX, int mouseY, OrthographicCamera camera, ShapeRenderer sR, SpriteBatch sB, float partialTicks);

    @Override
    public void clickDown(int mouseX, int mouseY, int pointer, int button, float drawX, float drawY) {
        super.clickDown(mouseX, mouseY, pointer, button, drawX, drawY);
        if (GuiUtils.isPointInRegion(x, y, width, height, mouseX, mouseY) && isEnabled) {
            clickStarted = true;
        }
    }

    @Override
    public void clickUp(int mouseX, int mouseY, int pointer, int button, float drawX, float drawY) {
        super.clickUp(mouseX, mouseY, pointer, button, drawX, drawY);
        if (GuiUtils.isPointInRegion(x, y, width, height, mouseX, mouseY) && clickStarted) {
            callback.buttonCallback(this);
        }
        clickStarted = false;
    }

    public void setCallback(GuiButtonCallback callback) {
        this.callback = callback;
    }

    public interface GuiButtonCallback {

        /**
         * Gets called by {@link GuiComponent#clickUp(int, int, int, int, float, float)} when the user successfully clicked and released the pointer on a button
         */
        void buttonCallback(GuiButton button);
    }
}
