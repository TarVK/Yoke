package com.yoke.activities.home;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.yoke.R;

import java.util.ArrayList;

import com.yoke.activities.profileEdit.ProfileEditActivity;
import com.yoke.database.types.Profile;

public class HomeActivity extends AppCompatActivity {
    FloatingActionButton button;
    ImageView settings;
    Toolbar toolbar;

    private ArrayList<Profile> mDataset = new ArrayList<>();
    RecyclerView recyclerView;
    MyAdapter adapter;
    ArrayList<Profile> profiles = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        recyclerView = findViewById(R.id.recyclerView);

        button = findViewById(R.id.createProfile);
        settings = findViewById(R.id.settingsButton);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        adapter = new MyAdapter(profiles, this);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        Context mContext = this; //TODO add fab implementation and replace with extended text fab
        button.setOnClickListener(v -> {
            Profile profile = new Profile("Some Cool {Profile");
            profile.save(() -> {
                long ID = profile.getID();
                Log.w("COOL ID ", ID+"");
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

    // Initialize the RecyclerView
    private void initRecyclerView() {
        final RecyclerView recyclerView = findViewById(R.id.recyclerView);
        Profile.getAll(retrievedProfiles -> {
            runOnUiThread(() -> {
                profiles.clear();
                profiles.addAll(retrievedProfiles);
                adapter.notifyDataSetChanged();
            });
        });

    }

    //method to get all profiles.
   /* private void getProfiles() {
        Profile.getAll(new DataObject.DataCallback<List<Profile>>(){
            public void retrieve(List<Profile> profiles) {

            }
        });
    }*/


    @Override
    public void onResume() {
        super.onResume();

        // Make sure to refresh the data
        initRecyclerView();
    }
}
