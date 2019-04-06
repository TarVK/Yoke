package com.yoke.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;


import com.yoke.Helper.LocaleManager;
import com.yoke.Helper.MainApp;

public class BaseActivity extends AppCompatActivity {

//    LocaleManager localeManager;
//
//    public BaseActivity() {
//        localeManager = new LocaleManager(this);
//    }
//
//    public BaseActivity(LocaleManager localeManager) {
//        this.localeManager = localeManager;
//    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(MainApp.localeManager.setLocale(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println(MainApp.localeManager.getLocale(getResources()));
//        localeManager.setNewLocale(getApplicationContext(), localeManager.getLanguage());
        resetTitle();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
//        updateView(sharedPreferences.getString("language", "en"));


    }

//    private void updateView(String language) {
//        Context context = localeManager.setLocale(this);
//        Resources resources = context.getResources();
//    }

//    private void updateInfo()

    private void resetTitle() {
        try {
            int label = getPackageManager().getActivityInfo(getComponentName(), PackageManager.GET_META_DATA).labelRes;
            if (label != 0) {
                setTitle(label);
            }

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
}
