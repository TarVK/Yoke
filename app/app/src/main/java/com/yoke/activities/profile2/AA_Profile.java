package com.yoke.activities.profile2;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yoke.R;
import com.yoke.activities.macro.MacroBuilder;
import com.yoke.database.types.Button;
import com.yoke.database.types.Macro;
import com.yoke.database.types.Profile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AA_Profile extends AppCompatActivity {

//    protected Connection connection = MultiClientConnection.getInstance();

    private static final String TAG = "AA_Profile";

    private ArrayList<Integer> mImageID = new ArrayList<>(); //save the index of the buttons
    private ArrayList<String> mImageName = new ArrayList<>(); //save the name of the buttons
    private List<com.yoke.database.types.Button> mButton;
    private ArrayList<Macro> mMacro = new ArrayList<>();
    private TextView profileName;
    private String name = "";
    Long id;
//    private TextView profile_name = findViewById(R.id.textView);

    Profile profile; //declare the profile object we are going to use
    boolean isLandscape;
    boolean hasSpace;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aa_profile);
        Toolbar toolbar = findViewById(R.id.toolbar_profile);
        profileName = (TextView) findViewById(R.id.profileTextView);

        isLandscape =
                getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;

        if (isLandscape) {
            toolbar.setVisibility(View.GONE);
        }
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Log.w(TAG, "onCreate: started");

        retrieveData();

        Log.w(TAG, "onCreate: " + name);
        name = returnName();
        profileName.setText(name);

        //add a new macro, it should direct to the macro activity
        findViewById(R.id.addMacro)
                .setOnClickListener(addView -> {
            if (hasSpace) {
                Intent intent = new Intent(getApplicationContext(), MacroBuilder.class);
                intent.putExtra("macro id", -1);
                startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(),"cant be added", Toast.LENGTH_LONG).show();
            }
        });

        //when edit button is clicked send the profile id and open the edit activity
        findViewById(R.id.beginEdit)
                .setOnClickListener(openEditView -> {
            Intent intent = new Intent(getApplicationContext(), AA_ProfileEdit.class);
            intent.putExtra("profile id", retrieveID());
            startActivity(intent);
            finish();
        });

    }

    /**
     * Retrieve the data from the database and store in the arraylist
     */
    public void retrieveData() {
        Long profileID = getIntent().getLongExtra("profile id", 0);
        id = profileID;
        Log.w(TAG, "retrieveData: " + id);

        //add the profile datas to the arguments
        Profile.getByID(id, (profile)-> {
            String name = profile.getName();
            hasSpace = profile.hasSpace();
            profileName.setText(name);
            Log.w(TAG, "retrieveData: "+ name);

            mButton = (profile.getButtons());

            //sort the buttons so they are in order and displayed in a correct order on the layout
            Collections.sort(mButton, (o1, o2) -> o1.getIndex() - o2.getIndex());

            profile.getName();
            profile.getIndex();

            myRecyclerView();

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

        mImageID.add(R.drawable.spotify);
        mImageName.add("spotify");
        mImageID.add(R.drawable.steam);
        mImageName.add("steam");
        mImageID.add(R.drawable.youtube);
        mImageName.add("youtube");
        mImageID.add(R.drawable.chrome);
        mImageName.add("chrome");
        mImageID.add(R.drawable.twitch);
        mImageName.add("twitch");
        mImageID.add(R.drawable.wikipedia);
        mImageName.add("wikipedia");

    }

    //uses the rercycler view
    private void myRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        RecyclerViewAdapter adapter =
                new RecyclerViewAdapter(mImageID,this, mImageName, mMacro, mButton);

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

    //pass on the profile id for the profile edit activity
    public long retrieveID() {
        return id;
    }

    public String returnName() { return name; }






}
