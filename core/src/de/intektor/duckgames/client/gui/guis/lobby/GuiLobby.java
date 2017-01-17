package de.intektor.duckgames.client.gui.guis.lobby;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import de.intektor.duckgames.client.editor.EditableGameMap;
import de.intektor.duckgames.client.gui.Gui;
import de.intektor.duckgames.client.gui.components.GuiButton;
import de.intektor.duckgames.client.gui.components.GuiTextBasedButton;
import de.intektor.duckgames.client.rendering.FontUtils;
import de.intektor.duckgames.client.rendering.RenderUtils;
import de.intektor.duckgames.common.DuckGamesServer;
import de.intektor.duckgames.common.PlayerProfile;
import de.intektor.duckgames.common.chat.ChatMessage;

/**
 * @author Intektor
 */
public class GuiLobby extends Gui {

    private boolean isHost;

    private GuiTextBasedButton startGameButton;
    private GuiTextBasedButton leaveLobbyButton;
    private GuiTextBasedButton selectWorldButton;
    private GuiLobbyChat chatComponent;

    private SelectWorldToPlayGuiComponent selectWorldToPlayGuiComponent;

    private EditableGameMap selectedMap;

    private BitmapFont font = dg.defaultFont28;

    public GuiLobby(boolean isHost) {
        this.isHost = isHost;
    }

    @Override
    public void enterGui() {
        super.enterGui();
        chatComponent = new GuiLobbyChat(width / 2 - 600, height / 2 - 400, 1200, 800);
        registerComponent(chatComponent);
        if (isHost) {
            selectWorldToPlayGuiComponent = new SelectWorldToPlayGuiComponent(0, 0, 500, 600, this);
            selectWorldToPlayGuiComponent.setShown(false);

            selectWorldButton = new GuiTextBasedButton(width - 100, (int) (height - font.getLineHeight()), 100, (int) font.getLineHeight(), "Select World");

            startGameButton = new GuiTextBasedButton(width - 200, 0, 200, (int) font.getLineHeight(), "Start Game!");

            registerComponent(selectWorldToPlayGuiComponent);
            registerComponent(selectWorldButton);
            registerComponent(startGameButton);

            DuckGamesServer dedicatedServer = new DuckGamesServer();
            dg.setDedicatedServer(dedicatedServer);
            dedicatedServer.startServer(DuckGamesServer.ServerState.LOBBY_STATE);
        }
    }

    @Override
    protected void renderGui(int mouseX, int mouseY, OrthographicCamera camera, float partialTicks) {
        super.renderGui(mouseX, mouseY, camera, partialTicks);
        if (isHost) {
            DuckGamesServer server = dg.getDedicatedServer();
            if (!server.isServerReadyForConnections()) {
                shapeRenderer.begin();
                shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
                shapeRenderer.setColor(Color.GREEN);
                shapeRenderer.end();
            } else if (dg.getClientConnection() != null && !dg.getClientConnection().isIdentificationSuccessful()) {
                shapeRenderer.begin();
                shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
                shapeRenderer.setColor(Color.ORANGE);
                shapeRenderer.end();
            }
        }
        if (dg.getClientConnection() != null) {
            spriteBatch.begin();
            RenderUtils.drawString("Connected Players:", font, 0, dg.getPreferredScreenHeight(), spriteBatch, Color.WHITE);
            float y = dg.getPreferredScreenHeight() - font.getLineHeight() * 1;
            for (PlayerProfile profile : dg.getClientConnection().getPlayerProfiles().values()) {
                RenderUtils.drawString(profile.username, font, 0, y, spriteBatch, Color.WHITE);
                y -= font.getLineHeight();
            }
            spriteBatch.end();
        }

        spriteBatch.begin();
        String s = "Selected Map:";
        RenderUtils.drawString(s, font, width - FontUtils.getStringWidth(s, font), height / 2, spriteBatch, Color.WHITE);
        String text;
        Color color;
        if (getSelectedMap() == null) {
            text = "No map selected!";
            color = Color.RED;
        } else {
            text = getSelectedMap().getSaveName();
            color = Color.GREEN;
        }
        RenderUtils.drawString(text, font, width - FontUtils.getStringWidth(text, font), height / 2 - FontUtils.getStringHeight(text, font), spriteBatch, color);
        spriteBatch.end();
    }

    @Override
    protected void updateGui(int mouseX, int mouseY) {
        super.updateGui(mouseX, mouseY);
        if (isHost) {
            DuckGamesServer server = dg.getDedicatedServer();
            if (server.isServerReadyForConnections() && dg.getClientConnection() == null) {
                dg.connectToServer("localhost");
            }
        }
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
        if (button == selectWorldButton) {
            selectWorldToPlayGuiComponent.setShown(true);
        } else if (button == startGameButton) {
            if (getSelectedMap() != null) {
                DuckGamesServer dedicatedServer = dg.getDedicatedServer();
                dedicatedServer.getMainServerThread().launchGame(getSelectedMap());
            }
        }
    }

    public void addMessage(ChatMessage message) {
        chatComponent.addMessage(message);
    }

    public EditableGameMap getSelectedMap() {
        return selectedMap;
    }

    public void setSelectedMap(EditableGameMap selectedMap) {
        this.selectedMap = selectedMap;
    }
}
