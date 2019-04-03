package com.yoke.Helper;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;

import com.yoke.activities.settings.SettingsFragment;

public class MainApp extends Application {

    @Override
    protected void attachBaseContext(Context base){
        super.attachBaseContext(LocaleHelper.onAttach(base, "en"));
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // TODO LocaleHelper.setLocale(this, somehow get the language here);
    }
}
