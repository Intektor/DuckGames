package de.intektor.duckgames.client.gui;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import de.intektor.duckgames.DuckGamesClient;
import de.intektor.duckgames.client.gui.components.GuiButton;

import javax.vecmath.Point2i;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Intektor
 */
public class Gui implements InputProcessor, GuiButton.GuiButtonCallback {

    protected DuckGamesClient dg;
    protected int offsetX, offsetY;
    protected int width, height;
    protected SpriteBatch spriteBatch;
    protected ShapeRenderer shapeRenderer;
    protected OrthographicCamera camera;

    protected List<GuiComponent> componentList = new ArrayList<GuiComponent>();

    protected Input input;
    protected Graphics graphics;

    protected boolean allowInput = true;

    private Map<Integer, Point2i> lastPointerPositionMap = new HashMap<Integer, Point2i>();

    {
        dg = DuckGamesClient.getDuckGames();
        spriteBatch = dg.getDefaultSpriteBatch();
        shapeRenderer = dg.getDefaultShapeRenderer();
        camera = dg.getDefaultCamera();
        InputMultiplexer multiplexer = new InputMultiplexer(this);
        Gdx.input.setInputProcessor(multiplexer);
        input = Gdx.input;
        graphics = Gdx.graphics;
        for (int i = 0; i < 10; i++) {
            lastPointerPositionMap.put(i, new Point2i(0, 0));
        }
        width = dg.getPreferredScreenWidth();
        height = dg.getPreferredScreenHeight();
    }

    /**
     * Gets called when {@link DuckGamesClient#showGui(Gui)} loads this Gui
     */
    public void enterGui() {
    }

    /**
     * Renders this Gui, called by {@link DuckGamesClient#renderGame()}. Receives the unscaled mouse amounts directly from {@link Gdx#input} <br>
     * Calls {@link Gui#renderGui(int, int, OrthographicCamera, float)} with scaled mouse amounts.
     *
     * @see Gui#scaleMouseX(int)
     * @see Gui#scaleMouseY(int)
     */
    public final void render(int mouseX, int mouseY, OrthographicCamera camera, float partialTicks) {
        renderGui(scaleMouseX(mouseX), scaleMouseY(mouseY), camera, partialTicks);

    }

    /**
     * Called by {@link Gui#render(int, int, OrthographicCamera, float)} with scaled mouse amounts. <br>
     * This method should be overwritten if individual rendering is needed
     *
     * @see Gui#scaleMouseX(int)
     * @see Gui#scaleMouseY(int)
     */
    protected void renderGui(int mouseX, int mouseY, OrthographicCamera camera, float partialTicks) {
        renderGuiComponents(mouseX, mouseY, camera, partialTicks);
    }

    /**
     * Renders the gui components of this gui, default called by {@link Gui#renderGui(int, int, OrthographicCamera, float)}
     */
    protected void renderGuiComponents(int mouseX, int mouseY, OrthographicCamera camera, float partialTicks) {
        for (GuiComponent guiComponent : componentList) {
            if (guiComponent.isShown()) {
                guiComponent.renderComponent(mouseX, mouseY, camera, shapeRenderer, spriteBatch, partialTicks);
            }
        }
    }

    /**
     * Updates this Gui, calles by {@link DuckGamesClient#updateGame()}. Receives the unscaled mouse amounts directly from {@link Gdx#input} <br>
     * Calls {@link Gui#updateGui(int, int)} with scaled mouse amounts.
     *
     * @see Gui#scaleMouseX(int)
     * @see Gui#scaleMouseY(int)
     */
    public final void update(int mouseX, int mouseY) {
        updateGui(scaleMouseX(mouseX), scaleMouseY(mouseY));
    }

    /**
     * Called by {@link Gui#update(int, int)} with scaled mouse amounts. <br>
     * This method should be overwritten if individual updating is needed
     *
     * @see Gui#scaleMouseX(int)
     * @see Gui#scaleMouseY(int)
     */
    protected void updateGui(int mouseX, int mouseY) {
        updateGuiComponents(mouseX, mouseY);
    }

