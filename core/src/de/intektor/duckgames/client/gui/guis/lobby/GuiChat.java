package de.intektor.duckgames.client.gui.guis.lobby;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import de.intektor.duckgames.client.gui.GuiMultiComponent;
import de.intektor.duckgames.client.gui.components.GuiButton;
import de.intektor.duckgames.client.gui.components.GuiScrollBar;
import de.intektor.duckgames.client.gui.components.GuiTextBasedButton;
import de.intektor.duckgames.client.gui.components.GuiTextField;
import de.intektor.duckgames.client.gui.util.GuiUtils;
import de.intektor.duckgames.client.rendering.RenderUtils;
import de.intektor.duckgames.common.chat.IChatMessage;
import de.intektor.duckgames.common.net.client_to_server.ChatMessagePacketToServer;
import de.intektor.duckgames.util.charlist.CharList;

import java.util.ArrayList;
import java.util.List;

import static com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType.Filled;

/**
 * @author Intektor
 */
public class GuiChat extends GuiMultiComponent {

    private GuiScrollBar messageScrollBar;
    private GuiTextBasedButton sendMessageButton;
    private GuiTextField chatWritingField;

    private BitmapFont font = dg.defaultFont28;

    private List<IChatMessage> messages = new ArrayList<IChatMessage>();

    public GuiChat(int x, int y, int width, int height) {
        super(x, y, width, height);
        messageScrollBar = new GuiScrollBar(width - 20, 0, 20, height, GuiScrollBar.Direction.VERTICAL, 0, (int) (height - font.getLineHeight()));
        registerGuiComponent(messageScrollBar);

        sendMessageButton = new GuiTextBasedButton(width - 100, 0, 100, (int) font.getLineHeight(), "Send!", true);
        registerGuiComponent(sendMessageButton);

        chatWritingField = new GuiTextField(0, 0, width - 100, (int) font.getLineHeight(), "Type message here!", CharList.combine(CharList.LETTERS_AND_DIGITS, CharList.PUNCTUATION_MARKS, CharList.SPACE));
        registerGuiComponent(chatWritingField);
    }

    @Override
    protected void renderComponent(float drawX, float drawY, int mouseX, int mouseY, OrthographicCamera camera, ShapeRenderer sR, SpriteBatch sB, float partialTicks) {
        sR.begin(Filled);
        sR.setColor(new Color(0.1f, 0.1f, 0.1f, 1f));
        sR.rect(drawX, drawY, width, height);
        sR.end();
        sB.begin();
        Gdx.gl.glEnable(GL20.GL_SCISSOR_TEST);
        Gdx.gl.glScissor(GuiUtils.unscaleScreenCoordX(drawX), GuiUtils.unscaleScreenCoordY(drawY + font.getLineHeight()),
                GuiUtils.unscaleScreenCoordX(width), GuiUtils.unscaleScreenCoordX(height - font.getLineHeight()));
        float y = drawY + chatWritingField.getHeight() + font.getLineHeight();
        if (messages.size() * font.getLineHeight() > height - font.getLineHeight()) {
            y -= (font.getLineHeight() * messages.size() - height + font.getLineHeight()) * (1 - messageScrollBar.getScrollPercent());
        }
        for (int i = messages.size() - 1; i >= 0; i--) {
            IChatMessage message = messages.get(i);
            String text = message.getMessage();
            RenderUtils.drawString(text, font, drawX, y, sB, message.getMessageColor());
            y += font.getLineHeight();
        }
        sB.end();
        Gdx.gl.glDisable(GL20.GL_SCISSOR_TEST);
        super.renderComponent(drawX, drawY, mouseX, mouseY, camera, sR, sB, partialTicks);
    }

    @Override
    protected void updateComponent(int mouseX, int mouseY, float drawX, float drawY) {
        super.updateComponent(mouseX, mouseY, drawX, drawY);
    }

    @Override
    public void clickDown(int mouseX, int mouseY, int pointer, int button, float drawX, float drawY) {
        super.clickDown(mouseX, mouseY, pointer, button, drawX, drawY);
    }

    @Override
    public void clickUp(int mouseX, int mouseY, int pointer, int button, float drawX, float drawY) {
        super.clickUp(mouseX, mouseY, pointer, button, drawX, drawY);
    }

    @Override
    public void keyDown(int mouseX, int mouseY, int keyCode) {
        super.keyDown(mouseX, mouseY, keyCode);
        switch (keyCode) {
            case Input.Keys.ENTER:
                sendMessage();
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
        if (button == sendMessageButton) {
            sendMessage();
        }
    }

    public void addMessage(IChatMessage message) {
        messages.add(message);
        messageScrollBar.addAllWindowSize((int) font.getLineHeight() + 1);
    }

    private void sendMessage() {
        dg.sendPacketToServer(new ChatMessagePacketToServer(chatWritingField.getText()));
        chatWritingField.setText("");
    }
}
