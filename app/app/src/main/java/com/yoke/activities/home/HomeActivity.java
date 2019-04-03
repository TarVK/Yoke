package com.yoke.activities.home;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.yoke.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.yoke.activities.BaseActivity;
import com.yoke.activities.profileEdit.ItemMoveCallback;
import com.yoke.activities.profileEdit.ProfileEditActivity;
import com.yoke.database.types.Profile;

public class HomeActivity extends BaseActivity {
    FloatingActionButton button;
    ImageView settings;
    Toolbar toolbar;

    private ArrayList<Profile> mDataset = new ArrayList<>();
    RecyclerView recyclerView;
    HomeAdapter adapter;
    ArrayList<Profile> profiles = new ArrayList<>();

    DragController dragController;
    ItemTouchHelper dragHelper;
    boolean orderChanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        recyclerView = findViewById(R.id.recyclerView);

        button = findViewById(R.id.createProfile);
        settings = findViewById(R.id.settingsButton);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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


        Context mContext = this;
        button.setOnClickListener(v -> {
            Profile profile = new Profile("Some Cool Profile");
            profile.setIndex(profiles.size());
            profile.save(() -> {
                long ID = profile.getID();
                Intent intent = new Intent(mContext, ProfileEditActivity.class);
                intent.putExtra("profile id", ID);
                mContext.startActivity(intent);
            });
        });

        settings.setOnClickListener(settingsView -> {
            //startActivity(new Intent(getApplicationContext(), Settings.class));
        });

        initData();
    }
    // initialize the date for the RecyclerView
    // This should be all the profiles
    public void initData() {
        initRecyclerView();
    }

    /**
     * Initializes the recycler view
     */
    private void initRecyclerView() {
        final RecyclerView recyclerView = findViewById(R.id.recyclerView);
        Profile.getAll(retrievedProfiles -> {
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

        if (orderChanged) {
            orderChanged = false;
            for (int i = 0; i < profiles.size(); i++) {
                Profile profile = profiles.get(i);
                profile.setIndex(i);
                profile.save();
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
