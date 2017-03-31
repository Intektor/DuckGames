package de.intektor.duckgames.client.gui.guis;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.google.common.base.Splitter;
import de.intektor.duckgames.client.gui.Gui;
import de.intektor.duckgames.client.gui.components.GuiButton;
import de.intektor.duckgames.client.gui.components.GuiTextBasedButton;
import de.intektor.duckgames.client.gui.components.GuiTextField;
import de.intektor.duckgames.client.gui.guis.lobby.GuiLobby;
import de.intektor.duckgames.client.net.DuckGamesClientConnection;
import de.intektor.duckgames.client.rendering.FontUtils;
import de.intektor.duckgames.client.rendering.RenderUtils;
import de.intektor.duckgames.common.net.lan.ThreadFindLanServers;
import de.intektor.duckgames.util.charlist.CharList;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;

/**
 * @author Intektor
 */
public class GuiConnectToLobby extends Gui {

    private GuiTextBasedButton buttonJoinLobby;
    private GuiTextField enterIPTextField;

    private boolean tryingConnection;
    private boolean connectionFailed;
    private String errorMessage;

    private final Splitter splitterDoublePoint = Splitter.on(':').trimResults();

    private ThreadFindLanServers lanServerFinder;

    @Override
    public void enterGui() {
        super.enterGui();
        BitmapFont font = dg.defaultFont28;
        enterIPTextField = new GuiTextField(width / 2 - 300, (int) (height / 2 - font.getLineHeight() / 2), 600, (int) font.getLineHeight(), "Enter Address here!", CharList.combine(CharList.DIGITS, CharList.create(':')));
        buttonJoinLobby = new GuiTextBasedButton(width / 2 + 300, (int) (height / 2 - font.getLineHeight() / 2), 100, (int) font.getLineHeight(), "Connect!");
        try {
            lanServerFinder = new ThreadFindLanServers();
            lanServerFinder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
                dg.showGui(new GuiLobby(false, null));
            }
        }
    }

    @Override
    public void exitGui() {
        if (lanServerFinder != null) {
            lanServerFinder.interrupt();
        }
        super.exitGui();
    }

    @Override
    public void buttonCallback(GuiButton button) {
        if (button == buttonJoinLobby) {
            List<String> strings = splitterDoublePoint.splitToList(enterIPTextField.getText());
            InetSocketAddress address = new InetSocketAddress("localhost", 0);
            if (strings.size() == 1) {
                address = new InetSocketAddress(strings.get(0), 19473);
            } else if (strings.size() == 2) {
                try {
                    address = new InetSocketAddress(strings.get(0), Integer.parseInt(strings.get(1)));
                } catch (Exception ignored) {
                }
            }
            dg.connectToServer(address);
            tryingConnection = true;
            buttonJoinLobby.setShown(false);
        }
    }
}
