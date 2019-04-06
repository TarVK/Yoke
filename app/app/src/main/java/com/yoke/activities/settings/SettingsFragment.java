package com.yoke.activities.settings;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.yoke.R;
import com.yoke.Helper.LocaleManager;
import com.yoke.Helper.MainApp;
import com.yoke.activities.MainActivity;
import com.yoke.activities.home.HomeActivity;
import com.yoke.activities.splash.SplashActivity;
import com.yoke.activities.tutorial.TutorialActivity;

public class SettingsFragment extends PreferenceFragmentCompat {

    //public static String preferences;
    public static SharedPreferences preferences;
//    LocaleManager localeManager;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        localeManager = new LocaleManager(getContext());

        Resources res = getContext().getResources();
        android.content.res.Configuration conf = res.getConfiguration();

    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preferences);
        System.out.println("whatevs");
        if (getActivity().getSharedPreferences("preferences", Context.MODE_PRIVATE) == null) {
            preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        }
        preferences = getActivity().getSharedPreferences("preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        getPreferenceScreen().setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                if (preference.getKey() == "tutorial") {
                    startActivity(new Intent((getActivity()), TutorialActivity.class));
                } else if (preference.getKey() == "about") {
                    //github README.txt
                }

                return true;
            }
        });


        //Tutorial preference
        /*Preference tutorial = findPreference("tutorial");
        //System.out.println("===============================" + findPreference("tutorial"));
        tutorial.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override

            public boolean onPreferenceClick(Preference preference) {
                System.out.println("===============================" + findPreference("tutorial"));
                startActivity(new Intent(getActivity(), TutorialActivity.class));
                return true;
            }
        });*/

        /*Preference.OnPreferenceClickListener clickListener = new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                System.out.println("whatevs");
                return false;
            }
        };*/




        /*//About preference
        Preference about = findPreference("about");
        about.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                //link to github README.txt
                return true;
            }
        });*/

        //Language preference
        if (findPreference("language") == null) {
            editor.putString(getString(R.string.language), "en");
        }

        Preference language = findPreference("language");
        language.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                //TODO for this I need Locale
                editor.putString(getString(R.string.language), o.toString());
                System.out.println("=======================================================");
                System.out.println("Language: " + o.toString());
                language.setSummary(o.toString());
                setNewLocale(o.toString());
                /*if (o.toString().equals("english")) {
                    language.setSummary("English");
                } else if (o.toString().equals("bulg")) {
                    language.setSummary("Български");
                } else {
                    language.setSummary("Nederlands");
                }*/
                //LocaleHelper.setLocale(getContext(), o.toString());
                //updateView((String)findPreference("language").toString());
                //updateResources();
                return true;
            }
        });





        Preference connection = findPreference("connection");
        connection.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                //connection type stored as byte?
                //settingsData.setConnectionType(??);
                return true;
            }
        });

    }

    private void setNewLocale(String language) {
        MainApp.localeManager.setNewLocale(getContext(), language);

        Intent i = new Intent(getContext(), HomeActivity.class);
        startActivity(i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));

//        System.exit(0);
    }

    /*private void updateView(String language) {
        Context context = LocaleHelper.setLocale(getActivity().getApplicationContext(), language);

    }*/



}