    /**
     * Updates the gui components of this gui, default called by {@link Gui#updateGui(int, int)}
     */
    protected void updateGuiComponents(int mouseX, int mouseY) {
        for (GuiComponent guiComponent : componentList) {
            if (guiComponent.isEnabled()) {
                guiComponent.updateComponent(mouseX, mouseY);
            }
        }
    }

    @Override
    public final boolean keyDown(int keycode) {
        if (!allowInput) return false;
        int mouseX = scaleMouseX();
        int mouseY = scaleMouseY();
        for (GuiComponent component : componentList) {
            if (component.isEnabled()) {
                component.keyDown(mouseX, mouseY, keycode);
            }
        }
        keyPushed(keycode, mouseX, mouseY);
        return false;
    }

    /**
     * Called by {@link Gui#keyDown(int)} when the user pushed down a key, uses scaled mouse amounts
     *
     * @see Gui#scaleMouseX(int)
     * @see Gui#scaleMouseY(int)
     */
    public void keyPushed(int keyCode, int mouseX, int mouseY) {

    }

    @Override
    public final boolean keyUp(int keycode) {
        if (!allowInput) return false;
        int mouseX = scaleMouseX();
        int mouseY = scaleMouseY();
        for (GuiComponent component : componentList) {
            if (component.isEnabled()) {
                component.keyReleased(mouseX, mouseY, keycode);
            }
        }
        keyReleased(keycode, mouseX, mouseY);
        return false;
    }

    /**
     * Called by {@link Gui#keyUp(int)} when the user released a pushed down key, uses scaled mouse amounts
     *
     * @see Gui#scaleMouseX(int)
     * @see Gui#scaleMouseY(int)
     */
    public void keyReleased(int keyCode, int mouseX, int mouseY) {

    }

    @Override
    public final boolean keyTyped(char character) {
        if (!allowInput) return false;
        int mouseX = scaleMouseX();
        int mouseY = scaleMouseY();
        for (GuiComponent component : componentList) {
            if (component.isEnabled()) {
                component.charTyped(mouseX, mouseY, character);
            }
        }
        charTyped(character, mouseX, mouseY);
        return false;
    }

    /**
     * Called by {@link Gui#keyTyped(char)} when the user types, uses scaled mouse amounts
     *
     * @see Gui#scaleMouseX(int)
     * @see Gui#scaleMouseY(int)
     */
    public void charTyped(char character, int mouseX, int mouseY) {

    }

