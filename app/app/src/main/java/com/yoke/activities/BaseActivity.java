package com.yoke.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;


import com.example.yoke.R;
import com.yoke.Helper.LocaleManager;
import com.yoke.Helper.MainApp;
import com.yoke.activities.settings.SettingsFragment;

//import static com.yoke.activities.settings.SettingsFragment.preferences;

public class BaseActivity extends AppCompatActivity {

    Toolbar toolbar;
    TabLayout tabLayout;
    FloatingActionButton fab;
    SharedPreferences sharedPreferences;
    Class type;

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


    public final void setNewThemeColour(int ID, Class type) {
        this.type = type;
        int colorPrimary = ContextCompat.getColor(this, R.color.colorPrimary);
        int color = sharedPreferences.getInt("color", colorPrimary);
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //maybe there is a better way to do this, abstracting from the type?
        if (type == Toolbar.class) {
            Log.e("toolbar", "yes");
            toolbar = findViewById(ID);
            toolbar.setBackgroundColor(color);
            if (Build.VERSION.SDK_INT >= 21) {
                window.setStatusBarColor(darkenColor(color));
            }
        } else if (type == TabLayout.class) {
            Log.e("tabs", "yes");
            tabLayout = findViewById(ID);
            tabLayout.setBackgroundColor(color);
        } else if (type == FloatingActionButton.class){
            Log.e("fab called", "yes");
            fab = findViewById(ID);
            fab.setBackgroundTintList(ColorStateList.valueOf(darkenColor(color)));
        }
    }

    private int darkenColor(int color) {
        float[] hsv = new float[3];
        //int color = getColor();
        Color.colorToHSV(color, hsv);
        hsv[2] *= 0.8f; // value component
        color = Color.HSVToColor(hsv);
        return color;
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
