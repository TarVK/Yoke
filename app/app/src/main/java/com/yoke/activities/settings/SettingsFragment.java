package com.yoke.activities.settings;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.util.Log;

import com.example.yoke.R;
import com.yoke.Helper.MainApp;
import com.yoke.activities.home.HomeActivity;
import com.yoke.activities.tutorial.TutorialActivity;

import static com.yoke.activities.splash.GlobalMessageReceiver.getActivity;

public class SettingsFragment extends PreferenceFragmentCompat {

    //public static String preferences;
    public static SharedPreferences preferences;
//    LocaleManager localeManager;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        localeManager = new LocaleManager(getContext());

        Resources res = getContext().getResources();
        android.content.res.Configuration conf = res.getConfiguration();
        //addPreferencesFromResource(R.xml.preferences);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preferences);
        //System.out.println("whatevs");
        if (getActivity().getSharedPreferences("preferences", Context.MODE_PRIVATE) == null) {
            Log.e("preferences empty", "true");
            preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        }
        preferences = getActivity().getSharedPreferences("preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        getPreferenceScreen().setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                if (preference.getKey() == "tutorial") {
                    startActivity(new Intent((getActivity()), TutorialActivity.class));
                } else if (preference.getKey() == "about") {
                    //github README.txt
                }

                return true;
            }
        });

        //Language preference
        Preference language = findPreference("language");

        if (!preferences.contains("language")) {
            editor.putString(getString(R.string.language), "en");
            editor.apply();
            setLanguageSummary(language, "en");
        }

        setLanguageSummary(language, preferences.getString("language", "default"));


        language.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference language, Object o) {
                editor.putString("language", o.toString());
                editor.apply();
                setNewLocale(o.toString());

                return true;
            }
        });




        //Connection preference
        Preference connection = findPreference("connection");
        connection.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                editor.putString("connection", o.toString());
                editor.apply();

                return true;
            }
        });

    }


    private void setNewLocale(String language) {
        MainApp.localeManager.setNewLocale(getContext(), language);

        Intent i = new Intent(getContext(), Settings.class);
        startActivity(i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));

    }


    private void setLanguageSummary(Preference language, String newLang) {
        if (newLang.equals("en")) {
            language.setSummary("English (default)");
        } else if (newLang.equals("ne")) {
            language.setSummary("Nederlands");
        } else if (newLang.equals("bg")){
            language.setSummary("Български");
        } else {
            language.setSummary("something went wrong");
        }
    }

}
