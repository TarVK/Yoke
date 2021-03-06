package com.yoke.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.example.yoke.R;
import com.yoke.Helper.MainApp;
import com.yoke.utils.DataCallback;

//Used code and information from https://proandroiddev.com/change-language-programmatically-at-runtime-on-android-5e6bc15c758

public class BaseActivity extends AppCompatActivity {

    private static final String TAG = "BaseActivity";

    Window window;
    SharedPreferences sharedPreferences;

    Toolbar toolbar;
    TabLayout tabLayout;
    FloatingActionButton fab;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(MainApp.localeManager.setLocale(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        window = getWindow();
        resetTitle();
        sharedPreferences = getSharedPreferences("preferences", Context.MODE_PRIVATE);
//        Log.d(TAG, "Locale: " + LocaleManager.getLocale(getResources()));
    }


    /**
     * Method to set the new theme colour for the type Toolbar
     * @param toolbar Toolbar object that the new colour should be switched to
     */
    public final void setNewThemeColour(Toolbar toolbar) {
        this.toolbar = toolbar;
        Log.d(TAG, "Type is: " + toolbar);
        setExtras((color) -> {
            toolbar.setBackgroundColor(color);
            //setting the status bar's color in this way is only possible for API level >= 21
            if (Build.VERSION.SDK_INT >= 21) {
                window.setStatusBarColor(darkenColor(color));
            }
        });
    }

    /**
     * Method to set the new theme colour for the type Window
     * @param window Window object that the new colour should be switched to
     */
    public final void setNewThemeColour(Window window) {
        this.window = window;
        Log.d(TAG, "Type is: " + window);
        setExtras((color) -> {
            if (Build.VERSION.SDK_INT >= 21) {
                window.setStatusBarColor(darkenColor(color));
            }
        });
    }

    /**
     * Method to set the new theme colour for the type TabLayout
     * @param tabLayout TabLayout object that the new colour should be switched to
     */
    public final void setNewThemeColour(TabLayout tabLayout) {
        this.tabLayout = tabLayout;
        Log.d(TAG, "Type is: " + tabLayout);
        setExtras((color) -> {
            tabLayout.setBackgroundColor(color);
            tabLayout.setSelectedTabIndicatorColor(darkenColor(color));
        });
    }

    /**
     * Method to set the new theme colour for the type FloatingActionButton
     * @param fab FloatingActionButton object that the new colour should be switched to
     */
    public final void setNewThemeColour(FloatingActionButton fab) {
        this.fab = fab;
        Log.d(TAG, "Type is: " + fab);
        setExtras((color) -> {
            fab.setBackgroundTintList(ColorStateList.valueOf(darkenColor(color)));
        });
    }

    /**
     * Main routine for the color changing methods
     * @param callback Color Callback to be used for the different object types
     */
    private void setExtras(DataCallback<Integer> callback) {
        int colorPrimary = ContextCompat.getColor(this, R.color.colorPrimary);
        int color = sharedPreferences.getInt("color", colorPrimary);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        }
        callback.retrieve(color);
    }

    /**
     * Takes the colour picked by user and creates a colour a few shades darker
     * @param color color picked by user
     * @modifies color
     * @return color - darker color
     */
    private int darkenColor(int color) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] *= 0.8f;
        color = Color.HSVToColor(hsv);
        return color;
    }

    /**
     * Resets the activity's title based on the language picked by the user
     * @pre label exists
     * @throws android.content.pm.PackageManager.NameNotFoundException
     * @returns void
     */
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