    @Override
    public final boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (!allowInput) return false;
        int mouseX = scaleMouseX(screenX);
        int mouseY = scaleMouseY(screenY);
        for (GuiComponent component : componentList) {
            if (component.isEnabled()) {
                component.clickDown(mouseX, mouseY, pointer, button);
            }
        }
        lastPointerPositionMap.put(pointer, new Point2i(mouseX, mouseY));
        pointerDown(mouseX, mouseY, pointer, button);
        return false;
    }

    /**
     * Called by {@link Gui#touchDown(int, int, int, int)} when the user clicked the screen, uses scaled mouse amounts
     *
     * @see Gui#scaleMouseX(int)
     * @see Gui#scaleMouseY(int)
     */
    protected void pointerDown(int mouseX, int mouseY, int pointer, int button) {

    }

    @Override
    public final boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (!allowInput) return false;
        int mouseX = scaleMouseX(screenX);
        int mouseY = scaleMouseY(screenY);
        for (GuiComponent component : componentList) {
            if (component.isEnabled()) {
                component.clickUp(mouseX, mouseY, pointer, button);
            }
        }
        pointerUp(mouseX, mouseY, pointer, button);
        return false;
    }

    /**
     * Called by {@link Gui#touchUp(int, int, int, int)} when the user released his pointer from the screen, uses scaled mouse amounts
     *
     * @see Gui#scaleMouseX(int)
     * @see Gui#scaleMouseY(int)
     */
    protected void pointerUp(int mouseX, int mouseY, int pointer, int button) {

    }

    @Override
    public final boolean touchDragged(int screenX, int screenY, int pointer) {
        if (!allowInput) return false;
        int mouseX = scaleMouseX(screenX);
        int mouseY = scaleMouseY(screenY);
        Point2i point2i = lastPointerPositionMap.get(pointer);
        for (GuiComponent component : componentList) {
            if (component.isEnabled()) {
                component.clickDragged(mouseX, mouseY, point2i.x, point2i.y, pointer);
            }
        }
        pointerDragged(mouseX, mouseY, point2i.x, point2i.y, pointer);
        lastPointerPositionMap.put(pointer, new Point2i(mouseX, mouseY));
        return false;
    }

    /**
     * Called by {@link Gui#touchDragged(int, int, int)} when the user moves his pointer over the screen while pressing it, used scaled mouse amounts
     *
     * @see Gui#scaleMouseX(int)
     * @see Gui#scaleMouseY(int)
     */
    protected void pointerDragged(int mouseX, int mouseY, int prevMouseX, int prevMouseY, int pointer) {

    }

    @Override
    public final boolean mouseMoved(int screenX, int screenY) {
        if (!allowInput) return false;
        int mouseX = scaleMouseX(screenX);
        int mouseY = scaleMouseY(screenY);
        Point2i point2i = lastPointerPositionMap.get(0);
        for (GuiComponent component : componentList) {
            if (component.isEnabled()) {
                component.mouseMoved(mouseX, mouseY, point2i.x, point2i.y);
            }
        }
        pointerMoved(mouseX, mouseY, point2i.x, point2i.y);
        lastPointerPositionMap.put(0, new Point2i(mouseX, mouseY));
        return false;
    }

    /**
     * Called by {@link Gui#mouseMoved(int, int)} when the user moves his pointer over the screen without pressing it, used scaled mouse amounts
     *
     * @see Gui#scaleMouseX(int)
     * @see Gui#scaleMouseY(int)
     */
    protected void pointerMoved(int mouseX, int mouseY, int prevMouseX, int prevMouseY) {

    }

    @Override
    public final boolean scrolled(int amount) {
        if (!allowInput) return false;
        int mouseX = scaleMouseX();
        int mouseY = scaleMouseY();
        for (GuiComponent component : componentList) {
            if (component.isEnabled()) {
                component.scroll(mouseX, mouseY, amount);
            }
        }
        scrolledWheel(mouseX, mouseY, amount);
        return false;
    }

    /**
     * Called by {@link Gui#scrolled(int)} when the user scrolls his mouse wheel, used scaled mouse amounts
     *
     * @see Gui#scaleMouseX(int)
     * @see Gui#scaleMouseY(int)
     */
    protected void scrolledWheel(int mouseX, int mouseY, int amount) {

    }

    protected final int scaleMouseX() {
        return scaleMouseX(input.getX());
    }

    protected final int scaleMouseY() {
        return scaleMouseY(input.getY());
    }

    protected final int scaleMouseX(int mouseX) {
        double scale = dg.getPreferredScreenWidth() / (double) Gdx.graphics.getWidth();
        return (int) (mouseX * scale) + offsetX;
    }

    protected final int scaleMouseY(int mouseY) {
        double scale = dg.getPreferredScreenHeight() / (double) Gdx.graphics.getHeight();
        return dg.getPreferredScreenHeight() - (int) ((mouseY * scale) + offsetY);
    }

    protected final int unscaleMouseX(int mouseX) {
        double scale = dg.getPreferredScreenWidth() / (double) Gdx.graphics.getWidth();
        return (int) (mouseX / scale) - offsetX;
    }

    protected final int unscaleMouseY(int mouseY) {
        double scale = dg.getPreferredScreenHeight() / (double) Gdx.graphics.getHeight();
        return (int) ((mouseY / scale) - offsetY);
    }

    protected final void registerComponent(GuiComponent guiComponent) {
        if (guiComponent instanceof GuiButton) {
            ((GuiButton) guiComponent).setCallback(this);
        }
        componentList.add(guiComponent);
    }

    /**
     * @return whether the given point hovers a gui component
     */
    protected boolean hoversComponent(int mouseX, int mouseY) {
        for (GuiComponent guiComponent : componentList) {
            if (guiComponent.isHovered(mouseX, mouseY)) return true;
        }
        return false;
    }

    public void exitGui() {
    }

    @Override
    public void buttonCallback(GuiButton button) {

    }
}
