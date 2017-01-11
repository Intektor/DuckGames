package de.intektor.duckgames.client.gui;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import de.intektor.duckgames.DuckGamesClient;
import de.intektor.duckgames.client.gui.util.GuiUtils;

/**
 * @author Intektor
 */
public class GuiComponent {

    protected int x, y;
    protected int width, height;
    protected boolean isShown = true;
    protected boolean isEnabled = true;

    protected final DuckGamesClient dg = DuckGamesClient.getDuckGames();

    public GuiComponent(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * Gets called every renderInEditor tick, used to renderInEditor the component
     */
    protected void renderComponent(float drawX, float drawY, int mouseX, int mouseY, OrthographicCamera camera, ShapeRenderer sR, SpriteBatch sB, float partialTicks) {

    }

    /**
     * Gets called every updateWorld tick, used to updateWorld the component
     */
    protected void updateComponent(int mouseX, int mouseY, float drawX, float drawY) {

    }

    /**
     * Gets called when the user clicks somewhere on the gui
     */
    public void clickDown(int mouseX, int mouseY, int pointer, int button, float drawX, float drawY) {

    }

    /**
     * Gets called as long as the user holds the click
     */
    public void clicked(int mouseX, int mouseY, int pointer) {

    }

    /**
     * Gets called when the user releases his click from the gui
     */
    public void clickUp(int mouseX, int mouseY, int pointer, int button, float drawX, float drawY) {

    }

    /**
     * Gets called when the user just pressed a key
     */
    public void keyDown(int mouseX, int mouseY, int keyCode) {

    }

    /**
     * Gets called as long the user is pressing a key
     */
    public void keyPressed(int mouseX, int mouseY, int keyCode) {

    }

    /**
     * Gets called when the user releases a key
     */
    public void keyReleased(int mouseX, int mouseY, int keyCode) {

    }

    /**
     * Gets called when the user types a character
     */
    public void charTyped(int mouseX, int mouseY, char character) {

    }

    /**
     * Gets called when the user moves the mouse without having it clicked
     */
    public void mouseMoved(int mouseX, int mouseY, int prevX, int prevY) {

    }

    /**
     * Gets called when the user clicks and moves his pointer
     */
    public void clickDragged(int mouseX, int mouseY, int prevX, int prevY, int pointer) {

    }

    /**
     * Gets called when the user scrolls with his scroll wheel
     */
    public void scroll(int mouseX, int mouseY, int scrollAmount) {

    }

    /**
     * Moves the component relative to its current position
     */
    public void moveComponent(int amountX, int amountY) {
        x += amountX;
        y += amountY;
    }

    /**
     * Sets the position of this component
     */
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * @return whether the component is hovered with the mouse
     */
    public boolean isHovered(int mouseX, int mouseY) {
        return GuiUtils.isPointInRegion(x, y, width, height, mouseX, mouseY) && isShown();
    }

    public boolean isShown() {
        return isShown;
    }

    public void setShown(boolean shown) {
        isShown = shown;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
