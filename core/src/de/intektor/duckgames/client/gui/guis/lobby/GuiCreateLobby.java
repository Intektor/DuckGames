package de.intektor.duckgames.client.gui.guis.lobby;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import de.intektor.duckgames.client.gui.Gui;
import de.intektor.duckgames.client.gui.components.GuiButton;
import de.intektor.duckgames.client.gui.components.GuiTextBasedButton;
import de.intektor.duckgames.client.gui.components.GuiTextField;
import de.intektor.duckgames.client.rendering.RenderUtils;
import de.intektor.duckgames.common.DuckGamesServer;
import de.intektor.duckgames.common.HostingInfo;
import de.intektor.duckgames.util.charlist.CharList;

/**
 * @author Intektor
 */
public class GuiCreateLobby extends Gui {

    private GuiButton launchGuiButton;
    private GuiButton switchHostTypeButton;
    private GuiButton switchDynamicPortButton;
    private GuiTextField specificPortTextField;

    private DuckGamesServer.HostingType hostingType = DuckGamesServer.HostingType.LAN;
    private boolean useDynamicPort;

    @Override
    public void enterGui() {
        super.enterGui();
        launchGuiButton = new GuiTextBasedButton(width / 2 - width / 16, 40, width / 8, 40, "Launch Lobby!");
        switchHostTypeButton = new GuiTextBasedButton(20, height - 120, 300, 30, "Switch type!");
        switchDynamicPortButton = new GuiTextBasedButton(20, height - 160, 300, 30, "Switch use dynmaic port!");
        specificPortTextField = new GuiTextField(530, height - 160, 150, 45, "port:", "13327", CharList.DIGITS);

        registerComponent(launchGuiButton);
        registerComponent(switchHostTypeButton);
        registerComponent(switchDynamicPortButton);
        registerComponent(specificPortTextField);
    }

    @Override
    protected void renderGui(int mouseX, int mouseY, OrthographicCamera camera, float partialTicks) {
        super.renderGui(mouseX, mouseY, camera, partialTicks);
        spriteBatch.begin();
        RenderUtils.drawString(hostingType.name(), dg.defaultFont16, 350, height - 120, spriteBatch, Color.WHITE);
        spriteBatch.end();

    }

    @Override
    protected void updateGui(int mouseX, int mouseY) {
        super.updateGui(mouseX, mouseY);

    }

    @Override
    public void buttonCallback(GuiButton button) {
        if (button == launchGuiButton) {
            try {
                int port = Integer.parseInt(specificPortTextField.getText());
                dg.showGui(new GuiLobby(true, new HostingInfo(hostingType, port)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (button == switchHostTypeButton) {
            hostingType = hostingType == DuckGamesServer.HostingType.LAN ? DuckGamesServer.HostingType.INTERNET : DuckGamesServer.HostingType.LAN;
            boolean lanHosted = hostingType == DuckGamesServer.HostingType.LAN;
            switchDynamicPortButton.setShown(!lanHosted);
            if (lanHosted) {
                specificPortTextField.setShown(false);
            }
        } else if (button == switchDynamicPortButton) {
            useDynamicPort = !useDynamicPort;
            specificPortTextField.setShown(useDynamicPort);
        }
    }

    @Override
    public void exitGui() {
        super.exitGui();

    }
}
