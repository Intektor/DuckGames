package de.intektor.duckgames.client.gui.components;

import de.intektor.duckgames.client.gui.GuiComponent;
import de.intektor.duckgames.client.gui.util.GuiUtils;

/**
 * @author Intektor
 */
public class GuiButton extends GuiComponent {

    /**
     * Indicates whether the user started the click on this button
     */
    private boolean clickStarted;

    private final int id;

    GuiButtonCallback callback;

    public GuiButton(int x, int y, int width, int height, int id) {
        super(x, y, width, height);
        this.id = id;
    }

    @Override
    public void clickDown(int mouseX, int mouseY, int pointer, int button) {
        super.clickDown(mouseX, mouseY, pointer, button);
        if (GuiUtils.isPointInRegion(x, y, width, height, mouseX, mouseY)) {
            clickStarted = true;
        }
    }

    @Override
    public void clickUp(int mouseX, int mouseY, int pointer, int button) {
        super.clickUp(mouseX, mouseY, pointer, button);
        if (GuiUtils.isPointInRegion(x, y, width, height, mouseX, mouseY) && clickStarted) {
            callback.buttonCallback(this);
        }
        clickStarted = false;
    }

    public void setCallback(GuiButtonCallback callback) {
        this.callback = callback;
    }

    public int getId() {
        return id;
    }

    public interface GuiButtonCallback {

        /**
         * Gets called by {@link GuiButton#clickUp(int, int, int, int)} when the user successfully clicked and released the pointer on a button
         */
        void buttonCallback(GuiButton button);
    }
}
