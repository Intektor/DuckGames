package de.intektor.duckgames.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import de.intektor.duckgames.client.editor.EditableGameMap;
import de.intektor.duckgames.data_storage.tag.TagCompound;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Intektor
 */
public class WorldUtils {

    public static List<EditableGameMap> getWorldList(String uri) throws IOException {
        List<EditableGameMap> list = new ArrayList<EditableGameMap>();
        FileHandle handle = Gdx.files.local(uri);
        for (FileHandle fileHandle : handle.list()) {
            TagCompound tag = new TagCompound();
            tag.readFromStream(new DataInputStream(new FileInputStream(fileHandle.file())));
            EditableGameMap editableGameMap = EditableGameMap.readMapFromCompound(tag);
            list.add(editableGameMap);
        }
        return list;
    }
}
