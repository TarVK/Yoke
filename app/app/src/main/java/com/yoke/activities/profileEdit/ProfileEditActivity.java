package com.yoke.activities.profileEdit;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yoke.R;
import com.yoke.activities.macro.MacroActivity;
import com.yoke.activities.macro.select.MacroSelection;
import com.yoke.activities.profile.ProfileActivity;
import com.yoke.database.types.Profile;
import com.yoke.utils.Callback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProfileEditActivity extends AppCompatActivity implements StartDragListener {

    private static final String TAG = "ProfileEditActivity";


    private List<com.yoke.database.types.Button> mButton;
    private com.yoke.database.types.Button selectedButton;
    private Profile profile;
    private ArrayList<com.yoke.database.types.Button> deletedButtons = new ArrayList<>();
    ButtonsEditRecyclerViewAdapter adapter;

    private EditText profileName;
    private EditText associatedPrograms;
    private ImageView addMacro;
    private ImageView editMacro;
    private ImageView deleteMacro;
    private ImageView doneMacro;


    boolean isActivated;
    boolean isLandscape;

    ItemTouchHelper touchHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);
        Toolbar toolbar = findViewById(R.id.toolbarEdit);

        profileName = (EditText) findViewById(R.id.profileEditTextView);
        associatedPrograms = (EditText) findViewById(R.id.associatedPrograms);
        addMacro = (ImageView) findViewById(R.id.addMacro);
        editMacro = (ImageView) findViewById(R.id.editMacro);
        deleteMacro = (ImageView) findViewById(R.id.deleteMacro);
        doneMacro = (ImageView) findViewById(R.id.doneMacro);

        isLandscape =
                getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
//        if (isLandscape) {
//            toolbar.setVisibility(View.GONE);
//        }
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Log.d(TAG, "onCreate: edit started");

        retrieveProfileData();

        // Add a new Macro -> MacroSelection.java
        addMacro.setOnClickListener(v -> {
            if (profile.hasSpace()) {
                // Save the profile before continuing
                // TODO: add prompt asking whether you are sure you want to asve the profile
                saveProfile(()->{
                    Intent intent = new Intent(getApplicationContext(), MacroSelection.class);
                    intent.putExtra("profile id", profile.getID());
                    startActivity(intent);
                });
            } else {
                Toast.makeText(getApplicationContext(),"cant be added", Toast.LENGTH_LONG).show();
            }
        });

        // Edit selected Macro -> MacroActivity.java
        editMacro.setOnClickListener(v -> {
            long macroID = selectedButton.getMacro().getID();

            // Save the profile before continuing
            // TODO: add prompt asking whether you are sure you want to asve the profile
            saveProfile(()->{
                Intent intent = new Intent(getApplicationContext(), MacroActivity.class);
                intent.putExtra("macro id", macroID);
                intent.putExtra("profile id", profile.getID());
                startActivity(intent);
            });
        });

        // Delete selected Macro
        deleteMacro.setOnClickListener(v -> {
            int index = mButton.indexOf(selectedButton);

            profile.removeButton(selectedButton);
//            selectedButton.delete();
            deletedButtons.add(selectedButton);
            mButton.remove(selectedButton);
            Log.w(TAG, "onClick: " + index);

            adapter.notifyItemRangeChanged(index, mButton.size());
            adapter.notifyItemRemoved(index);
//                adapter.notifyDataSetChanged();

        });

        // Finish editing profile -> ProfileActivity.java
        doneMacro.setOnClickListener(v -> {
            saveProfile(()-> {
                onBackPressed();
            });
        });

        // Hide keyboard after done
        associatedPrograms.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    InputMethodManager imm = (InputMethodManager)v.getContext()
                            .getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    associatedPrograms.clearFocus();
                }
                return false;
            }
        });
    }

    /**
     * Puts all local activity data into the opened profile
     * @param callback  The callback that will be called once the profile is saved
     */
    protected void saveProfile(Runnable callback) {
        // Update the index of all buttons
        for (byte i = 0; i < mButton.size(); i++ ) {
            mButton.get(i).setIndex(i);
        }

        // Delete any deleted buttons permanently
        for (com.yoke.database.types.Button button : deletedButtons) {
            button.delete();
        }

        // Update the name and associated programs
        profile.setName(profileName.getText().toString());
        profile.setAssociatedPrograms(associatedPrograms.getText().toString());

        // Save the profile and run callback in UI thread
        profile.save(()->{
            runOnUiThread(callback);
        });
    }

    //still retrieves the data on edit page as well
    public void retrieveProfileData() {
        Long profileID = getIntent().getLongExtra("profile id", 0);
        Log.w(TAG, "retrieveData: " + profileID);

        Profile.getByID(profileID, (profile)-> {
            runOnUiThread(() -> {
                profileName.setText(profile.getName());
                associatedPrograms.setText(profile.getAssociatedPrograms());
                mButton = (profile.getButtons());
                this.profile = profile;

                //sort the buttons so they are in order and displayed in a correct order on the layout
                Collections.sort(mButton, (o1, o2) -> o1.getIndex() - o2.getIndex());

                initializeRecyclerView();
            });
        });

    }

    /**
     * Initializes the recycler view
     */
    private void initializeRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view_edit);
        adapter = new ButtonsEditRecyclerViewAdapter(ProfileEditActivity.this, mButton, this, recyclerView);

        //looper issue needs to be fixed
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
