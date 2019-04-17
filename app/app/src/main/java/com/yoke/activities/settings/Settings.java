package com.yoke.activities.settings;


import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.yoke.R;
import com.yoke.activities.BaseActivity;

public class Settings extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = findViewById(R.id.toolbarSettings);
        setSupportActionBar(toolbar);
        this.setNewThemeColour(R.id.toolbarSettings, Toolbar.class);
        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);

        if (findViewById(R.id.fragment_container) != null) {
            if (savedInstanceState != null){
                return;
            }
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,
                    new SettingsFragment()).commit();

        }

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

    }
}
