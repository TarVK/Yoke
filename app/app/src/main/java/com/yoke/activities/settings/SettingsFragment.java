package com.yoke.activities.settings;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.example.yoke.R;

public class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        super.onCreate(savedInstanceState);
        setPreferencesFromResource(R.xml.preferences, rootKey);
    }
}
