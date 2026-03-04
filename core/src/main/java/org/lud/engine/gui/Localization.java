package org.lud.engine.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.I18NBundle;
import org.lud.engine.enums.LangKeys;

import java.util.Locale;

public class Localization {
    public static Localization lang =
        new Localization(Locale.forLanguageTag("en"));
    private static final LangKeys[] ALL_KEYS = LangKeys.values();
    private I18NBundle bundle;
    private Locale locale;

    public Localization(Locale locale) {
        setLocale(locale);
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
        FileHandle baseFile = Gdx.files.internal("i18n/lang");
        bundle = I18NBundle.createBundle(baseFile, locale, "ISO-8859-1");
    }

    public String t(String key) {
        try {
            return bundle.get(key);
        } catch (Exception e) {
            return key;
        }
    }

    public String f(String key, Object... args) {
        try {
            return bundle.format(key, args);
        } catch (Exception e) {
            return key;
        }
    }

    public Locale getLocale() {
        return locale;
    }

    public String getAllStrings() {
        StringBuilder allText = new StringBuilder();
        for (LangKeys group : ALL_KEYS) {
            for (String key : group.getKeys()) {
                try {
                    allText.append(bundle.get(key));
                } catch(Exception ignored) {}
            }
        }
        return allText.toString();
    }
}
