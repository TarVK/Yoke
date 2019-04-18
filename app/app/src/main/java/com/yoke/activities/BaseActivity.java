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
import android.view.Window;
import android.view.WindowManager;


import com.example.yoke.R;
import com.yoke.Helper.LocaleManager;
import com.yoke.Helper.MainApp;
import com.yoke.activities.settings.SettingsFragment;

public class BaseActivity extends AppCompatActivity {

   SharedPreferences sharedPreferences;
    //type of the view whose color will be updated
    Class type;
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
        //System.out.println(MainApp.localeManager.getLocale(getResources()));
        resetTitle();
        sharedPreferences = getSharedPreferences("preferences", Context.MODE_PRIVATE); //PreferenceManager.getDefaultSharedPreferences(this);

    }

    /**
     * Sets new theme color
     * @pre {@code type <> null}
     * @param ID id of the view to be updated
     * @param type class of the view to be updated
     * @modifies view with id ID
     * @returns void
     * @throws NullPointerException if type ==  null
     */
    public final void setNewThemeColour(int ID, Class type) {
        if (type == null) {
            throw new NullPointerException("Class type cannot be null");
        }
        this.type = type;
        //default theme color
        int colorPrimary = ContextCompat.getColor(this, R.color.colorPrimary);
        //currently chosen color, colorPrimary is returned when a color is not present
        int color = sharedPreferences.getInt("color", colorPrimary);
        Window window = getWindow();
        //clearing and adding the flags is needed for setting the status bar color
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //maybe there is a better way to do this, abstracting from the type?
        if (type == Toolbar.class) {
            //finds the toolbar and sets its color
            toolbar = findViewById(ID);
            toolbar.setBackgroundColor(color);
            //setting the status bar's color in this way is only possible for API level >= 21
            if (Build.VERSION.SDK_INT >= 21) {
                window.setStatusBarColor(darkenColor(color));
            }
        } else if (type == Window.class && Build.VERSION.SDK_INT >= 21) {
            //sets the status bat color (API level >= 21)
            window.setStatusBarColor(darkenColor(color));
        } else if (type == TabLayout.class) {
            //finds the tab layout and sets its color
            tabLayout = findViewById(ID);
            tabLayout.setBackgroundColor(color);
        } else if (type == FloatingActionButton.class){
            //finds the floating action button and sets its color
            fab = findViewById(ID);
            fab.setBackgroundTintList(ColorStateList.valueOf(darkenColor(color)));
        }
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
        hsv[2] *= 0.8f; // value component
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
