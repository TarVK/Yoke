package com.yoke.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;


import com.example.yoke.R;
import com.yoke.Helper.LocaleManager;
import com.yoke.Helper.MainApp;
import com.yoke.activities.settings.SettingsFragment;

//import static com.yoke.activities.settings.SettingsFragment.preferences;

public class BaseActivity extends AppCompatActivity {

    Toolbar toolbar;
    SharedPreferences sharedPreferences;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(MainApp.localeManager.setLocale(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println(MainApp.localeManager.getLocale(getResources()));
        resetTitle();
        sharedPreferences = getSharedPreferences("preferences", Context.MODE_PRIVATE); //PreferenceManager.getDefaultSharedPreferences(this);

    }


    public final void setNewToolbarColour(int toolbarID) {
        toolbar = findViewById(toolbarID);
        Log.e("setNew toolbar =", String.valueOf(toolbarID));
        int colorPrimary = ContextCompat.getColor(this, R.color.colorPrimary);
        Log.e("primary", String.valueOf(colorPrimary));

        int color = sharedPreferences.getInt("color", colorPrimary);//SettingsFragment.getColor(); //sharedPreferences.getInt("color", colorPrimary);
        Log.e("color is", String.valueOf(color));
        toolbar.setBackgroundColor(color);
        //toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPistachioGreen));
        //toolbar.setBackgroundResource(R.color.colorPistachioGreen);
        Log.e("setBGColour called", "lets see");
        //setItemColor
    }

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
