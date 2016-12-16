package de.intektor.duckgames.client.gui.guis;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import de.intektor.duckgames.client.gui.Gui;
import de.intektor.duckgames.client.gui.components.GuiButton;
import de.intektor.duckgames.client.renderer.WorldRenderer;
import de.intektor.duckgames.entity.EntityPlayer;
import de.intektor.duckgames.world.World;

/**
 * @author Intektor
 */
public class GuiPlayState extends Gui {

    private WorldRenderer worldRenderer = new WorldRenderer();

    private OrthographicCamera worldCamera;
    private ShapeRenderer worldShapeRenderer;
    private SpriteBatch worldSpriteBatch;

    public GuiPlayState() {
        World world = dg.theWorld;
        worldCamera = new OrthographicCamera(world.getWidth(), world.getHeight());
        worldCamera.position.set(world.getWidth() / 2, world.getHeight() / 2, 0);
        worldShapeRenderer = new ShapeRenderer();
        worldShapeRenderer.setAutoShapeType(true);
        worldSpriteBatch = new SpriteBatch();
    }

    @Override
    protected void renderGui(int mouseX, int mouseY, OrthographicCamera camera) {
        EntityPlayer player = dg.thePlayer;
        World world = dg.theWorld;
        worldCamera.update();
        worldShapeRenderer.setProjectionMatrix(worldCamera.combined);
        worldSpriteBatch.setProjectionMatrix(worldCamera.combined);
        worldRenderer.renderWorld(world, worldCamera, worldShapeRenderer, worldSpriteBatch, player);
        super.renderGui(mouseX, mouseY, camera);
    }

    @Override
    protected void updateGui(int mouseX, int mouseY) {
        EntityPlayer player = dg.thePlayer;
        World world = dg.theWorld;

        super.updateGui(mouseX, mouseY);
    }

    @Override
    public void keyPushed(int keyCode, int mouseX, int mouseY) {

    }

    @Override
    public void keyReleased(int keyCode, int mouseX, int mouseY) {
    }

    @Override
    public void charTyped(char character, int mouseX, int mouseY) {
        super.charTyped(character, mouseX, mouseY);
    }

    @Override
    protected void pointerDown(int mouseX, int mouseY, int pointer, int button) {
        super.pointerDown(mouseX, mouseY, pointer, button);
    }

    @Override
    protected void pointerUp(int mouseX, int mouseY, int pointer, int button) {
        super.pointerUp(mouseX, mouseY, pointer, button);
    }

    @Override
    protected void pointerDragged(int mouseX, int mouseY, int prevMouseX, int prevMouseY, int pointer) {
        super.pointerDragged(mouseX, mouseY, prevMouseX, prevMouseY, pointer);
    }

    @Override
    protected void pointerMoved(int mouseX, int mouseY, int prevMouseX, int prevMouseY) {
        super.pointerMoved(mouseX, mouseY, prevMouseX, prevMouseY);
    }

    @Override
    protected void scrolledWheel(int mouseX, int mouseY, int amount) {
        super.scrolledWheel(mouseX, mouseY, amount);
    }

    @Override
    public void exitGui() {

    }

    @Override
    public void buttonCallback(GuiButton button) {

    }
}
