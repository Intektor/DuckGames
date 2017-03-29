package de.intektor.duckgames.client.gui.guis;

import com.badlogic.gdx.graphics.OrthographicCamera;
import de.intektor.duckgames.client.editor.EditableGameMap;
import de.intektor.duckgames.client.gui.Gui;
import de.intektor.duckgames.client.gui.components.GuiButton;
import de.intektor.duckgames.client.gui.components.GuiTextBasedButton;
import de.intektor.duckgames.client.gui.guis.lobby.GuiCreateLobby;

/**
 * @author Intektor
 */
public class GuiMainMenu extends Gui {

    private GuiTextBasedButton joinLobbyButton;
    private GuiTextBasedButton createLobbyButton;
    private GuiTextBasedButton levelEditorButton;

    @Override
    public void enterGui() {
        super.enterGui();
        int x = width / 2 - 600;
        int y = (int) ((height / 2) + 200 * 1.5f);
        joinLobbyButton = new GuiTextBasedButton(x, y, 1200, 200, "Join Lobby");
        y -= 200;
        createLobbyButton = new GuiTextBasedButton(x, y, 1200, 200, "Create Lobby");
        y -= 200;
        levelEditorButton = new GuiTextBasedButton(x, y, 1200, 200, "Enter Level Editor");

        registerComponent(joinLobbyButton);
        registerComponent(createLobbyButton);
        registerComponent(levelEditorButton);
    }

    @Override
    protected void renderGui(int mouseX, int mouseY, OrthographicCamera camera, float partialTicks) {
        super.renderGui(mouseX, mouseY, camera, partialTicks);
    }

    @Override
    protected void updateGui(int mouseX, int mouseY) {
        super.updateGui(mouseX, mouseY);
    }

    @Override
    public void exitGui() {
        super.exitGui();
    }

    @Override
    public void buttonCallback(GuiButton button) {
        if (button == joinLobbyButton) {
            dg.showGui(new GuiConnectToLobby());
        } else if (button == createLobbyButton) {
            dg.showGui(new GuiCreateLobby());
        } else if (button == levelEditorButton) {
            dg.showGui(new GuiLevelEditor(new EditableGameMap(80, 40)));
        }
    }
}
