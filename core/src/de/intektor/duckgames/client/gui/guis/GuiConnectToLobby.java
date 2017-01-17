package de.intektor.duckgames.client.gui.guis;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import de.intektor.duckgames.client.gui.Gui;
import de.intektor.duckgames.client.gui.components.GuiButton;
import de.intektor.duckgames.client.gui.components.GuiTextBasedButton;
import de.intektor.duckgames.client.gui.components.GuiTextField;
import de.intektor.duckgames.client.gui.guis.lobby.GuiLobby;
import de.intektor.duckgames.client.net.DuckGamesClientConnection;
import de.intektor.duckgames.client.rendering.FontUtils;
import de.intektor.duckgames.client.rendering.RenderUtils;

/**
 * @author Intektor
 */
public class GuiConnectToLobby extends Gui {

    private GuiTextBasedButton buttonJoinLobby;
    private GuiTextField enterIPTextField;

    private boolean tryingConnection;
    private boolean connectionFailed;
    private String errorMessage;

    @Override
    public void enterGui() {
        super.enterGui();
        BitmapFont font = dg.defaultFont28;
        enterIPTextField = new GuiTextField(width / 2 - 300, (int) (height / 2 - font.getLineHeight() / 2), 600, (int) font.getLineHeight(), "Enter Address here!");
        buttonJoinLobby = new GuiTextBasedButton(width / 2 + 300, (int) (height / 2 - font.getLineHeight() / 2), 100, (int) font.getLineHeight(), "Connect!");
        registerComponent(enterIPTextField);
        registerComponent(buttonJoinLobby);
    }

    @Override
    protected void renderGui(int mouseX, int mouseY, OrthographicCamera camera, float partialTicks) {
        super.renderGui(mouseX, mouseY, camera, partialTicks);
        if (tryingConnection) {
            spriteBatch.begin();
            BitmapFont font = dg.defaultFont28;
            String text = !connectionFailed ? "Connecting!" : errorMessage;
            RenderUtils.drawString(text, font, enterIPTextField.getX(), enterIPTextField.getY() - font.getLineHeight() + FontUtils.getStringHeight(text, font), spriteBatch, Color.WHITE);
            spriteBatch.end();
        }
    }

    @Override
    protected void updateGui(int mouseX, int mouseY) {
        super.updateGui(mouseX, mouseY);
        DuckGamesClientConnection clientConnection = dg.getClientConnection();
        if (tryingConnection && clientConnection != null) {
            if (clientConnection.connectionFailed()) {
                connectionFailed = true;
                errorMessage = clientConnection.getConnectionFailedProblem().getMessage();
                dg.disconnect();
                buttonJoinLobby.setShown(true);
            } else if (clientConnection.isConnected()) {
                tryingConnection = false;
                dg.showGui(new GuiLobby(false));
            }
        }
    }

    @Override
    public void exitGui() {
        super.exitGui();
    }

    @Override
    public void buttonCallback(GuiButton button) {
        if (button == buttonJoinLobby) {
            dg.connectToServer(enterIPTextField.getText());
            tryingConnection = true;
            buttonJoinLobby.setShown(false);
        }
    }
}
