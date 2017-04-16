package de.intektor.duckgames.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import de.intektor.duckgames.client.editor.EditableGameMap;
import de.intektor.duckgames.client.gui.components.GuiTextBasedButton;
import de.intektor.duckgames.tag.TagCompound;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Intektor
 */
public class WorldUtils {

    public static List<EditableGameMap> getWorldList() throws IOException {
        List<EditableGameMap> list = new ArrayList<EditableGameMap>();
        FileHandle handle = Gdx.files.local("saves/user");
        for (FileHandle fileHandle : handle.list()) {
            TagCompound tag = new TagCompound();
            tag.readFromStream(new DataInputStream(new FileInputStream(fileHandle.file())));
            EditableGameMap editableGameMap = EditableGameMap.readMapFromCompound(tag);
            GuiTextBasedButton button = new GuiTextBasedButton(0, 0, width, 80, editableGameMap.getSaveName(), true);
            buttonMap.put(button, editableGameMap);
            registerGuiComponent(button);
        }
    }
}
