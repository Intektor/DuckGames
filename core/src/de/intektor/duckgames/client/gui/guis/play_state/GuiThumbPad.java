package de.intektor.duckgames.client.gui.guis.play_state;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import de.intektor.duckgames.client.gui.GuiComponent;

import javax.vecmath.Point2f;

/**
 * @author Intektor
 */
public class GuiThumbPad extends GuiComponent {

    private boolean currentlyUsed;
    private int sX, sY;
    private int radius;
    private int thumbPadX, thumbPadY;
    private int currentPointer;

    public GuiThumbPad(int x, int y, int radius) {
        super(x - radius, y - radius, radius, radius);
        this.sX = x;
        this.sY = y;
        this.radius = radius;
        thumbPadX = x;
        thumbPadY = y;
    }

    @Override
    protected void renderComponent(float drawX, float drawY, int mouseX, int mouseY, OrthographicCamera camera, ShapeRenderer sR, SpriteBatch sB, float partialTicks) {
        super.renderComponent(drawX, drawY, mouseX, mouseY, camera, sR, sB, partialTicks);
        sR.begin(ShapeRenderer.ShapeType.Line);
        sR.circle(x + radius, y + radius, radius);
        sR.set(ShapeRenderer.ShapeType.Filled);
        sR.circle(thumbPadX, thumbPadY, radius / 2);
        sR.end();
    }

    @Override
    public void clickDown(int mouseX, int mouseY, int pointer, int button, float drawX, float drawY) {
        super.clickDown(mouseX, mouseY, pointer, button, drawX, drawY);
        if (new Point2f(sX, sY).distance(new Point2f(mouseX, mouseY)) <= radius) {
            currentlyUsed = true;
            setPosition(mouseX, mouseY);
            thumbPadX = mouseX;
            thumbPadY = mouseY;
            currentPointer = pointer;
        }
    }

    @Override
    public void clickDragged(int mouseX, int mouseY, int prevX, int prevY, int pointer) {
        super.clickDragged(mouseX, mouseY, prevX, prevY, pointer);
        if (pointer == currentPointer) {
            int midX = x + radius;
            int midY = y + radius;
            if (currentlyUsed) {
                if (new Point2f(mouseX, mouseY).distance(new Point2f(midX, midY)) <= radius) {
                    thumbPadX = mouseX;
                    thumbPadY = mouseY;
                } else {
                    double angle = Math.atan2(mouseY - midY, mouseX - midX);
                    thumbPadX = (int) (midX + Math.cos(angle) * radius);
                    thumbPadY = (int) (midY + Math.sin(angle) * radius);
                }
            }
        }
    }

    @Override
    public void clickUp(int mouseX, int mouseY, int pointer, int button, float drawX, float drawY) {
        super.clickUp(mouseX, mouseY, pointer, button, drawX, drawY);
        if (button == currentPointer) {
            setPosition(sX, sY);
            thumbPadX = x + radius;
            thumbPadY = y + radius;
            currentlyUsed = false;
        }
    }

    @Override
    public void setPosition(int x, int y) {
        super.setPosition(x - radius, y - radius);
    }

    public boolean isCurrentlyUsed() {
        return currentlyUsed;
    }

    public double getAngle() {
        return Math.atan2(thumbPadY - (y + radius), thumbPadX - (x + radius));
    }

    public double getPercentage() {
        return (new Point2f(thumbPadX, thumbPadY).distance(new Point2f(x + radius, y + radius))) / radius;
    }
}
