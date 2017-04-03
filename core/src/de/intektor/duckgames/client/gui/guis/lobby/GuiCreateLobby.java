package de.intektor.duckgames.client.gui.guis.lobby;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import de.intektor.duckgames.client.gui.Gui;
import de.intektor.duckgames.client.gui.components.GuiButton;
import de.intektor.duckgames.client.gui.components.GuiTextBasedButton;
import de.intektor.duckgames.client.gui.components.GuiTextField;
import de.intektor.duckgames.client.gui.guis.GuiMainMenu;
import de.intektor.duckgames.client.i18n.I18n;
import de.intektor.duckgames.client.rendering.RenderUtils;
import de.intektor.duckgames.common.HostingInfo;
import de.intektor.duckgames.common.HostingType;
import de.intektor.duckgames.util.charlist.CharList;

/**
 * @author Intektor
 */
public class GuiCreateLobby extends Gui {

    private GuiButton launchGuiButton;
    private GuiButton switchHostTypeButton;
    private GuiButton switchDynamicPortButton;
    private GuiButton buttonBack;
    private GuiTextField specificPortTextField;

    private HostingType hostingType = HostingType.INTERNET;
    private boolean useDynamicPort = false;

    @Override
    public void enterGui() {
        super.enterGui();
        launchGuiButton = new GuiTextBasedButton(width / 2 - 250, 80, 500, 80, "Launch Lobby!", true);
        switchHostTypeButton = new GuiTextBasedButton(20, height - 120, 500, 60, "Switch type!", true);
        switchDynamicPortButton = new GuiTextBasedButton(20, height - 200, 500, 60, "Switch use dynamic port!", true);
        specificPortTextField = new GuiTextField(700, height - 200 + 30 - 45 / 2, 150, 45, "port:", "13327", CharList.DIGITS);

        buttonBack = new GuiTextBasedButton(0, 0, 200, (int) dg.defaultFont28.getLineHeight(), I18n.translate("gui.button.back_button.text"), true);

        registerComponent(launchGuiButton);
        registerComponent(switchHostTypeButton);
        registerComponent(switchDynamicPortButton);
        registerComponent(specificPortTextField);
        registerComponent(buttonBack);
    }

    @Override
    protected void renderGui(int mouseX, int mouseY, OrthographicCamera camera, float partialTicks) {
        super.renderGui(mouseX, mouseY, camera, partialTicks);
        spriteBatch.begin();
        RenderUtils.drawString(hostingType.name(), dg.defaultFont28, 700, height - 120 + 30, spriteBatch, Color.WHITE, false, true);
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
            hostingType = hostingType == HostingType.LAN ? HostingType.INTERNET : HostingType.LAN;
            boolean lanHosted = hostingType == HostingType.LAN;
            switchDynamicPortButton.setShown(!lanHosted);
            if (lanHosted) {
                specificPortTextField.setShown(false);
            } else {
                specificPortTextField.setShown(!useDynamicPort);
            }
        } else if (button == switchDynamicPortButton) {
            useDynamicPort = !useDynamicPort;
            specificPortTextField.setShown(!useDynamicPort);
        } else if (button == buttonBack) {
            dg.showGui(new GuiMainMenu());
        }
    }

    @Override
    public void exitGui() {
        super.exitGui();

    }
}
