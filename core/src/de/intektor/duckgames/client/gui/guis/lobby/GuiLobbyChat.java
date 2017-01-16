package de.intektor.duckgames.client.gui.guis.lobby;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import de.intektor.duckgames.client.gui.GuiMultiComponent;
import de.intektor.duckgames.client.gui.components.GuiButton;
import de.intektor.duckgames.client.gui.components.GuiScrollBar;
import de.intektor.duckgames.client.gui.components.GuiTextBasedButton;
import de.intektor.duckgames.client.gui.util.GuiUtils;
import de.intektor.duckgames.client.rendering.FontUtils;
import de.intektor.duckgames.client.rendering.RenderUtils;
import de.intektor.duckgames.common.chat.ChatMessage;

import java.util.ArrayList;
import java.util.List;

import static com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType.Filled;

/**
 * @author Intektor
 */
public class GuiLobbyChat extends GuiMultiComponent {

    private GuiScrollBar messageScrollBar;
    private GuiTextBasedButton sendMessageButton;

    private List<ChatMessage> messages = new ArrayList<ChatMessage>();

    private String currentlyTyped = "";
    private boolean currentlyTypingMessage;

    public GuiLobbyChat(int x, int y, int width, int height) {
        super(x, y, width, height);
        messageScrollBar = new GuiScrollBar(width - 20, 0, 20, height, GuiScrollBar.Direction.VERTICAL, 0, height);
        registerGuiComponent(messageScrollBar);
        sendMessageButton = new GuiTextBasedButton(width - 50, 0, 100, 50, "Send!");
        registerGuiComponent(sendMessageButton);
    }

    @Override
    protected void renderComponent(float drawX, float drawY, int mouseX, int mouseY, OrthographicCamera camera, ShapeRenderer sR, SpriteBatch sB, float partialTicks) {
        super.renderComponent(drawX, drawY, mouseX, mouseY, camera, sR, sB, partialTicks);
        sR.begin(Filled);
        sR.setColor(Color.BLUE);
        sR.rect(drawX, drawY, width, height);
        sR.end();
        sB.begin();
        BitmapFont font = dg.defaultFont28;
        RenderUtils.drawString(currentlyTyped, font, drawX, drawY + FontUtils.getStringHeight(currentlyTyped, font), sB, Color.WHITE);
        sB.end();
    }

    @Override
    protected void updateComponent(int mouseX, int mouseY, float drawX, float drawY) {
        super.updateComponent(mouseX, mouseY, drawX, drawY);
    }

    @Override
    public void clickDown(int mouseX, int mouseY, int pointer, int button, float drawX, float drawY) {
        super.clickDown(mouseX, mouseY, pointer, button, drawX, drawY);
        currentlyTypingMessage = GuiUtils.isPointInRegion(x, y, width, 50, mouseX, mouseY);
    }

    @Override
    public void clickUp(int mouseX, int mouseY, int pointer, int button, float drawX, float drawY) {
        super.clickUp(mouseX, mouseY, pointer, button, drawX, drawY);
    }

    @Override
    public void charTyped(int mouseX, int mouseY, char character) {
        super.charTyped(mouseX, mouseY, character);
        switch (character) {
            case '\b':
                currentlyTyped = currentlyTyped.substring(0, currentlyTyped.length() - 1);
                break;
            default:
                currentlyTyped += character;
                break;
        }
    }

    @Override
    public void scroll(int mouseX, int mouseY, int scrollAmount) {
        super.scroll(mouseX, mouseY, scrollAmount);
    }

    @Override
    public void buttonCallback(GuiButton button) {
        super.buttonCallback(button);
    }

    public void addMessage(ChatMessage message) {
        messages.add(message);
    }

    private void sendMessage() {

        currentlyTyped = "";
    }
}
