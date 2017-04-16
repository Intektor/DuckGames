package de.intektor.duckgames.client.gui.guis;

import com.badlogic.gdx.graphics.OrthographicCamera;
import de.intektor.duckgames.client.gui.Gui;

/**
 * @author Intektor
 */
public class GuiEmpty extends Gui {

    @Override
    protected void renderGui(int mouseX, int mouseY, OrthographicCamera camera, float partialTicks) {
        super.renderGui(mouseX, mouseY, camera, partialTicks);
    }
}
