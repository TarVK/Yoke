package com.yoke.Helper;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;

public class MainApp extends Application {

    private final String TAG = "MainApp";

    //the locale manager takes care of updating the app language independently
    // from the device's language
    public static LocaleManager localeManager;

    @Override
    protected void attachBaseContext(Context base){
        localeManager = new LocaleManager(base);
        super.attachBaseContext(localeManager.setLocale(base));
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        localeManager.setLocale(getApplicationContext());
    }
}
