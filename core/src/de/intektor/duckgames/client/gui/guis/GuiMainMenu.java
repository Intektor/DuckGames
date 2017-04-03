package de.intektor.duckgames.client.gui.guis;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import de.intektor.duckgames.client.editor.EditableGameMap;
import de.intektor.duckgames.client.gui.Gui;
import de.intektor.duckgames.client.gui.components.GuiButton;
import de.intektor.duckgames.client.gui.components.GuiTextBasedButton;
import de.intektor.duckgames.client.gui.guis.connect_to_lobby.GuiConnectToLobby;
import de.intektor.duckgames.client.gui.guis.lobby.GuiCreateLobby;
import de.intektor.duckgames.client.rendering.RenderUtils;

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
        int y = height / 2;
        joinLobbyButton = new GuiTextBasedButton(x, y, 1200, 200, "Join Lobby", true);
        y -= 200;
        createLobbyButton = new GuiTextBasedButton(x, y, 1200, 200, "Create Lobby", true);
        y -= 200;
        levelEditorButton = new GuiTextBasedButton(x, y, 1200, 200, "Enter Level Editor", true);

        registerComponent(joinLobbyButton);
        registerComponent(createLobbyButton);
        registerComponent(levelEditorButton);
    }

    @Override
    protected void renderGui(int mouseX, int mouseY, OrthographicCamera camera, float partialTicks) {
        super.renderGui(mouseX, mouseY, camera, partialTicks);
        spriteBatch.begin();
        RenderUtils.drawString("Awesome Game!", dg.defaultFont72, width / 2, height / 2 + height / 3, spriteBatch, Color.WHITE, true, true);
        spriteBatch.end();
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
