package com.yoke.activities.macro.select;

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
import com.yoke.activities.macro.MacroActivity;
import com.yoke.connection.Message;
import com.yoke.database.types.Button;
import com.yoke.database.types.Macro;
import com.yoke.database.types.Profile;

import java.util.ArrayList;

public class ActionSelection extends AppCompatActivity {

    private static final String TAG = "ActionSelection";

    private Long macroID;
    public ArrayList<Message> message = new ArrayList<>();

    RecyclerView recyclerView;
    FloatingActionButton fabAction;
    ActionSelectionAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_macro_selection);
        recyclerView = findViewById(R.id.recyclerView);
        fabAction = findViewById(R.id.createAction);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        macroID = getIntent().getLongExtra("macro id", 0);
        Macro.getByID(macroID, (macro) -> {
            message.add(macro.getAction());



            adapter = new ActionSelectionAdapter(message, this, macroID);
            recyclerView.setAdapter(adapter);
            LinearLayoutManager llm = new LinearLayoutManager(this);
            llm.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(llm);

            initData();
        });
    }

    // Public method to initialize the RecyclerView
    public void initData() {
        initRecyclerView();
    }

    // Initialize the RecyclerView
    private void initRecyclerView() {
        final RecyclerView recyclerView = findViewById(R.id.recyclerView);

        fabAction.setOnClickListener(view -> {
            // Create new action

        });


    }

    @Override
    public void onResume() {
        super.onResume();

        // Refresh data
        initRecyclerView();
    }

}
