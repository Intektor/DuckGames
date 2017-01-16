package de.intektor.duckgames.client.gui.guis.lobby;

import com.badlogic.gdx.graphics.OrthographicCamera;
import de.intektor.duckgames.client.gui.Gui;
import de.intektor.duckgames.client.gui.components.GuiButton;
import de.intektor.duckgames.client.gui.components.GuiTextBasedButton;
import de.intektor.duckgames.common.chat.ChatMessage;

/**
 * @author Intektor
 */
public class GuiLobby extends Gui {

    private boolean isHost;

    private GuiTextBasedButton startGameButton;
    private GuiTextBasedButton leaveLobbyButton;
    private GuiLobbyChat chatComponent;

    @Override
    public void enterGui() {
        super.enterGui();
        chatComponent = new GuiLobbyChat(width / 2 - 600, height / 2 - 400, 1200, 800);
        registerComponent(chatComponent);
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
    public void keyPushed(int keyCode, int mouseX, int mouseY) {
        super.keyPushed(keyCode, mouseX, mouseY);
    }

    @Override
    public void keyReleased(int keyCode, int mouseX, int mouseY) {
        super.keyReleased(keyCode, mouseX, mouseY);
    }

    @Override
    public void charTyped(char character, int mouseX, int mouseY) {
        super.charTyped(character, mouseX, mouseY);
    }

    @Override
    public void exitGui() {
        super.exitGui();
    }

    @Override
    public void buttonCallback(GuiButton button) {
        super.buttonCallback(button);
    }

    public void addMessage(ChatMessage message) {
        chatComponent.addMessage(message);
    }
}
