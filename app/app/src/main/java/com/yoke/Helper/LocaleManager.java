package com.yoke.Helper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.preference.PreferenceManager;

import java.util.Locale;

//Used code and information from https://proandroiddev.com/change-language-programmatically-at-runtime-on-android-5e6bc15c758

public class LocaleManager {

    public static final String LANGUAGE_ENGLISH = "en";
    public static final String LANGUAGE_DUTCH = "ne";
    public static final String LANGUAGE_BULGARIAN = "bg";
    public static final String LANGUAGE_KEY = "language";

    private final SharedPreferences preferences;
    private final SharedPreferences.Editor editor;

    /** LocaleManager constructor
     * @param context
     * @pre {@code context <> null}
     * @modifies preferences, editor
     * @post preferences and editor are initialised
     */
    public LocaleManager(Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = preferences.edit();
    }

    /**
     * Forwards call to UpdateResources()
     * @param context
     * @pre {@code context <> null}
     * @return Context - updated context
     */
    public Context setLocale(Context context) {
        return updateResources(context, getLanguage());
    }

    /**
     * Updates the value of the language preference and forwards call to UpdateResources()
     * @param context
     * @param language
     * @pre {@code context <> null && language <> null} and language - one of the possible languages
     * @return Context - updated context
     */
    public Context setNewLocale(Context context, String language) {
        persistLanguage(language);
        return updateResources(context, language);
    }

    /**
     * Returns the value of the language preference
     * @return preferences.getString("language")
     */
    public String getLanguage() {
        return preferences.getString(LANGUAGE_KEY, LANGUAGE_ENGLISH);
    }

    /**
     * Updates the value of the language preference
     * @param language
     * @pre {@code language <> null}
     */
    @SuppressLint("ApplySharedPref")
    private void persistLanguage(String language) {
        editor.putString(LANGUAGE_KEY, language);
        editor.apply();
    }

    /**
     * Updates the context with the configuration that uses the resource file
     * that correspons to the updated Locale
     * @param context
     * @param language
     * @return updated context
     */
    private Context updateResources(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Resources resources = context.getResources();
        Configuration config = new Configuration(resources.getConfiguration());
        config.setLocale(locale);

        return context.createConfigurationContext(config);
    }

    /**
     * Getter method for the locale that takes into account different API levels
     * @param resources
     * @return locale
     */
    public static Locale getLocale(Resources resources) {
        Configuration configuration = resources.getConfiguration();
        if (Build.VERSION.SDK_INT >= 24) {
            return configuration.getLocales().get(0);
        } else {
            return configuration.locale;
        }
    }
}
