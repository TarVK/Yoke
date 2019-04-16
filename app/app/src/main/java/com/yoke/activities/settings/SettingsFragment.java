package com.yoke.activities.settings;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.util.Log;
import android.widget.Toolbar;

import com.example.yoke.R;
import com.yoke.Helper.MainApp;
import com.yoke.activities.BaseActivity;
import com.yoke.activities.home.HomeActivity;
import com.yoke.activities.tutorial.TutorialActivity;

import yuku.ambilwarna.AmbilWarnaDialog;

import static com.yoke.activities.splash.GlobalMessageReceiver.getActivity;

public class SettingsFragment extends PreferenceFragmentCompat {

    public static SharedPreferences preferences;
    //TypedArray colorTypedArray;
    AmbilWarnaDialog colorPicker;
//    Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        localeManager = new LocaleManager(getContext());

        Resources res = getContext().getResources();
        android.content.res.Configuration conf = res.getConfiguration();
        //toolbar = BaseActivity.get
        //addPreferencesFromResource(R.xml.preferences);
        //ListPreference colorPicker = new ColorPickerDialog(getContext());
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preferences);

//        if (getActivity().getSharedPreferences("preferences", Context.MODE_PRIVATE) == null) {
//            Log.e("preferences empty", "true");
//            preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
//        }
        preferences = getActivity().getSharedPreferences("preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

//        getPreferenceScreen().setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
//            @Override
//            public boolean onPreferenceClick(Preference preference) {
//                System.out.println(preference.getKey());
//                if (preference.getKey() == "tutorial") {
////                    startActivity(new Intent((getActivity()), TutorialActivity.class));
//                    System.out.println("Tutorial clicked");
//                } else if (preference.getKey() == "about") {
//                    //github README.txt
//                } else if (preference.getKey() == "color") {
//                    Log.e("clicked", "so far so good");
//
//                    colorPicker.show();
//                }
//
//                return true;
//            }
//        });

        //Language preference
        Preference language = findPreference("language");

        if (!preferences.contains("language")) {
            //the following line was changed since i last checked that language change works
            editor.putString("language", "en");
            editor.apply();
            setLanguageSummary(language, "en");
        }

        setLanguageSummary(language, preferences.getString("language", "default"));


        language.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference language, Object o) {
                editor.putString("language", o.toString());
                editor.apply();
                setNewLocale(o.toString());

                return true;
            }
        });


        //Colour Preference
        Preference color = findPreference("color");
        color.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                int colorPrimary = ContextCompat.getColor(getContext(), R.color.colorPrimary);
                if (!preferences.contains("color")) {
                    System.out.println("No color set");
                    editor.putInt("color", colorPrimary);
                    editor.apply();
                }

                int currentColor = preferences.getInt("color", 0);
                colorPicker = new AmbilWarnaDialog(getContext(), currentColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
                    @Override
                    public void onOk(AmbilWarnaDialog dialog, int color) {

                        Log.e("old color ", "is " + preferences.getInt("color", 0));
                        editor.putInt("color", color).apply();
                        resetColor();
                        Log.e("new color applied", "color is supposed to be" + color);
                        Log.e("new color applied", "color is " + preferences.getInt("color", 0));

                        Intent i = new Intent(getContext(), Settings.class);
                        startActivity(i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                    }

                    @Override
                    public void onCancel(AmbilWarnaDialog dialog) {
                        //nothing needed here
                    }
                });

                colorPicker.show();
                return false;
            }
        });

        //Connection preference
        Preference connection = findPreference("connection");
        connection.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                editor.putString("connection", o.toString());
                editor.apply();

                return true;
            }
        });

    }

//    public static int getColor() {
//        return preferences.getInt("color", ContextCompat.getColor(getContext(), R.color.colorPrimary));
//    }

    public static SharedPreferences preferences() {
        return preferences;
    }

    private void resetColor() {
         Intent i = new Intent(getContext(), Settings.class);
         startActivity(i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    private void setNewLocale(String language) {
        MainApp.localeManager.setNewLocale(getContext(), language);

        Intent i = new Intent(getContext(), Settings.class);
        startActivity(i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));

    }


    private void setLanguageSummary(Preference language, String newLang) {
        if (newLang.equals("en")) {
            language.setSummary("English (default)");
        } else if (newLang.equals("nl")) {
            language.setSummary("Nederlands");
        } else if (newLang.equals("bg")){
            language.setSummary("Български");
        } else {
            language.setSummary("something went wrong");
        }
    }

}
