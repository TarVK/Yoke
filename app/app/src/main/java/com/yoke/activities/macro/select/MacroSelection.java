package com.yoke.activities.macro.select;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;

import com.example.yoke.R;
import com.yoke.database.types.Macro;

import java.util.ArrayList;

public class MacroSelection extends AppCompatActivity {

    private static final String TAG = "`MacroSelection";

    private ArrayList<Macro> mDataset = new ArrayList<>();
    RecyclerView recyclerView;
    MacroSelectionAdapter adapter;
    ArrayList<Macro> macros = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_macro_selection);
        recyclerView = findViewById(R.id.recyclerView);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Long profileID = getIntent().getLongExtra("profile id", 0);

        adapter = new MacroSelectionAdapter(macros, this, profileID);
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

        Macro.getAll(retrievedMacros -> {
            runOnUiThread(() -> {
                macros.clear();
                macros.addAll(retrievedMacros);
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
