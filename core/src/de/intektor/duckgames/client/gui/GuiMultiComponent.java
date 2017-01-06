package de.intektor.duckgames.client.gui;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import de.intektor.duckgames.client.gui.components.GuiButton;
import de.intektor.duckgames.client.gui.util.GuiUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * A GuiComponent, which can handle intern GuiComponents
 *
 * @author Intektor
 */
public class GuiMultiComponent extends GuiComponent implements GuiButton.GuiButtonCallback {

    private List<GuiComponent> componentList = new ArrayList<GuiComponent>();

    public GuiMultiComponent(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    protected void renderComponent(int mouseX, int mouseY, OrthographicCamera camera, ShapeRenderer sR, SpriteBatch sB, float partialTicks) {
        super.renderComponent(mouseX, mouseY, camera, sR, sB, partialTicks);
        for (GuiComponent guiComponent : componentList) {
            guiComponent.renderComponent(mouseX, mouseY, camera, sR, sB, partialTicks);
        }
    }

    @Override
    protected void updateComponent(int mouseX, int mouseY) {
        super.updateComponent(mouseX, mouseY);
        for (GuiComponent guiComponent : componentList) {
            guiComponent.updateComponent(mouseX, mouseY);
        }
    }

    @Override
    public void clickDown(int mouseX, int mouseY, int pointer, int button) {
        super.clickDown(mouseX, mouseY, pointer, button);
        for (GuiComponent guiComponent : componentList) {
            guiComponent.clickDown(mouseX, mouseY, pointer, button);
        }
    }

    @Override
    public void clicked(int mouseX, int mouseY, int pointer) {
        super.clicked(mouseX, mouseY, pointer);
        for (GuiComponent guiComponent : componentList) {
            guiComponent.clicked(mouseX, mouseY, pointer);
        }
    }

    @Override
    public void clickUp(int mouseX, int mouseY, int pointer, int button) {
        super.clickUp(mouseX, mouseY, pointer, button);
        for (GuiComponent guiComponent : componentList) {
            guiComponent.clickUp(mouseX, mouseY, pointer, button);
        }
    }

    @Override
    public void keyDown(int mouseX, int mouseY, int keyCode) {
        super.keyDown(mouseX, mouseY, keyCode);
        for (GuiComponent guiComponent : componentList) {
            guiComponent.keyDown(mouseX, mouseY, keyCode);
        }
    }

    @Override
    public void keyPressed(int mouseX, int mouseY, int keyCode) {
        super.keyPressed(mouseX, mouseY, keyCode);
        for (GuiComponent guiComponent : componentList) {
            guiComponent.keyPressed(mouseX, mouseY, keyCode);
        }
    }

    @Override
    public void keyReleased(int mouseX, int mouseY, int keyCode) {
        super.keyReleased(mouseX, mouseY, keyCode);
        for (GuiComponent guiComponent : componentList) {
            guiComponent.keyReleased(mouseX, mouseY, keyCode);
        }
    }

    @Override
    public void charTyped(int mouseX, int mouseY, char character) {
        super.charTyped(mouseX, mouseY, character);
        for (GuiComponent guiComponent : componentList) {
            guiComponent.charTyped(mouseX, mouseY, character);
        }
    }

    @Override
    public void mouseMoved(int mouseX, int mouseY, int prevX, int prevY) {
        super.mouseMoved(mouseX, mouseY, prevX, prevY);
        for (GuiComponent guiComponent : componentList) {
            guiComponent.mouseMoved(mouseX, mouseY, prevX, prevY);
        }
    }

    @Override
    public void clickDragged(int mouseX, int mouseY, int prevX, int prevY, int pointer) {
        super.clickDragged(mouseX, mouseY, prevX, prevY, pointer);
        for (GuiComponent guiComponent : componentList) {
            guiComponent.clickDragged(mouseX, mouseY, prevX, prevY, pointer);
        }
    }

    @Override
    public void scroll(int mouseX, int mouseY, int scrollAmount) {
        super.scroll(mouseX, mouseY, scrollAmount);
        for (GuiComponent guiComponent : componentList) {
            guiComponent.scroll(mouseX, mouseY, scrollAmount);
        }
    }

    @Override
    public boolean isHovered(int mouseX, int mouseY) {
        for (GuiComponent guiComponent : componentList) {
            if (guiComponent.isHovered(mouseX, mouseY)) return true;
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
        guiComponent.moveComponent(x, y);
        componentList.add(guiComponent);
    }

    @Override
    public void moveComponent(int amountX, int amountY) {
        super.moveComponent(amountX, amountY);
        for (GuiComponent guiComponent : componentList) {
            guiComponent.moveComponent(amountX, amountY);
        }
    }

    @Override
    public void buttonCallback(GuiButton button) {

    }
}
