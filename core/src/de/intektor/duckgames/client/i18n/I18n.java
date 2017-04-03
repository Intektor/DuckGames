package de.intektor.duckgames.client.i18n;

/**
 * @author Intektor
 */
public abstract class I18n {

    private static FallbackI18n currentTranslation;

    static {
//        try {
//            FileBasedI18n us = new FileBasedI18n(Gdx.files.internal("lang/us.lang"));
//            currentTranslation = new FallbackI18n(us, us);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
    }

    public static String translate(String s) {
        return s;
//        return currentTranslation.translate0(s);
    }

    public static String translate(String s, Object... args) {
        return currentTranslation.translate0(s, args);
    }

    public static void loadTranslation(FileBasedI18n translation) {
        currentTranslation.changeMainTranslation(translation);
    }

    public abstract String translate0(String s);

    public abstract String translate0(String s, Object... args);
}
