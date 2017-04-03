package de.intektor.duckgames.client.editor.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import de.intektor.duckgames.client.editor.EditableGameMap;
import de.intektor.duckgames.client.gui.components.GuiButton;
import de.intektor.duckgames.client.gui.components.GuiFrame;
import de.intektor.duckgames.client.gui.components.GuiTextBasedButton;
import de.intektor.duckgames.client.gui.components.GuiTextField;
import de.intektor.duckgames.client.gui.guis.GuiLevelEditor;
import de.intektor.duckgames.client.i18n.I18n;
import de.intektor.duckgames.client.rendering.RenderUtils;
import de.intektor.duckgames.util.charlist.CharList;

/**
 * @author Intektor
 */
public class SaveWorldGuiComponent extends GuiFrame {

    private GuiTextField worldNameTextField;
    private GuiTextBasedButton saveWorldButton;

    private BitmapFont font = dg.defaultFont28;

    private boolean noWorldNameSetError;

    private GuiLevelEditor levelEditor;

    public SaveWorldGuiComponent(int x, int y, int width, int height, GuiLevelEditor levelEditor) {
        super(x, y, width, height, I18n.translate("level_editor.save_world.frame_name"));
        worldNameTextField = new GuiTextField(20, (int) (height - topBarHeight - font.getLineHeight() * 2), width - 40, (int) font.getLineHeight(), I18n.translate("level_editor.save_world.text_field_world_name_hint"), CharList.combine(CharList.LETTERS, CharList.SPACE));
        saveWorldButton = new GuiTextBasedButton(20, 0, width - 40, (int) font.getLineHeight(), I18n.translate("level_editor.save_world.save_world_button_description"), true);
        registerGuiComponent(worldNameTextField);
        registerGuiComponent(saveWorldButton);
        this.levelEditor = levelEditor;
    }

    @Override
    protected void drawBody(float drawX, float drawY, OrthographicCamera camera, ShapeRenderer sR, SpriteBatch sB, float partialTicks, int mouseX, int mouseY) {
        sR.begin(ShapeRenderer.ShapeType.Filled);
        sR.setColor(Color.GRAY);
        sR.rect(drawX, drawY, width, height - topBarHeight);
        sR.end();

        if (noWorldNameSetError) {
            sB.begin();
            RenderUtils.drawString(I18n.translate("level_editor.save_world.missing_world_name"), font, drawX + 20, drawY + height - font.getLineHeight(), sB, Color.RED);
            sB.end();
        }
    }

    @Override
    protected void updateComponent(int mouseX, int mouseY, float drawX, float drawY) {
        super.updateComponent(mouseX, mouseY, drawX, drawY);
    }

    @Override
    public void buttonCallback(GuiButton button) {
        super.buttonCallback(button);
        if (button == saveWorldButton) {
            if (worldNameTextField.getText().length() == 0) {
                noWorldNameSetError = true;
            } else {
                EditableGameMap map = levelEditor.getMap();
                map.saveMapToFile(worldNameTextField.getText());
            }
        }
    }
}
