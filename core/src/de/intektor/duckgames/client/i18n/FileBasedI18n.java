package de.intektor.duckgames.client.i18n;

import com.badlogic.gdx.files.FileHandle;
import com.google.common.collect.Maps;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

/**
 * @author Intektor
 */
public class FileBasedI18n extends I18n {

    private Map<String, String> translationMap;

    public FileBasedI18n(FileHandle file) throws IOException {
        Properties properties = new Properties();
        properties.load(new FileInputStream(file.file()));
        translationMap = Maps.fromProperties(properties);
    }

    @Override
    public String translate0(String s) {
        return translationMap.get(s);
    }

    @Override
    public String translate0(String s, Object... args) {
        return String.format(translationMap.get(s), args);
    }
}
