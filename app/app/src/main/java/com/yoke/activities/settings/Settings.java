package com.yoke.activities.settings;


import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.yoke.R;

public class Settings extends AppCompatActivity {

    /*Preference language;
    SwitchPreference lightTheme;
    Preference color;
    Preference connection;
    Preference autoStartUp;
    Preference tutorial;
    Preference about;*/

    TextView textViewLang;
    TextView textViewConn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        // Doesn't seem to be required?
        Toolbar toolbar = findViewById(R.id.toolbarSettings);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);

        if (findViewById(R.id.fragment_container) != null) {
            if (savedInstanceState != null){
                return;
            }
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,
                    new SettingsFragment()).commit();

        }
//
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);



        // Definitely don't do this: can't just put android layout components in the settings xml
        // That's also why you shouldn't place it in the layout's folder, it isn't a layout
        // It is a different type of xml file, that just stores settings data
        // Changing values should be done from the fragment

//        textViewLang = findViewById(R.id.currLanguage);
//        textViewLang.setText(com.yoke.database.types.Settings.getInstance().getLanguage());
//
//        textViewConn = findViewById(R.id.currConn);
//        textViewConn.setText(com.yoke.database.types.Settings.getInstance().getConnectionType());



    }
}
