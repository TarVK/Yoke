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
import com.yoke.database.types.Button;
import com.yoke.database.types.Profile;

import java.util.ArrayList;
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
        Toolbar toolbar = findViewById(R.id.toolbarEdit);
        textView = (TextView) findViewById(R.id.profileEditTextView);

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

        ImageButton add = findViewById(R.id.addMacro);
        ImageButton delete = findViewById(R.id.deleteMacro);
        ImageButton done = findViewById(R.id.doneEdit);

        //add a new macro, it should direct to the macro activity
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (profile.hasSpace()) {

                } else {
                    Toast.makeText(getApplicationContext(),"cant be added", Toast.LENGTH_LONG).show();
                }
            }
        });

        //delete a selected macro
        delete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int index = mButton.indexOf(selectedButton);

                profile.removeButton(selectedButton);
//                selectedButton.delete();
                deletedButtons.add(selectedButton);
                mButton.remove(selectedButton);
                Log.w(TAG, "onClick: " + index);

                adapter.notifyItemRangeChanged(index, mButton.size());
                adapter.notifyItemRemoved(index);
//                adapter.notifyDataSetChanged();

            }
        });

        //finish edit
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (byte i = 0; i < mButton.size(); i++ ) {
                    mButton.get(i).setIndex(i);

                }

                for (com.yoke.database.types.Button button : deletedButtons) {
                    button.delete();
                }

                profile.save(()->{
                    runOnUiThread(()-> {
                        Intent intent = new Intent(getApplicationContext(), AA_Profile.class);
                        intent.putExtra("profile id", profile.getID());
                        startActivity(intent);
                    });

                });

            }
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
            mButton.sort(new Comparator<Button>() {
                @Override
                public int compare(Button o1, Button o2) {
                    return o1.getIndex() - o2.getIndex();
                }
            });
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