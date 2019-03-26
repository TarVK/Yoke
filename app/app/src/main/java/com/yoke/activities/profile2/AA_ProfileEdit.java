package com.yoke.activities.profile2;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yoke.R;
import com.yoke.activities.macro.MacroBuilder;
import com.yoke.database.types.Button;
import com.yoke.database.types.Profile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AA_ProfileEdit extends AppCompatActivity implements StartDragListener {

    private static final String TAG = "AA_ProfileEdit";


    private List<com.yoke.database.types.Button> mButton;
    private com.yoke.database.types.Button selectedButton;
    private Profile profile;
    private ArrayList<com.yoke.database.types.Button> deletedButtons = new ArrayList<>();
    RecyclerViewAdapterEdit adapter;
    private TextView textView;

    boolean isActivated;
    boolean isLandscape;

    ItemTouchHelper touchHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aa_profile_edit);
        Toolbar toolbar = findViewById(R.id.toolbarEditView);
        textView = (TextView) findViewById(R.id.profileEditText);

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

        // Delete selected macro //TODO add confirmation dialog
        findViewById(R.id.editMacro)
                .setOnClickListener(deleteView -> {
                    int index = mButton.indexOf(selectedButton);
                    Log.w(TAG, "onClick: " + index);

                    Intent intent = new Intent(getApplicationContext(), MacroBuilder.class);
                    intent.putExtra("macro id", index);
                    startActivity(intent);

//                    adapter.notifyItemRangeChanged(index, mButton.size());
//                    adapter.notifyItemRemoved(index);
//                adapter.notifyDataSetChanged();

                });

        // Delete selected macro
        findViewById(R.id.deleteMacro)
                .setOnClickListener(deleteView -> {
            int index = mButton.indexOf(selectedButton);

            profile.removeButton(selectedButton);
//                selectedButton.delete();
            deletedButtons.add(selectedButton);
            mButton.remove(selectedButton);
            Log.w(TAG, "onClick: " + index);

            adapter.notifyItemRangeChanged(index, mButton.size());
            adapter.notifyItemRemoved(index);
//                adapter.notifyDataSetChanged();

        });

        //finish edit //TODO remove for redundant back button
        findViewById(R.id.doneMacro)
                .setOnClickListener(completedView -> {
            for (byte i = 0; i < mButton.size(); i++ ) {
                mButton.get(i).setIndex(i);
            }
            for (Button button : deletedButtons) {
                button.delete();
            }

            profile.save(() -> runOnUiThread(() -> {
                Intent intent = new Intent(getApplicationContext(), AA_Profile.class);
                intent.putExtra("profile id", profile.getID());
                startActivity(intent);
                finish();
            }));

        });
    }

    //still retrieves the data on edit page as well
    public void retrieveProfileData() {
        Long profileId = getIntent().getLongExtra("profile id", 0);
        Log.w(TAG, "retrieveData: " + profileId);

        Profile.getByID(profileId, (profile)-> {
            textView.setText(profile.getName());
            mButton = (profile.getButtons());
            this.profile = profile;

            //sort the buttons so they are in order and displayed in a correct order on the layout
            Collections.sort(mButton, (o1, o2) -> o1.getIndex() - o2.getIndex());

            profile.getName();
            profile.getIndex();

            myRecyclerView();
        });

    }

    private void myRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view_edit);
        adapter = new RecyclerViewAdapterEdit(AA_ProfileEdit.this, mButton, this, recyclerView);

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
        Intent intent = new Intent(getApplicationContext(), AA_Profile.class);
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
