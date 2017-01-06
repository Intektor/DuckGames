package de.intektor.duckgames.client.gui.guis;

import com.badlogic.gdx.graphics.OrthographicCamera;
import de.intektor.duckgames.client.gui.Gui;

/**
 * @author Intektor
 */
public class GuiDownloadingMap extends Gui {

    private DownloadContext context;

    public GuiDownloadingMap() {

    }

    @Override
    protected void renderGui(int mouseX, int mouseY, OrthographicCamera camera, float partialTicks) {
        super.renderGui(mouseX, mouseY, camera, partialTicks);

    }

    @Override
    protected void updateGui(int mouseX, int mouseY) {
        super.updateGui(mouseX, mouseY);

    }

    public enum DownloadContext {
        DOWNLOADING_MAP
    }
}
