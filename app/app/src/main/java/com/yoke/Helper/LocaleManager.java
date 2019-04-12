package com.yoke.Helper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;

import static android.os.Build.VERSION_CODES.N;

import com.yoke.activities.settings.SettingsFragment;

import java.util.Locale;

public class LocaleManager {

    public static final String LANGUAGE_ENGLISH = "en";
    public static final String LANGUAGE_DUTCH = "ne";
    public static final String LANGUAGE_BULGARIAN = "bg";
    public static final String LANGUAGE_KEY = "language";

    private final SharedPreferences preferences;

    public LocaleManager(Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }


    public Context setLocale(Context context) {
        Log.d("LocaleManager", "setLocale");
        return updateResources(context, getLanguage());
    }

    public Context setNewLocale(Context context, String language) {
        Log.d("LocaleManager", "setNewLocale");
        persistLanguage(language);
        return updateResources(context, language);
    }

    public String getLanguage() {
        return preferences.getString(LANGUAGE_KEY, LANGUAGE_ENGLISH);
    }

    @SuppressLint("ApplySharedPref")
    private void persistLanguage(String language) {
        preferences.edit().putString(LANGUAGE_KEY, language).commit();
    }

    private Context updateResources(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Resources resources = context.getResources();
        Configuration config = new Configuration(resources.getConfiguration());
        config.setLocale(locale);

        return context.createConfigurationContext(config);
    }

    public static Locale getLocale(Resources resources) {
        Configuration configuration = resources.getConfiguration();
        if (Build.VERSION.SDK_INT >= 24) {
            return configuration.getLocales().get(0);
        } else {
            return configuration.locale;
        }
    }
}
