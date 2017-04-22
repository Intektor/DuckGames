package de.intektor.duckgames.client.gui.guis.lobby;

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
import de.intektor.duckgames.util.WorldUtils;

import java.util.HashMap;
import java.util.List;
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
            List<EditableGameMap> worldList = WorldUtils.getWorldList("Saves/user");
            for (EditableGameMap map : worldList) {
                GuiTextBasedButton button = new GuiTextBasedButton(0, 0, width, 80, map.getSaveName(), true);
                buttonMap.put(button, map);
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

        int y = height - topBarHeight - 80;

        for (GuiTextBasedButton button : buttonMap.keySet()) {
            button.setPosition(0, y);
            y -= button.getHeight();
        }
    }

    @Override
    public void buttonCallback(GuiButton button) {
        super.buttonCallback(button);
        if (button instanceof GuiTextBasedButton) {
            EditableGameMap editableGameMap = buttonMap.get(button);
            lobby.setSelectedMap(editableGameMap);
            setShown(false);
        }
    }
}
