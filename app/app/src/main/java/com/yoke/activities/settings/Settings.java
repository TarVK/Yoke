package com.yoke.activities.settings;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.yoke.R;

public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings_container, new SettingsFragment())
                .commit();

    }
}
