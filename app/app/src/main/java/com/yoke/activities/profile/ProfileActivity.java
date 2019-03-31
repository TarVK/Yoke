package com.yoke.activities.profile;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yoke.R;
import com.yoke.activities.profileEdit.ProfileEditActivity;
import com.yoke.database.types.Macro;
import com.yoke.database.types.Profile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

//    protected Connection connection = MultiClientConnection.getInstance();

    private static final String TAG = "ProfileActivity";

    private List<com.yoke.database.types.Button> buttons;
    private ArrayList<Macro> mMacro = new ArrayList<>();
    private TextView profileName;
//    private TextView profile_name = findViewById(R.id.textView);

    private Profile profile; //declare the profile object we are going to use
    boolean isLandscape;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = findViewById(R.id.toolbar_profile);
        profileName = (TextView) findViewById(R.id.profileTextView);
        ImageView edit = (ImageView) findViewById(R.id.beginEdit);

        isLandscape =
                getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;

        if (isLandscape) {
            toolbar.setVisibility(View.GONE);
        }
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //TODO create custom toolbar + back button
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        retrieveData();

        //when edit button is clicked send the profile id and open the edit activity
        edit.setOnClickListener(openEditView -> {
            // Make sure the profile has loaded
            if (profile != null) {
                Intent intent = new Intent(getApplicationContext(), ProfileEditActivity.class);
                intent.putExtra("profile id", profile.getID());
                startActivity(intent);
            }
        });
    }

    /**
     * Retrieve the data from the database and store in the arraylist
     */
    public void retrieveData() {
        Long profileID = getIntent().getLongExtra("profile id", 0);

        //add the profile datas to the arguments
        Profile.getByID(profileID, (profile) -> {
            runOnUiThread(() -> {
                this.profile = profile;

                String name = profile.getName();
                profileName.setText(name);
                buttons = profile.getButtons();

                //sort the buttons so they are in order and displayed in a correct order on the layout
                Collections.sort(buttons, (o1, o2) -> o1.getIndex() - o2.getIndex());

                myRecyclerView();
            });
        });


        //not sure which way is better
//        DataBase.initialize(this, new DataObject.Callback() {
//            @Override
//            public void call() {
//                Profile.getAll(profiles -> {
//                    for (Profile profile : profiles) {
//                        profile.getID();
//                        profile.getIndex();
//                        profile.getWidth();
//                        profile.getHeight();
//                        mButton.addAll(profile.getButtons());
////                        profile_name.setText(profile.getName());
//
//                    }
//                });
//
//                Profile.getAll(profiles -> {
//                    profile = profiles.get(i);
//                    mButton.addAll(profile.getButtons());
//                });
//
////                Profile.getByID();
//                for (com.yoke.database.types.Button button : mButton) {
//                    mImageID.add((int) button.getIndex());
//                    //add the button index for positioning buttons
//                    mMacro.add(button.getMacro());
//
//                }
//
//            }
//        });

    }

    //uses the rercycler view
    private void myRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        ButtonsRecyclerViewAdapter adapter =
                new ButtonsRecyclerViewAdapter(this, buttons);

        int columns = 2;
        if (isLandscape) {
            columns = 3;
        }
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, columns));
    }

//    going back to the previous page
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();

        // Make sure to refresh the data
        retrieveData();
    }
}
