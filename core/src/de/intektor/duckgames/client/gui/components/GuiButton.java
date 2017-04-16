package de.intektor.duckgames.client.gui.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import de.intektor.duckgames.client.gui.Gui;
import de.intektor.duckgames.client.gui.GuiComponent;
import de.intektor.duckgames.client.gui.util.GuiUtils;
import de.intektor.duckgames.client.rendering.RenderUtils;

/**
 * @author Intektor
 */
public abstract class GuiButton extends GuiComponent {

    /**
     * Indicates whether the user started the click on this button
     */
    private boolean clickStarted;

    private int clickX, clickY;
    private long clickTicks;

    private GuiButtonCallback callback;

    public GuiButton(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    protected void renderComponent(float drawX, float drawY, int mouseX, int mouseY, OrthographicCamera camera, ShapeRenderer sR, SpriteBatch sB, float partialTicks) {
        super.renderComponent(drawX, drawY, mouseX, mouseY, camera, sR, sB, partialTicks);
        renderButton(drawX, drawY, mouseX, mouseY, camera, sR, sB, partialTicks);
        if (clickStarted) {
            RenderUtils.enableBlending();
            sR.begin(ShapeRenderer.ShapeType.Filled);
            sR.setColor(new Color(0.5f, 0.5f, 0.5f, 0.3f));
            Gdx.gl.glEnable(GL20.GL_SCISSOR_TEST);
            sR.circle(clickX, clickY, clickTicks * 6);
            Gdx.gl.glScissor(GuiUtils.unscaleScreenCoordX(drawX), GuiUtils.unscaleScreenCoordY(drawY), GuiUtils.unscaleScreenCoordX(width), GuiUtils.unscaleScreenCoordY(height));
            sR.end();
            RenderUtils.disableBlending();
            Gdx.gl.glDisable(GL20.GL_SCISSOR_TEST);
        }
    }

    @Override
    protected void updateComponent(int mouseX, int mouseY, float drawX, float drawY) {
        super.updateComponent(mouseX, mouseY, drawX, drawY);
        clickTicks++;
    }

    protected abstract void renderButton(float drawX, float drawY, int mouseX, int mouseY, OrthographicCamera camera, ShapeRenderer sR, SpriteBatch sB, float partialTicks);

    @Override
    public void clickDown(int mouseX, int mouseY, int pointer, int button, float drawX, float drawY) {
        super.clickDown(mouseX, mouseY, pointer, button, drawX, drawY);
        if (GuiUtils.isPointInRegion(x, y, width, height, mouseX, mouseY) && isEnabled && isShown) {
            clickStarted = true;
            clickX = mouseX;
            clickY = mouseY;
            clickTicks = 0;
        }
    }

    @Override
    public void clickUp(int mouseX, int mouseY, int pointer, int button, float drawX, float drawY) {
        super.clickUp(mouseX, mouseY, pointer, button, drawX, drawY);
        if (GuiUtils.isPointInRegion(x, y, width, height, mouseX, mouseY) && clickStarted && isShown) {
            callback.buttonCallback(this);
        }
        clickStarted = false;
    }

    public void setCallback(GuiButtonCallback callback) {
        this.callback = callback;
    }

    @Override
    public void setCurrentGui(Gui currentGui) {
        super.setCurrentGui(currentGui);
        setCallback(currentGui);
    }

    public interface GuiButtonCallback {

        /**
         * Gets called by {@link GuiComponent#clickUp(int, int, int, int, float, float)} when the user successfully clicked and released the pointer on a button
         */
        void buttonCallback(GuiButton button);
    }
}
