package de.intektor.duckgames.client.gui.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import de.intektor.duckgames.client.rendering.FontUtils;
import de.intektor.duckgames.client.rendering.RenderUtils;
import de.intektor.duckgames.util.StringUtils;

import java.util.List;

/**
 * @author Intektor
 */
public class GuiInfoComponent extends GuiFrame {

    private GuiButton okButton;
    private String infoText;

    private Runnable okAction;

    public GuiInfoComponent(String headline, String info) {
        this(-1, -1, -1, -1, headline, info);
    }

    public GuiInfoComponent(int x, int y, int width, int height, String headline, String info) {
        super(x, y, width, height, headline);
        this.infoText = info;
        if (x == -1 && y == -1 && width == -1 && height == -1) {
            this.width = 1000;
            List<String> strings = StringUtils.splitConcated("\n", FontUtils.splitString(infoText, dg.defaultFont28, this.width));
            this.height = (int) Math.max(strings.size() * dg.defaultFont28.getLineHeight() + 80, 400);
            setPosition(dg.getPreferredScreenHeight() / 2 - this.width / 2, dg.getPreferredScreenHeight() / 2 - this.height / 2);
        }
        okButton = new GuiTextBasedButton(this.width / 2 - 100, 0, 200, 80, "OK!", true);

        registerGuiComponent(okButton);

        okAction = new DefaultOkAction();
    }

    @Override
    protected void drawBody(float drawX, float drawY, OrthographicCamera camera, ShapeRenderer sR, SpriteBatch sB, float partialTicks, int mouseX, int mouseY) {
        sR.begin();
        sR.set(ShapeRenderer.ShapeType.Filled);
        sR.setColor(new Color(0.3f, 0.3f, 0.3f, 1));
        sR.rect(drawX, drawY, width, height - topBarHeight);
        sR.end();

        sB.begin();
        List<String> strings = StringUtils.splitConcated("\n", FontUtils.splitString(infoText, dg.defaultFont28, width));
        float lineHeight = dg.defaultFont28.getLineHeight();
        float y = drawY + height / 2 + strings.size() / 2f * (lineHeight - 1);
        for (String string : strings) {
            RenderUtils.drawString(string, dg.defaultFont28, drawX + width / 2, y, sB, Color.WHITE, true);
            y += lineHeight;
        }
        sB.end();
    }

    @Override
    public void buttonCallback(GuiButton button) {
        if (button == okButton) {
            okAction.run();
        }
    }

    public void setOkAction(Runnable okAction) {
        this.okAction = okAction;
    }

    @Override
    public void setShown(boolean shown) {
        super.setShown(shown);
        if (!shown) {
            gui.removeGuiComponent(this);
        }
    }

    private class DefaultOkAction implements Runnable {

        @Override
        public void run() {
            GuiInfoComponent.this.setShown(false);
        }
    }
}
