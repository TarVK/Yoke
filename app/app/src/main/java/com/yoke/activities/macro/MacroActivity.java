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

import com.example.yoke.R;
import com.yoke.activities.macro.tabs.MacroAppearance;
import com.yoke.activities.macro.tabs.MacroSequence;
import com.yoke.activities.profile.ProfileActivity;
import com.yoke.database.types.Macro;
import com.yoke.database.types.Profile;

import java.util.ArrayList;
import java.util.List;

public class MacroActivity extends AppCompatActivity {

    private static final String TAG = "MacroActivity";

    private TabLayout tabLayout;
    private ViewPager viewPager;

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

        // Finish edit
        findViewById(R.id.finishEdit).setOnClickListener(v -> {
            Macro.getByID(macroID, (macro) -> {
                macro.save(() -> {
                    runOnUiThread(() -> {
                        Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                        intent.putExtra("profile id", profileID);
                        startActivity(intent);
                        finish();
                        onBackPressed();
                    });
                });
            });
        });
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

    public void exitBuilder() {
        //TODO Save and store data

//        Intent intent = new Intent(this, ProfileEditActivity.class);
//        startActivity(intent);
        onBackPressed();
    }

}
