package de.intektor.duckgames.client.gui;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import de.intektor.duckgames.client.gui.components.GuiButton;
import de.intektor.duckgames.client.gui.util.GuiUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * A GuiComponent, which can handle internal GuiComponents
 *
 * @author Intektor
 */
public class GuiMultiComponent extends GuiComponent implements GuiButton.GuiButtonCallback {

    protected List<GuiComponent> componentList = new ArrayList<GuiComponent>();
    private List<GuiComponent> componentsToBeAdded = new ArrayList<GuiComponent>();
    private List<GuiComponent> componentsToBeRemoved = new ArrayList<GuiComponent>();

    public GuiMultiComponent(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    protected void renderComponent(float drawX, float drawY, int mouseX, int mouseY, OrthographicCamera camera, ShapeRenderer sR, SpriteBatch sB, float partialTicks) {
        super.renderComponent(drawX, drawY, mouseX, mouseY, camera, sR, sB, partialTicks);
        renderSubComponents(drawX, drawY, mouseX, mouseY, camera, sR, sB, partialTicks);
    }

    protected void renderSubComponents(float drawX, float drawY, int mouseX, int mouseY, OrthographicCamera camera, ShapeRenderer sR, SpriteBatch sB, float partialTicks) {
        int lMouseX = localMouseX(mouseX);
        int lMouseY = localMouseY(mouseY);
        for (GuiComponent guiComponent : componentList) {
            if (guiComponent.isShown()) {
                guiComponent.renderComponent(drawX + guiComponent.x, drawY + guiComponent.y, lMouseX, lMouseY, camera, sR, sB, partialTicks);
            }
        }
    }

    @Override
    protected void updateComponent(int mouseX, int mouseY, float drawX, float drawY) {
        super.updateComponent(mouseX, mouseY, drawX, drawY);
        componentList.removeAll(componentsToBeRemoved);
        componentsToBeRemoved.clear();
        componentList.addAll(componentsToBeAdded);
        componentsToBeAdded.clear();
        int lMouseX = localMouseX(mouseX);
        int lMouseY = localMouseY(mouseY);
        for (GuiComponent guiComponent : componentList) {
            guiComponent.updateComponent(lMouseX, lMouseY, drawX + guiComponent.x, drawY + guiComponent.y);
        }
    }

    @Override
    public void clickDown(int mouseX, int mouseY, int pointer, int button, float drawX, float drawY) {
        super.clickDown(mouseX, mouseY, pointer, button, drawX, drawY);
        int lMouseX = localMouseX(mouseX);
        int lMouseY = localMouseY(mouseY);
        for (GuiComponent guiComponent : componentList) {
            guiComponent.clickDown(lMouseX, lMouseY, pointer, button, drawX + guiComponent.x, drawY + guiComponent.y);
        }
    }

    @Override
    public void clicked(int mouseX, int mouseY, int pointer) {
        super.clicked(mouseX, mouseY, pointer);
        int lMouseX = localMouseX(mouseX);
        int lMouseY = localMouseY(mouseY);
        for (GuiComponent guiComponent : componentList) {
            guiComponent.clicked(lMouseX, lMouseY, pointer);
        }
    }

    @Override
    public void clickUp(int mouseX, int mouseY, int pointer, int button, float drawX, float drawY) {
        super.clickUp(mouseX, mouseY, pointer, button, drawX, drawY);
        int lMouseX = localMouseX(mouseX);
        int lMouseY = localMouseY(mouseY);
        for (GuiComponent guiComponent : componentList) {
            guiComponent.clickUp(lMouseX, lMouseY, pointer, button, drawX + guiComponent.x, drawY + guiComponent.y);
        }
    }

    @Override
    public void keyDown(int mouseX, int mouseY, int keyCode) {
        super.keyDown(mouseX, mouseY, keyCode);
        int lMouseX = localMouseX(mouseX);
        int lMouseY = localMouseY(mouseY);
        for (GuiComponent guiComponent : componentList) {
            guiComponent.keyDown(lMouseX, lMouseY, keyCode);
        }
    }

    @Override
    public void keyPressed(int mouseX, int mouseY, int keyCode) {
        super.keyPressed(mouseX, mouseY, keyCode);
        int lMouseX = localMouseX(mouseX);
        int lMouseY = localMouseY(mouseY);
        for (GuiComponent guiComponent : componentList) {
            guiComponent.keyPressed(lMouseX, lMouseY, keyCode);
        }
    }

    @Override
    public void keyReleased(int mouseX, int mouseY, int keyCode) {
        super.keyReleased(mouseX, mouseY, keyCode);
        int lMouseX = localMouseX(mouseX);
        int lMouseY = localMouseY(mouseY);
        for (GuiComponent guiComponent : componentList) {
            guiComponent.keyReleased(lMouseX, lMouseY, keyCode);
        }
    }

    @Override
    public void charTyped(int mouseX, int mouseY, char character) {
        super.charTyped(mouseX, mouseY, character);
        int lMouseX = localMouseX(mouseX);
        int lMouseY = localMouseY(mouseY);
        for (GuiComponent guiComponent : componentList) {
            guiComponent.charTyped(lMouseX, lMouseY, character);
        }
    }

    @Override
    public void mouseMoved(int mouseX, int mouseY, int prevX, int prevY) {
        super.mouseMoved(mouseX, mouseY, prevX, prevY);
        int lMouseX = localMouseX(mouseX);
        int lMouseY = localMouseY(mouseY);
        int prevlmouseX = localMouseX(prevX);
        int prevlmouseY = localMouseY(prevY);
        for (GuiComponent guiComponent : componentList) {
            guiComponent.mouseMoved(lMouseX, lMouseY, prevlmouseX, prevlmouseY);
        }
    }

    @Override
    public void clickDragged(int mouseX, int mouseY, int prevX, int prevY, int pointer) {
        super.clickDragged(mouseX, mouseY, prevX, prevY, pointer);
        int lMouseX = localMouseX(mouseX);
        int lMouseY = localMouseY(mouseY);
        int prevlmouseX = localMouseX(prevX);
        int prevlmouseY = localMouseY(prevY);
        for (GuiComponent guiComponent : componentList) {
            guiComponent.clickDragged(lMouseX, lMouseY, prevlmouseX, prevlmouseY, pointer);
        }
    }

    @Override
    public void scroll(int mouseX, int mouseY, int scrollAmount) {
        super.scroll(mouseX, mouseY, scrollAmount);
        int lMouseX = localMouseX(mouseX);
        int lMouseY = localMouseY(mouseY);
        for (GuiComponent guiComponent : componentList) {
            guiComponent.scroll(lMouseX, lMouseY, scrollAmount);
        }
    }

    @Override
    public boolean isHovered(int mouseX, int mouseY) {
        int lMouseX = localMouseX(mouseX);
        int lMouseY = localMouseY(mouseY);
        for (GuiComponent guiComponent : componentList) {
            if (guiComponent.isHovered(lMouseX, lMouseY)) return true;
        }
        return GuiUtils.isPointInRegion(x, y, width, height, mouseX, mouseY);
    }

    /**
     * Registers a Gui Component to this GuiMultiComponent, moves the GuiComponent to register relative to this components position
     */
    protected void registerGuiComponent(GuiComponent guiComponent) {
        if (guiComponent instanceof GuiButton) {
            ((GuiButton) guiComponent).setCallback(this);
        }
        componentsToBeAdded.add(guiComponent);
    }

    protected void removeGuiComponent(GuiComponent guiComponent) {
        componentsToBeRemoved.add(guiComponent);
    }

    @Override
    public void moveComponent(int amountX, int amountY) {
        super.moveComponent(amountX, amountY);
    }

    @Override
    public void setPosition(int x, int y) {
        moveComponent(x - this.x, y - this.y);
    }

    @Override
    public void buttonCallback(GuiButton button) {

    }

    protected int localMouseX(int mouseX) {
        return mouseX - x;
    }

    protected int localMouseY(int mouseY) {
        return mouseY - y;
    }
}
