package com.yoke.activities.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.widget.ImageView;

import com.example.yoke.R;
import com.yoke.activities.BaseActivity;
import com.yoke.activities.motionDetector.ShakeService;
import com.yoke.activities.profileEdit.ProfileEditActivity;
import com.yoke.activities.settings.Settings;
import com.yoke.database.types.Profile;

import java.util.ArrayList;
import java.util.Collections;

public class HomeActivity extends BaseActivity {
    FloatingActionButton button; // The add profile button
    ImageView settings; // The settings icon
    Toolbar toolbar; // The toolbar that contains some of the buttons

    RecyclerView recyclerView; // The RecyclerView that contains a list of profiles
    HomeAdapter adapter; // The adapter of the RecyclerView
    ArrayList<Profile> profiles = new ArrayList<>(); // The list that contains all the profiles

    DragController dragController; // The dragController
    ItemTouchHelper dragHelper; // The dragHelper
    boolean orderChanged = false; // A boolean value to check if the order is changed

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        recyclerView = findViewById(R.id.recyclerView);

        // Add all ids to their variable
        button = findViewById(R.id.createProfile);
        this.setNewThemeColour(button);
        settings = findViewById(R.id.settingsButton);
        toolbar = findViewById(R.id.toolbar);
        this.setNewThemeColour(toolbar);
        setSupportActionBar(toolbar);

        Intent shake = new Intent(this, ShakeService.class);
        startService(shake);

        // Set up the adapter and the RecyclerView
        adapter = new HomeAdapter(profiles, this);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        // Add a swipe adapter
        ItemTouchHelper.Callback swipeController = new SwipeController(this);
        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
        itemTouchhelper.attachToRecyclerView(recyclerView);

        // Add drag adapter
        dragController = new DragController(adapter);
        dragHelper = new ItemTouchHelper(dragController);
        dragHelper.attachToRecyclerView(recyclerView);

        // onClickListener for the add profile button
        Context mContext = this;
        button.setOnClickListener(v -> {
            Profile profile = new Profile("Some Cool Profile");
            profile.setIndex(profiles.size());
            profile.save(this, () -> {
                long ID = profile.getID();
                Intent intent = new Intent(mContext, ProfileEditActivity.class);
                intent.putExtra("profile id", ID);
                mContext.startActivity(intent);
            });
        });

        // onClickListener for the settings button
        settings.setOnClickListener(settingsView -> {
            startActivity(new Intent(getApplicationContext(), Settings.class));
        });

        initData();
    }
    // initialize the data for the RecyclerView
    public void initData() {
        initRecyclerView();
    }

    /**
     * Initializes the data for the RecyclerView
     */
    private void initRecyclerView() {
        final RecyclerView recyclerView = findViewById(R.id.recyclerView);
        Profile.getAll(this, retrievedProfiles -> {
            runOnUiThread(() -> {
                profiles.clear();
                profiles.addAll(retrievedProfiles);

                // Sort the profiles by index
                Collections.sort(profiles, (p1, p2) -> p1.getIndex() - p2.getIndex());

                adapter.notifyDataSetChanged();
            });
        });

    }

    @Override
    protected void onPause() {
        super.onPause();

        // If the order is Changed rearrange it into the right order
        if (orderChanged) {
            orderChanged = false;
            for (int i = 0; i < profiles.size(); i++) {
                Profile profile = profiles.get(i);
                profile.setIndex(i);
                profile.save(this);
            }
        }


    }

    @Override
    public void onResume() {
        super.onResume();

        // Make sure to refresh the data
        initRecyclerView();
    }


}
