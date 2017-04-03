package de.intektor.duckgames.client.gui.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import de.intektor.duckgames.client.gui.GuiComponent;
import de.intektor.duckgames.client.gui.GuiMultiComponent;
import de.intektor.duckgames.client.gui.util.GuiUtils;
import de.intektor.duckgames.client.rendering.RenderUtils;

/**
 * @author Intektor
 */
public abstract class GuiFrame extends GuiMultiComponent {

    protected int topBarHeight = 30;
    protected String headline;

    protected boolean isActive;
    protected boolean isClickedInTopBar;

    public GuiFrame(int x, int y, int width, int height, String headline) {
        super(x, y, width, height);
        this.headline = headline;
    }

    @Override
    protected final void renderComponent(float drawX, float drawY, int mouseX, int mouseY, OrthographicCamera camera, ShapeRenderer sR, SpriteBatch sB, float partialTicks) {
        sR.begin();
        sR.set(ShapeType.Filled);
        sR.setColor(Color.WHITE);
        sR.rect(drawX, drawY + height - topBarHeight, width, topBarHeight);
        sR.end();
        sB.begin();
        RenderUtils.drawString(headline, dg.defaultFont28, x, y + height - topBarHeight / 2, sB, Color.BLACK, false, true);
        sB.end();
        sR.begin();
        sR.setColor(Color.BLACK);
        sR.line(drawX, drawY + height - topBarHeight, x + width, y + height - topBarHeight);
        sR.end();
        drawBody(drawX, drawY, camera, sR, sB, partialTicks, mouseX, mouseY);
        super.renderComponent(drawX, drawY, mouseX, mouseY, camera, sR, sB, partialTicks);
        postDrawBody(drawX, drawY, camera, sR, sB, partialTicks, mouseX, mouseY);
    }

    protected abstract void drawBody(float drawX, float drawY, OrthographicCamera camera, ShapeRenderer sR, SpriteBatch sB, float partialTicks, int mouseX, int mouseY);

    protected void postDrawBody(float drawX, float drawY, OrthographicCamera camera, ShapeRenderer sR, SpriteBatch sB, float partialTicks, int mouseX, int mouseY) {

    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    @Override
    public void clickDown(int mouseX, int mouseY, int pointer, int button, float drawX, float drawY) {
        super.clickDown(mouseX, mouseY, pointer, button, drawX, drawY);
        if (isShown()) {
            checkClickedFrame(mouseX, mouseY);
            isClickedInTopBar = GuiUtils.isPointInRegion(x, y + height - topBarHeight, width, topBarHeight, mouseX, mouseY);
        }
    }

    @Override
    public void clickDragged(int mouseX, int mouseY, int prevX, int prevY, int pointer) {
        super.clickDragged(mouseX, mouseY, prevX, prevY, pointer);
        if (isClickedInTopBar) {
            int dX = mouseX - prevX;
            int dY = mouseY - prevY;
            moveComponent(dX, dY);
            checkFrameOutOfWindow();
        }
    }

    protected boolean checkClickedFrame(int mouseX, int mouseY) {
        boolean hoveredByComponent = false;
        for (GuiComponent guiComponent : componentList) {
            if (guiComponent.isShown() && guiComponent.isHovered(localMouseX(mouseX), localMouseY(mouseY))) {
                hoveredByComponent = true;
                break;
            }
        }
        isActive = GuiUtils.isPointInRegion(x, y, width, height, mouseX, mouseY) || hoveredByComponent;
        if (!isActive) onNotClickedOnFrame();
        return isActive;
    }

    private void checkFrameOutOfWindow() {
        if (x < 0) setPosition(x = 0, y);
        if (x + width > dg.getPreferredScreenWidth()) setPosition(dg.getPreferredScreenWidth() - width, y);
        if (y + height > dg.getPreferredScreenHeight()) setPosition(x, dg.getPreferredScreenHeight() - height);
        if (y + height - topBarHeight < 0) setPosition(x, 0 - height + topBarHeight);
    }

    public boolean isActive() {
        return isActive;
    }

    protected void onNotClickedOnFrame() {
        setShown(false);
    }
}
