package com.yoke.Helper;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;

public class MainApp extends Application {

    private final String TAG = "MainApp";

    public static LocaleManager localeManager;

//    public MainApp() {
//        super();
//        localeManager = new LocaleManager(this.getApplicationContext());
//    }
//
//    public MainApp(LocaleManager localeManager) {
//        super();
//        this.localeManager = localeManager;
//    }

    @Override
    protected void attachBaseContext(Context base){
        localeManager = new LocaleManager(base);
        super.attachBaseContext(localeManager.setLocale(base));
        Log.d(TAG, "attachBaseContent");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        localeManager.setLocale(getApplicationContext());
        Log.d(TAG, "onConfigurationChanged");

    }
}
