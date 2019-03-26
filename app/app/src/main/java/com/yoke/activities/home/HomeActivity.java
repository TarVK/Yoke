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
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.yoke.R;

import java.util.ArrayList;
import java.util.List;

import com.example.yoke.R;
import com.yoke.activities.profile2.AA_Profile;
import com.yoke.activities.profile2.AA_ProfileEdit;
import com.yoke.connection.CompoundMessage;
import com.yoke.connection.Connection;
import com.yoke.connection.Message;
import com.yoke.connection.MessageReceiver;
import com.yoke.connection.client.MultiClientConnection;
import com.yoke.connection.client.types.BluetoothClientConnection;
import com.yoke.connection.messages.OpenURLCmd;
import com.yoke.connection.messages.computerCmds.NextTrackCmd;
import com.yoke.connection.messages.computerCmds.PlayPauseCmd;
import com.yoke.connection.messages.computerCmds.ShutDownCmd;
import com.yoke.connection.messages.computerCmds.SleepCmd;
import com.yoke.database.DataBase;
import com.yoke.database.DataObject;
import com.yoke.database.types.Macro;
import com.yoke.database.types.Profile;
import com.yoke.database.types.Settings;

public class HomeActivity extends AppCompatActivity {
    FloatingActionButton button;
    ImageButton settings;
    Toolbar toolbar;
    private ArrayList<Profile> mDataset = new ArrayList<>();
    RecyclerView recyclerView2;
    MyAdapter adapter;
    ArrayList<Profile> profiles = new ArrayList<Profile>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        recyclerView2 = findViewById(R.id.recyclerView);

        button = findViewById(R.id.createProfile);
        settings = findViewById(R.id.settingsButton);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        adapter = new MyAdapter(profiles, this);
        recyclerView2.setAdapter(adapter);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView2.setLayoutManager(llm);

        Context mContext = this;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Profile profile = new Profile("Some Cool {Profile");
                profile.save(() -> {
                    long ID = profile.getID();
                    Log.w("COOL ID ", ID+"");
                    Intent intent = new Intent(mContext, AA_ProfileEdit.class);
                    intent.putExtra("profile id", ID);
                    mContext.startActivity(intent);
                });
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(getApplicationContext(), Settings.class));
            }
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
}
