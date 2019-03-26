package com.yoke.activities.home;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.yoke.R;

import java.util.ArrayList;

import com.example.yoke.R;
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
    private ArrayList<String> mDataset = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.createProfile);
        settings = findViewById(R.id.settingsButton);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(getApplicationContext(), MacroSelection.class));
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
        mDataset.add("test1");
        mDataset.add("test2");
        mDataset.add("test3");
        initRecyclerView();
    }

    // Initialize the RecyclerView
    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        RecyclerView.Adapter mAdapter = new MyAdapter(mDataset, this);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    //method to get all profiles.
    /*private void getProfiles() {
        Profile.getAll(new DataObject.DataCallback<List<Profile>>(){
            public void retrieve(List<Profile> profiles) {

            }
        });
    }*/
}