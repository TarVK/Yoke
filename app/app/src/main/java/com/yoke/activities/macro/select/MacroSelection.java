package com.yoke.activities.macro.select;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;

import com.example.yoke.R;
import com.yoke.activities.BaseActivity;
import com.yoke.activities.macro.MacroActivity;
import com.yoke.database.types.Button;
import com.yoke.database.types.Macro;
import com.yoke.database.types.Profile;

import java.util.ArrayList;

public class MacroSelection extends BaseActivity {

    private static final String TAG = "`MacroSelection";

    private Long profileID;

    private ArrayList<Button> mDataset = new ArrayList<>();
    RecyclerView recyclerView;
    FloatingActionButton fabMacro;
    MacroSelectionAdapter adapter;
    ArrayList<Button> buttons = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_macro_selection);
        recyclerView = findViewById(R.id.recyclerView);
        fabMacro = findViewById(R.id.createMacro);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        profileID = getIntent().getLongExtra("profile id", 0);

        adapter = new MacroSelectionAdapter(buttons, this, profileID);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        initData();
    }

    // Public method to initialize the RecyclerView
    public void initData() {
        initRecyclerView();
    }

    // Initialize the RecyclerView
    private void initRecyclerView() {
        final RecyclerView recyclerView = findViewById(R.id.recyclerView);

        fabMacro.setOnClickListener(view -> {
            // Create new macro
            Macro newMacro = new Macro("Untitled");
            Button newButton = new Button(newMacro);

            Profile.getByID(profileID, (profile) -> {
                profile.addButton(newButton);
                newMacro.save(() -> {
                    profile.save(() -> {
                        Log.d(TAG, "FAB: create new Macro, mID: " + newMacro.getID());

                        Intent intent = new Intent(this, MacroActivity.class);
                        intent.putExtra("profile id", profileID);
                        intent.putExtra("macro id", newMacro.getID());
                        startActivity(intent);
                    });
                });
            });
        });

        Button.getAll(retrievedButtons -> {
            runOnUiThread(() -> {
                buttons.clear();
                buttons.addAll(retrievedButtons);
                adapter.notifyDataSetChanged();
            });
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        // Refresh data
        initRecyclerView();
    }

}
