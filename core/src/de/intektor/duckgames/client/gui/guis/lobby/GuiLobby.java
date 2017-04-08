package de.intektor.duckgames.client.gui.guis.lobby;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import de.intektor.duckgames.client.editor.EditableGameMap;
import de.intektor.duckgames.client.gui.Gui;
import de.intektor.duckgames.client.gui.components.GuiButton;
import de.intektor.duckgames.client.gui.components.GuiTextBasedButton;
import de.intektor.duckgames.client.gui.guis.GuiMainMenu;
import de.intektor.duckgames.client.rendering.FontUtils;
import de.intektor.duckgames.client.rendering.RenderUtils;
import de.intektor.duckgames.common.CommonCode;
import de.intektor.duckgames.common.DuckGamesServer;
import de.intektor.duckgames.common.HostingInfo;
import de.intektor.duckgames.common.chat.ChatMessage;
import de.intektor.duckgames.common.net.client_to_server.DisconnectPacketToServer;
import de.intektor.duckgames.common.net.client_to_server.LobbyChangeMapPacketToServer;
import de.intektor.duckgames.game.GameProfile;

import java.net.InetSocketAddress;

/**
 * @author Intektor
 */
public class GuiLobby extends Gui {

    private boolean isHost;
    private final HostingInfo hostingInfo;

    private GuiTextBasedButton startGameButton;
    private GuiTextBasedButton leaveLobbyButton;
    private GuiTextBasedButton selectWorldButton;
    private GuiLobbyChat chatComponent;

    private SelectWorldToPlayGuiComponent selectWorldToPlayGuiComponent;

    private EditableGameMap selectedMap;

    private String selectedMapName;

    private BitmapFont font = dg.defaultFont28;

    public GuiLobby(boolean isHost, HostingInfo hostingInfo) {
        this.isHost = isHost;
        this.hostingInfo = hostingInfo;
    }

    @Override
    public void enterGui() {
        super.enterGui();
        chatComponent = new GuiLobbyChat(width / 2 - 600, height / 2 - 400, 1200, 800);
        registerComponent(chatComponent);

        leaveLobbyButton = new GuiTextBasedButton(0, 0, 200, 80, "Leave Lobby!", true);
        registerComponent(leaveLobbyButton);

        if (isHost) {
            selectWorldToPlayGuiComponent = new SelectWorldToPlayGuiComponent(0, 0, 500, 600, this);
            selectWorldToPlayGuiComponent.setShown(false);

            selectWorldButton = new GuiTextBasedButton(width - 200, height - 80, 200, 80, "Select World", true);

            startGameButton = new GuiTextBasedButton(width - 200, 0, 200, 80, "Start Game!", true);


            registerComponent(selectWorldToPlayGuiComponent);
            registerComponent(selectWorldButton);
            registerComponent(startGameButton);

            DuckGamesServer dedicatedServer = new DuckGamesServer();
            dg.setDedicatedServer(dedicatedServer);
            dedicatedServer.startServer(DuckGamesServer.ServerState.LOBBY_STATE, hostingInfo);
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
            for (GameProfile profile : dg.getClientConnection().getPlayerProfiles().values()) {
                RenderUtils.drawString(profile.username, font, 0, y, spriteBatch, isHost ? Color.RED : Color.WHITE);
                y -= font.getLineHeight();
            }
            spriteBatch.end();
        }

        spriteBatch.begin();
        String s = "Selected Map:";
        RenderUtils.drawString(s, font, width - FontUtils.getStringWidth(s, font), height / 2, spriteBatch, Color.WHITE);
        String text;
        Color color;
        if (selectedMapName == null) {
            text = "No map selected!";
            color = Color.RED;
        } else {
            text = selectedMapName;
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
                dg.connectToServer(new InetSocketAddress("localhost", server.getPort()), hostingInfo.hostingType);
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
            selectWorldToPlayGuiComponent.setPosition(width / 2 - selectWorldToPlayGuiComponent.getWidth() / 2, height / 2 - selectWorldToPlayGuiComponent.getHeight() / 2);
        } else if (button == startGameButton) {
            if (getSelectedMap() != null) {
                DuckGamesServer dedicatedServer = dg.getDedicatedServer();
                dedicatedServer.getMainServerThread().launchGame(getSelectedMap(), DuckGamesServer.GameMode.COMPETITIVE_SOLO);
            }
        } else if (button == leaveLobbyButton) {
            DuckGamesServer server = CommonCode.getDuckGamesServer();
            if (server == null) {
                dg.sendPacketToServer(new DisconnectPacketToServer());
                dg.disconnect();
            } else {
                dg.disconnect();
                dg.setDedicatedServer(null);
            }
            dg.showGui(new GuiMainMenu());
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
        this.selectedMapName = selectedMap.getSaveName();
        if (isHost) dg.sendPacketToServer(new LobbyChangeMapPacketToServer(selectedMapName));
    }

    public void setSelectedMapName(String selectedMapName) {
        this.selectedMapName = selectedMapName;
    }
}
