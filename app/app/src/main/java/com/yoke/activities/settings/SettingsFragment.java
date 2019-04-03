package com.yoke.activities.settings;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.yoke.R;
import com.yoke.activities.tutorial.TutorialActivity;
import com.yoke.database.types.Settings;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preferences);

        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();


        Preference tutorial = findPreference("tutorial");
        //System.out.println(tutorial);

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
                //TODO for this I need Locale
                editor.putString(getString(R.string.language), o.toString());
                language.setSummary(o.toString());
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
