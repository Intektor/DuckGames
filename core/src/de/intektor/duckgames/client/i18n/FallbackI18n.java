package de.intektor.duckgames.client.i18n;

import com.google.common.base.MoreObjects;

/**
 * @author Intektor
 */
public class FallbackI18n extends I18n {

    private I18n mainTranslation, fallbackTranslation;

    public FallbackI18n(I18n mainTranslation, I18n fallbackTranslation) {
        this.mainTranslation = mainTranslation;
        this.fallbackTranslation = fallbackTranslation;
    }

    @Override
    public String translate0(String s) {
        String t = mainTranslation.translate0(s);
        if (mainTranslation == null) t = fallbackTranslation.translate0(s);
        return MoreObjects.firstNonNull(t, s);
    }

    @Override
    public String translate0(String s, Object... args) {
        String t = mainTranslation.translate0(s, args);
        if (mainTranslation == null) t = fallbackTranslation.translate0(s, args);
        return MoreObjects.firstNonNull(t, s);
    }

    public void changeMainTranslation(FileBasedI18n translation) {
        mainTranslation = translation;
    }
}
