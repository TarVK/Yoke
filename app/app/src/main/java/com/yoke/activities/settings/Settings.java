package com.yoke.activities.settings;

import android.preference.SwitchPreference;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;

import com.example.yoke.R;


import static com.example.yoke.R.xml.*;

public class Settings extends AppCompatActivity {

    View language;
    SwitchPreference lightTheme;
    Preference color;
    Preference connection;
    Preference autoStartUp;
    Preference tutorial;
    Preference about;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        if (findViewById(R.id.fragment_container) != null) {
            if (savedInstanceState != null){
                return;
            }
            getSupportFragmentManager().beginTransaction().add(R.id.settings_container, new SettingsFragment()).commit();

        }

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        language = findViewById(R.id.language);

        Preference.OnPreferenceClickListener clickListener=new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                return true;
            }
        };
    }
}
