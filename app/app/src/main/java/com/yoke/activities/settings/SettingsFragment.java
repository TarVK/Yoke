package com.yoke.activities.settings;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.util.Log;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.yoke.R;
import com.yoke.Helper.MainApp;
import com.yoke.activities.BaseActivity;
import com.yoke.activities.home.HomeActivity;
import com.yoke.activities.tutorial.TutorialActivity;
import com.yoke.connection.Message;
import com.yoke.connection.messages.OpenURLCmd;
import com.yoke.connection.Connection;
import com.yoke.connection.client.MultiClientConnection;

import yuku.ambilwarna.AmbilWarnaDialog;

import static com.yoke.activities.splash.GlobalMessageReceiver.getActivity;

public class SettingsFragment extends PreferenceFragmentCompat {

    public static SharedPreferences preferences;
    AmbilWarnaDialog colorPicker;
    int colorPrimary;
    SharedPreferences.Editor editor;

    Preference language;
    Preference color;
    CheckBoxPreference mainColor;
    Preference connection;
    Preference about;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Resources res = getContext().getResources();
        android.content.res.Configuration conf = res.getConfiguration();
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preferences);

        preferences = getActivity().getSharedPreferences("preferences", Context.MODE_PRIVATE);
        editor = preferences.edit();

        language = findPreference("language");
        color = findPreference("color");
        mainColor = (CheckBoxPreference)findPreference("primary");
        connection = findPreference("connection");
        about = findPreference("about");

        setOnClickListenerLanguage();
        setOnClickListenerColor();
        setOnClickListenerMainColor();
        setOnClickListenerAbout();

    }

    private void setUpLanguage() {
        //Language preference
        if (!preferences.contains("language")) {
            editor.putString("language", "en");
            editor.apply();
            setLanguageSummary(language, "en");
        }

        setLanguageSummary(language, preferences.getString("language", "default"));
    }

    private void setOnClickListenerLanguage() {
        setUpLanguage();
        language.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference language, Object o) {
                editor.putString("language", o.toString());
                editor.apply();
                setNewLocale(o.toString());

                return true;
            }
        });

    }

    private void setNewLocale(String language) {
        MainApp.localeManager.setNewLocale(getContext(), language);
        resetActivity();
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

    //Colour Preference
    private void setUpColor() {

        colorPrimary = ContextCompat.getColor(getContext(), R.color.colorPrimary);
        if (!preferences.contains("color")) {
            System.out.println("No color set");
            editor.putInt("color", colorPrimary);
            editor.apply();
        }
    }


    //Colour Preference
    private void setOnClickListenerColor() {
        setUpColor();
        int currentColor = preferences.getInt("color", 0);
        color.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                colorPicker = new AmbilWarnaDialog(getContext(), currentColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
                    @Override
                    public void onOk(AmbilWarnaDialog dialog, int color) {
                        Log.e("old color ", "is " + preferences.getInt("color", 0));
                        editor.putInt("color", color).apply();
                        editor.putBoolean("default", false).apply();
                        mainColor.setChecked(false);
                        resetActivity();
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

    }

    private void setUpMainColor() {
        //back to default theme
        mainColor.setDefaultValue(true);
        if (!preferences.contains("default")) {
            editor.putBoolean("default", true);
            editor.apply();
            mainColor.setChecked(true);
        }

        if (mainColor.isChecked()) {
            mainColor.setEnabled(false);
        }
    }

    private void setOnClickListenerMainColor() {
        setUpMainColor();
        mainColor.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                editor.putBoolean("default", (Boolean) o);
                editor.apply();
                editor.putInt("color", colorPrimary);
                editor.apply();
                resetActivity();
                return true;
            }
        });
    }

    //for future versions
    private void setOnClickListenerConnection() {
        //Connection preference
        connection.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                editor.putString("connection", o.toString());
                editor.apply();

                return true;
            }
        });

    }

    private void setOnClickListenerAbout() {
        about.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Log.e("about", "is clicked");
                MultiClientConnection connection = MultiClientConnection.getInstance(getContext());
                Message readMe = new OpenURLCmd("https://github.com/TarVK/Yoke/blob/master/README.md");
                if (connection.getState() == Connection.CONNECTED) {
                    connection.send(readMe);
                } else {
                    Toast.makeText(getContext(), "Command could not be sent. " + "\n Please make sure you are connected", Toast.LENGTH_LONG).show();
                }
                return true;
            }
        });

    }

    private void resetActivity() {
         Intent i = new Intent(getContext(), Settings.class);
         startActivity(i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
    }


}
