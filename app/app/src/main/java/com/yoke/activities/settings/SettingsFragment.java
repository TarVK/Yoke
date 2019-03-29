package com.yoke.activities.settings;

import android.content.Intent;
//import android.support.v4.view.LayoutInflaterCompat;
import android.os.Build;
import android.view.View;
import android.view.LayoutInflater;
import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.yoke.R;
import com.yoke.activities.tutorial.TutorialActivity;
import com.yoke.database.types.Settings;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_settings, container, false);
    }


    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        //setPreferencesFromResource(R.xml.preferences, rootKey);

        com.yoke.database.types.Settings settingsData = Settings.getInstance();

        Preference tutorial = findPreference("tutorial");
        tutorial.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                startActivity(new Intent(getActivity(), TutorialActivity.class));
                return true;
            }
        });

        Preference about = findPreference("about");
        tutorial.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                //About Activity does not exist yet
                //startActivity(new Intent(getActivity(), AboutActivity.class));
                return true;
            }
        });

        Preference language = findPreference("language");
        language.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                settingsData.setLanguage(language.getKey());
                return true;
            }
        });



        Preference connection = findPreference("connection");
        language.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                //connection type stored as byte?
                //settingsData.setConnectionType(??);
                return true;
            }
        });

    }


}
