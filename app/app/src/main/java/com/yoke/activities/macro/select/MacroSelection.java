package com.yoke.activities.macro.select;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yoke.R;
import com.yoke.activities.macro.MacroActivity;
import com.yoke.database.types.Button;
import com.yoke.database.types.Macro;
import com.yoke.database.types.Profile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;

public class MacroSelection extends AppCompatActivity {

    private static final String TAG = "MacroSelection";

    private Long profileID;

    RecyclerView recyclerView;
    FloatingActionButton fabMacro;
    EditText search;
    MacroSelectionAdapter adapter;

    ArrayList<Macro> allMacros = new ArrayList<>();
    ArrayList<Macro> macros = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_macro_selection);
        recyclerView = findViewById(R.id.recyclerView);
        search = findViewById(R.id.search);
        fabMacro = findViewById(R.id.createMacro);

        profileID = getIntent().getLongExtra("profile id", -1);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        // Initialise the adapter
        adapter = new MacroSelectionAdapter(macros, this, profileID);
        recyclerView.setAdapter(adapter);

        // Listen for new macros being created
        fabMacro.setOnClickListener(view -> {
            // Create new macro
            Macro newMacro = new Macro("Untitled");

            newMacro.save(this, () -> {
                Log.d(TAG, "FAB: create new Macro, mID: " + newMacro.getID());

                Intent intent = new Intent(this, MacroActivity.class);
                intent.putExtra("profile id", profileID);
                intent.putExtra("macro id", newMacro.getID());
                intent.putExtra("isNewMacro", true);
                startActivity(intent);
                finish();
            });
        });

        // Setup the search listener
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                filterMacros();
            }
        });


        // Hide keyboard after done
        search.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                InputMethodManager imm = (InputMethodManager)v.getContext()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                search.clearFocus();
            }
            return false;
        });

        initData();
    }

    /**
     * Filters the macros based on the search text
     */
    public void filterMacros() {
        String filter = search.getText().toString().toLowerCase();

        // Remove all currently visible macros
        macros.clear();

        // Check what macros should be visible
        if (filter.equals("")) {
            // All macros should be visible
            macros.addAll(allMacros);
        } else{
            for (Macro macro: allMacros) {
                String name = macro.getName().toLowerCase();

                // Add the macro if it should be visible
                if (name.replace(filter, "").length() != name.length()) {
                    macros.add(macro);
                }
            }
        }

        // Update the view
        adapter.notifyDataSetChanged();
    }

    /**
     * Retrieves all of the available macros
     */
    public void initData() {
        Macro.getAll(this, loadedMacros -> {
            runOnUiThread(() -> {
                allMacros.clear();
                allMacros.addAll(loadedMacros);
                Collections.sort(allMacros, (a, b) -> a.getName().compareTo(b.getName()));

                filterMacros();
            });
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        // Refresh data
        initData();
    }

}
