package com.example.mazegame.data;

import timber.log.Timber;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.os.LocaleListCompat;

public final class LanguageManager {

    public static final String SYSTEM = "";
    public static final String ENGLISH = "en";
    public static final String UKRAINIAN = "uk";

    private LanguageManager() {
    }

    public static String current() {
        LocaleListCompat locales = AppCompatDelegate.getApplicationLocales();
        if (locales.isEmpty() || locales.get(0) == null) {
            return SYSTEM;
        }
        String language = locales.get(0).getLanguage();
        if (UKRAINIAN.equals(language)) {
            return UKRAINIAN;
        }
        if (ENGLISH.equals(language)) {
            return ENGLISH;
        }
        return SYSTEM;
    }

    public static void apply(String language) {
        Timber.i("Language set to '%s'", SYSTEM.equals(language) ? "system" : language);
        if (SYSTEM.equals(language)) {
            AppCompatDelegate.setApplicationLocales(LocaleListCompat.getEmptyLocaleList());
        } else {
            AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(language));
        }
    }
}