package de.intektor.duckgames.client.gui.guis.lobby;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import de.intektor.duckgames.client.editor.EditableGameMap;
import de.intektor.duckgames.client.gui.components.GuiButton;
import de.intektor.duckgames.client.gui.components.GuiFrame;
import de.intektor.duckgames.client.gui.components.GuiScrollBar;
import de.intektor.duckgames.client.gui.components.GuiTextBasedButton;
import de.intektor.tag.TagCompound;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Intektor
 */
public class SelectWorldToPlayGuiComponent extends GuiFrame {

    private Map<GuiTextBasedButton, EditableGameMap> buttonMap = new HashMap<GuiTextBasedButton, EditableGameMap>();
    private GuiScrollBar scrollBar;

    private BitmapFont font = dg.defaultFont28;

    private GuiLobby lobby;

    public SelectWorldToPlayGuiComponent(int x, int y, int width, int height, GuiLobby lobby) {
        super(x, y, width, height, "Select World!");
        try {
            FileHandle handle = Gdx.files.local("saves/user");
            for (FileHandle fileHandle : handle.list()) {
                TagCompound tag = new TagCompound();
                tag.readFromStream(new DataInputStream(new FileInputStream(fileHandle.file())));
                EditableGameMap editableGameMap = EditableGameMap.readMapFromCompound(tag);
                GuiTextBasedButton button = new GuiTextBasedButton(0, 0, width, (int) font.getLineHeight(), editableGameMap.getSaveName());
                buttonMap.put(button, editableGameMap);
                registerGuiComponent(button);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        scrollBar = new GuiScrollBar(width - 20, 0, 20, height, GuiScrollBar.Direction.VERTICAL, (int) (buttonMap.size() * font.getLineHeight()), 0);
        registerGuiComponent(scrollBar);
        this.lobby = lobby;
    }

    @Override
    protected void drawBody(float drawX, float drawY, OrthographicCamera camera, ShapeRenderer sR, SpriteBatch sB, float partialTicks, int mouseX, int mouseY) {
        sR.begin(ShapeRenderer.ShapeType.Filled);
        sR.setColor(Color.GRAY);
        sR.rect(drawX, drawY, width, height - topBarHeight);
        sR.end();

        int y = (int) (height - topBarHeight - font.getLineHeight());

        for (GuiTextBasedButton button : buttonMap.keySet()) {
            button.setPosition(0, y);
            y -= button.getHeight();
        }
    }

    @Override
    public void buttonCallback(GuiButton button) {
        super.buttonCallback(button);
        System.out.println("callback");
        if (button instanceof GuiTextBasedButton) {
            EditableGameMap editableGameMap = buttonMap.get(button);
            lobby.setSelectedMap(editableGameMap);
            setShown(false);
        }
    }
}
