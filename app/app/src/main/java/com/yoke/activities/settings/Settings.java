package com.yoke.activities.settings;

import android.preference.SwitchPreference;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
//import android.widget.Toolbar;

import com.example.yoke.R;


import org.w3c.dom.Text;

import static com.example.yoke.R.xml.*;

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
        getLayoutInflater().inflate(R.layout.toolbar_settings, (ViewGroup) findViewById(android.R.id.content));

        Toolbar toolbar = findViewById(R.id.toolbarSettings);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);

        if (findViewById(R.id.fragment_container) != null) {
            if (savedInstanceState != null){
                return;
            }
            getSupportFragmentManager().beginTransaction().add(R.id.settings_container, new SettingsFragment()).commit();

        }

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);



        textViewLang = findViewById(R.id.currLanguage);
        textViewLang.setText(com.yoke.database.types.Settings.getInstance().getLanguage());

        textViewConn = findViewById(R.id.currConn);
        textViewConn.setText(com.yoke.database.types.Settings.getInstance().getConnectionType());



    }
}
