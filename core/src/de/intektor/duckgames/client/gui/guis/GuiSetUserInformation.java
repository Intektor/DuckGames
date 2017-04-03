package de.intektor.duckgames.client.gui.guis;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import de.intektor.duckgames.client.gui.Gui;
import de.intektor.duckgames.client.gui.components.GuiButton;
import de.intektor.duckgames.client.gui.components.GuiTextBasedButton;
import de.intektor.duckgames.client.gui.components.GuiTextField;
import de.intektor.duckgames.util.charlist.CharList;
import de.intektor.duckgames.tag.TagCompound;

import java.io.DataOutputStream;
import java.io.FileOutputStream;

/**
 * @author Intektor
 */
public class GuiSetUserInformation extends Gui {

    private BitmapFont font = dg.defaultFont28;

    private GuiTextField usernameField;
    private GuiTextBasedButton buttonSetUsername;

    @Override
    public void enterGui() {
        super.enterGui();
        usernameField = new GuiTextField(width / 2 - 300, (int) (height / 2 - font.getLineHeight() / 2), 600, (int) font.getLineHeight(), "Enter your username here!", CharList.combine(CharList.LETTERS_AND_DIGITS, CharList.UNDERSCORE));
        buttonSetUsername = new GuiTextBasedButton(width / 2 + 300, (int) (height / 2 - font.getLineHeight() / 2), 150, (int) font.getLineHeight(), "Set Username!", true);

        registerComponent(usernameField);
        registerComponent(buttonSetUsername);
    }

    @Override
    protected void updateGui(int mouseX, int mouseY) {
        super.updateGui(mouseX, mouseY);
    }

    @Override
    protected void renderGui(int mouseX, int mouseY, OrthographicCamera camera, float partialTicks) {
        super.renderGui(mouseX, mouseY, camera, partialTicks);
    }

    @Override
    public void buttonCallback(GuiButton button) {
        if (button == buttonSetUsername) {
            if (usernameField.getText().length() >= 3) {
                try {
                    FileHandle folder = Gdx.files.local("user/");
                    folder.mkdirs();

                    FileHandle data = Gdx.files.local("user/user.data");

                    TagCompound dataTag = new TagCompound();
                    dataTag.setString("username", usernameField.getText());
                    dataTag.writeToStream(new DataOutputStream(new FileOutputStream(data.file())));

                    dg.setUsername(usernameField.getText());

                    dg.showGui(new GuiMainMenu());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void exitGui() {
        super.exitGui();
    }
}
