package com.yoke.activities.settings;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.widget.Toast;

import com.example.yoke.R;
import com.yoke.Helper.MainApp;
import com.yoke.activities.tutorial.TutorialActivity;
import com.yoke.connection.Message;
import com.yoke.connection.messages.OpenURLCmd;
import com.yoke.connection.Connection;
import com.yoke.connection.client.MultiClientConnection;

import yuku.ambilwarna.AmbilWarnaDialog;

public class SettingsFragment extends PreferenceFragmentCompat {

    public static SharedPreferences preferences;
    AmbilWarnaDialog colorPicker;
    int colorPrimary;
    SharedPreferences.Editor editor;

    Preference language;
    Preference color;
    //the following preference, when checked goes back to the main colour scheme
    CheckBoxPreference mainColor;
    //for future versions
    Preference connection;
    Preference tutorial;
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

        //sets the value of the preference variables to their corresponding preferences from preferences.xml
        language = findPreference("language");
        color = findPreference("color");
        mainColor = (CheckBoxPreference)findPreference("primary");
        connection = findPreference("connection");
        tutorial = findPreference("tutorial");
        about = findPreference("about");

        setOnClickListenerLanguage();
        setOnClickListenerColor();
        setOnClickListenerMainColor();
        setOnClickListenerTutorial();
        setOnClickListenerAbout();

    }

    /** Set up for the language preference - Creates language preference if it does not exist yet,
     *  sets summary to current language
     * @pre true
     * @modifies {@code preferences.getString("language");
     *                  language.getSummary()}
     * @returns void
     * @post {@code preferences.contains("language");
     *              language.getSummary() <> null;
     *              language.getSummary().equals(currentLanguage) }
     */
    private void setUpLanguage() {
        if (!preferences.contains("language")) {
            editor.putString("language", "en");
            editor.apply();
            setLanguageSummary(language, "en");
        }

        setLanguageSummary(language, preferences.getString("language", "default"));
    }

    /** Sets an OnClickListener on language preference, that updates preference value
     * and application language
     * @pre {@code language <> null} (setUpLanguage ensures it holds - no need to check here)
     * @modifies onPreferenceChange() method
     * @returns void
     * @post {@code preferences.getString("language").equals(o.toString())
     *              locale.getLanguage().equals(o.toString)}
     */
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

    /** Updates language
     * @pre {@code language <> null}
     * @param language - new language
     * @returns void
     * @post locale.getLanguage().equals(o.toString())
     * @throws NullPointerException if language == null
     */
    private void setNewLocale(String language) {
        if (language == null) {
            throw new NullPointerException("language cannot be null");
        }
        //call to the app's localeManager
        MainApp.localeManager.setNewLocale(getContext(), language);
        resetActivity();
    }

    /** Sets the summary for the Language Preference depending on the current language value
     * @pre {@code language <> null && (newLang.equals("en") || newLang.equals("nl") || newLang.equals("bg"))}
     * @param language preference
     * @param newLang new language string
     * @throws NullPointerException if language == null
     * @throws IllegalArgumentException if {@code !(newLang.equals("en") || newLang.equals("nl") || newLang.equals("bg"))}
     */
    private void setLanguageSummary(Preference language, String newLang) {
        if (language == null) {
            throw new IllegalArgumentException("laguage cannot be null");
        } else if(newLang.equals("en")) {
            language.setSummary("English (default)");
        } else if (newLang.equals("nl")) {
            language.setSummary("Nederlands");
        } else if (newLang.equals("bg")){
            language.setSummary("Български");
        } else {
            throw new IllegalArgumentException("This language is not supported yet");
        }
    }

    /** Makes sure the color preference exists
     * @pre true
     * @modifies preferences
     * @post {@code preferences.contains("color")}
     */
    private void setUpColor() {
        colorPrimary = ContextCompat.getColor(getContext(), R.color.colorPrimary);
        if (!preferences.contains("color")) {
            System.out.println("No color set");
            editor.putInt("color", colorPrimary);
            editor.apply();
        }
    }

    /** Sets an OnClickListener on color preference that updates preference value
     * @pre {@code color <> null} (setUpColor ensures it holds - no need to check here)
     * @modifies onPreferenceClick() method && color
     * @returns false
     * @post {@code preferences.getString("color") == color)}
     */
    private void setOnClickListenerColor() {
        setUpColor();
        int currentColor = preferences.getInt("color", 0);
        color.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                colorPicker = new AmbilWarnaDialog(getContext(), currentColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
                    @Override
                    public void onOk(AmbilWarnaDialog dialog, int color) {
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

    /** Makes sure default preference exists
     * @pre {@code mainColor <> null}
     * @modifies preferences
     * @returns void
     * @post {@code preferences.contains("default")}
     * @throws NullPointerException if {@code mainColor == null}
     */
    private void setUpMainColor() {
        if (mainColor == null) {
            throw new NullPointerException("mainColor cannot be null");
        }
        mainColor.setDefaultValue(true);
        if (!preferences.contains("default")) {
            editor.putBoolean("default", true);
            editor.apply();
            mainColor.setChecked(true);
        }

        //makes sure the checkbox doesn't behave in an unexpected way
        if (mainColor.isChecked()) {
            mainColor.setEnabled(false);
        }
    }

    /** Sets an OnClickListener on default preference that updates preference value;
     * Sets the colour theme to the default one
     * @pre {@code mainColor <> null} (setUpMainColor ensures it holds - no need to check here)
     * @modifies onPreferenceChange() method && default
     * @returns true
     * @post {@code preferences.getBoolean("default") == true
     *              preferences.getInt("color") == colorPrimary}
     */
    private void setOnClickListenerMainColor() {
        setUpMainColor();
        mainColor.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                editor.putBoolean("default", (Boolean) o).apply();
                editor.putInt("color", colorPrimary).apply();
                resetActivity();
                return true;
            }
        });
    }

    //for future versions
    /** Sets an onClickListener on the connection preference that updates the preference's value
     * @pre {@code connection <> null}
     * @modifies onPreferenceChange() method and connection
     * @returns true
     * @post {@code preferences.getString("connection").equals(o.toString())}
     * @throws NullPointerException if connection == null
     */
    private void setOnClickListenerConnection() {
        if (connection == null) {
            throw new NullPointerException("connection cannot be null");
        }
        connection.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                editor.putString("connection", o.toString());
                editor.apply();

                return true;
            }
        });

    }

   /** Sets an onClickListener on tutorial that starts the tutorial activity
     * @pre {@code tutorial <> null}
     * @modifies onPreferenceClick() method
     * @returns true
     * @post tutorial activity is shown to the user
     * @throws NullPointerException if tutorial == null
     */
   private void setOnClickListenerTutorial() {
       if (tutorial == null) {
           throw new NullPointerException("tutorial preference cannot be null");
       }
       tutorial.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
           @Override
           public boolean onPreferenceClick(Preference preference) {
               Intent i = new Intent(getContext(), TutorialActivity.class);
               startActivity(i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
               return true;
           }
       });

   }

    /** Sets an onClickListener on about that links to our README.md
     * @pre {@code about <> null}
     * @modifies onPreferenceClick() method
     * @returns true
     * @post a message is sent to computer that opens a browser showing out README.md
     * @throws NullPointerException if about == null
     */
    private void setOnClickListenerAbout() {
        if (about == null) {
            throw new NullPointerException("about cannot be null");
        }
        about.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                MultiClientConnection connection = MultiClientConnection.getInstance(getContext());
                Message readMe = new OpenURLCmd("https://github.com/TarVK/Yoke/blob/master/README.md");
                //ensures the connection is active
                if (connection.getState() == Connection.CONNECTED) {
                    connection.send(readMe);
                } else {
                    Toast.makeText(getContext(), "Command could not be sent. " + "\n Please make sure you are connected", Toast.LENGTH_LONG).show();
                }
                return true;
            }
        });

    }

    /** Restarts activity - needed so that changes in settings can be observed
     * @post settings activity is restarted
     */
    private void resetActivity() {
         Intent i = new Intent(getContext(), Settings.class);
         startActivity(i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
    }


}
