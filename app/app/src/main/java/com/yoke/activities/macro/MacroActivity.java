package com.yoke.activities.macro;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.example.yoke.R;
import com.yoke.activities.macro.tabs.MacroAppearance;
import com.yoke.activities.macro.tabs.MacroSequence;
import com.yoke.activities.profile.ProfileActivity;
import com.yoke.activities.profileEdit.ProfileEditActivity;
import com.yoke.database.types.Macro;
import com.yoke.database.types.Profile;
import com.yoke.utils.Callback;

import java.util.ArrayList;
import java.util.List;

public class MacroActivity extends AppCompatActivity {

    private static final String TAG = "MacroActivity";

    private TabLayout tabLayout;
    private ViewPager viewPager;

    private Long macroID;
    public Macro macro;
    private List<Callback> callbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_macro);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        Long profileID = getIntent().getLongExtra("profile id", 0);
        Long macroID = getIntent().getLongExtra("macro id", 0);
        Log.w(TAG, "retrieveData: mID: " + macroID + ", pID: " + profileID);

        EditText macroName = findViewById(R.id.macroName);

        Macro.getByID(macroID, (macro) -> {
            String name = macro.getName();
            macroName.setText(name);
        });

        // Finish edit
        findViewById(R.id.finishEdit).setOnClickListener(v -> {
            macro.setName(macroName.getText().toString());
            macro.save(() -> {
                runOnUiThread(() -> {
                    Intent intent = new Intent(getApplicationContext(), ProfileEditActivity.class);
                    intent.putExtra("profile id", profileID);
                    if (getIntent().getBooleanExtra("isNewMacro", false)) {
                        intent.putExtra("macro id", macro.getID());
                    }
                    startActivity(intent);
                    finish();
                    onBackPressed();
                });
            });
        });
    }


    /**
     * Retrieves the macro and stores it
     * @param callback  Gets called once the macro has been loaded
     */
    public void loadMacro(Callback callback) {
        // first check if the macro hasn't been loaded already
        if (macro != null) {
            callback.call();
            return;
        }

        // Only run this method once
        if (callbacks == null) {
            callbacks = new ArrayList<>();
            macroID = getIntent().getLongExtra("macro id", -1);
            Log.w(TAG, "retrieveData: " + macroID);

            // Check macroID
            if (macroID == -1) {
                Log.e(TAG, "MacroActivity has not been called properly: " + macroID);
                return;
            }

            Macro.getByID(macroID, macro -> {
                runOnUiThread(() -> {
                    MacroActivity.this.macro = macro;

                    if (macro != null) {
                        for(Callback cb: callbacks) {
                            cb.call();
                        }
                    } else {
                        Log.e(TAG, "Macro is not initialized: " + macroID);
                    }
                });
            });
        }

        callbacks.add(callback);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new MacroAppearance(), "Appearance");
        adapter.addFragment(new MacroSequence(), "Sequence");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        // Since the profile activity finished, we can't just use back navigation
        Long profileID = getIntent().getLongExtra("profile id", 0);
        Intent intent = new Intent(getApplicationContext(), ProfileEditActivity.class);
        intent.putExtra("profile id", profileID);
        startActivity(intent);
        finish();

        return true;
    }
}
