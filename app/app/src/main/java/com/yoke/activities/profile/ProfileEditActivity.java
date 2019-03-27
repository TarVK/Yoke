package com.yoke.activities.profile;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.example.yoke.R;
import com.yoke.activities.macro.MacroActivity;
import com.yoke.database.types.Button;
import com.yoke.database.types.Profile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProfileEditActivity extends AppCompatActivity implements StartDragListener {

    private static final String TAG = "ProfileEditActivity";


    private List<com.yoke.database.types.Button> mButton;
    private com.yoke.database.types.Button selectedButton;
    private Profile profile;
    private ArrayList<com.yoke.database.types.Button> deletedButtons = new ArrayList<>();
    RecyclerViewAdapterEdit adapter;
    private EditText textView;

    boolean isActivated;
    boolean isLandscape;
    boolean hasSpace;


    ItemTouchHelper touchHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);
        Toolbar toolbar = findViewById(R.id.toolbarEditView);
        textView = (EditText) findViewById(R.id.profileEditTextView);

        isLandscape =
                getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
//        if (isLandscape) { //TODO remove unnecessary code?
//            toolbar.setVisibility(View.GONE);
//        }
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Log.d(TAG, "onCreate: edit started");

        retrieveProfileData();

        // Create new macro
        findViewById(R.id.addMacro)
                .setOnClickListener(addView -> {
                    if (false) { //TODO hasSpace
//                        Intent intent = new Intent(getApplicationContext(), MacroSelector.class); //TODO add selection activity
//                        intent.putExtra("macro id", -1);
//                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(),"Profile has no space left!", Toast.LENGTH_LONG).show();
                    }
                });

        // Edit selected macro
        findViewById(R.id.addMacro)
                .setOnClickListener(deleteView -> {
            long macroID = selectedButton.getMacro().getID();
            Intent intent = new Intent(getApplicationContext(), MacroActivity.class);
            intent.putExtra("macro id", macroID);
            startActivity(intent);
        });

        // Delete selected macro
        findViewById(R.id.deleteMacro)
                .setOnClickListener(deleteView -> {
            int macroID = (int) selectedButton.getMacro().getID(); //TODO check if (int) implementation works

            profile.removeButton(selectedButton);
//                selectedButton.delete(); //TODO remove unnecessary code?
            deletedButtons.add(selectedButton);
            mButton.remove(selectedButton);
            adapter.notifyItemRangeChanged(macroID, mButton.size());
            adapter.notifyItemRemoved(macroID);
//                adapter.notifyDataSetChanged(); //TODO remove unnecessary code?

        });

        // Finish edit
        findViewById(R.id.doneMacro).setOnClickListener(v -> {
            for (byte i = 0; i < mButton.size(); i++) {
                mButton.get(i).setIndex(i);
            }

            for (Button button : deletedButtons) {
                button.delete();
            }

            profile.setName(textView.getText().toString());

            profile.save(() -> {
                runOnUiThread(() -> {
                    Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                    intent.putExtra("profile id", profile.getID());
                    startActivity(intent);
                    finish();
                    onBackPressed();
                });
            });
        });
    }

    //TODO still retrieves the data on edit page as well
    public void retrieveProfileData() {
        Long profileID = getIntent().getLongExtra("profile id", 0);
        Log.w(TAG, "retrieveData: " + profileID);

        Profile.getByID(profileID, (profile)-> {
            this.profile = profile;

            textView.setText(profile.getName());
            mButton = profile.getButtons();
            hasSpace = profile.hasSpace();

            //sort the buttons so they are in order and displayed in a correct order on the layout
            Collections.sort(mButton, (o1, o2) -> o1.getIndex() - o2.getIndex());

            profile.getName();
            profile.getIndex();

            myRecyclerView();
        });

    }

    private void myRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view_edit);
        adapter = new RecyclerViewAdapterEdit(ProfileEditActivity.this, mButton, this, recyclerView);

        //TODO looper issue needs to be fixed
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ItemTouchHelper.Callback callback = new ItemMoveCallback(adapter);
                touchHelper = new ItemTouchHelper(callback);
                touchHelper.attachToRecyclerView(recyclerView);
                recyclerView.setAdapter(adapter);
            }
        });
        int columns = 2;
        if (isLandscape) {
            columns = 3;
        }

        recyclerView.setLayoutManager(new GridLayoutManager(this, columns));

        isActivated = adapter.canDelete();
    }




    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
        intent.putExtra("profile id", profile.getID());
        startActivity(intent);
        finish();
        return true;
    }

    @Override
    public void requestDrag(RecyclerView.ViewHolder viewHolder) {
        touchHelper.startDrag(viewHolder);
    }

    public void selectButton(com.yoke.database.types.Button button) {
        this.selectedButton = button;

    }

    public com.yoke.database.types.Button getSelectedButton() {
        return selectedButton;
    }






}
