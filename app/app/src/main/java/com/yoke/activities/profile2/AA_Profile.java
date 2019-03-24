package com.yoke.activities.profile2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.example.yoke.R;
import com.yoke.database.types.Button;
import com.yoke.database.types.Macro;
import com.yoke.database.types.Profile;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AA_Profile extends AppCompatActivity {

//    protected Connection connection = MultiClientConnection.getInstance();

    private static final String TAG = "AA_Profile";

    private ArrayList<Integer> mImageID = new ArrayList<>(); //save the index of the buttons
    private ArrayList<String> mImageName = new ArrayList<>(); //save the name of the buttons
    private List<com.yoke.database.types.Button> mButton;
    private ArrayList<Macro> mMacro = new ArrayList<>();
//    private TextView profile_name = findViewById(R.id.textView);
//    private Button beginEdit = findViewById(R.id.beginEdit);

    Profile profile; //declare the profile object we are going to use



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aa_profile);
        Toolbar toolbar = findViewById(R.id.toolbar_profile);
        Log.d(TAG, "onCreate: started");

        retrieveData();
//        beginEdit.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//               startActivity(new Intent(AA_Profile.this, AA_ProfileEdit.class));
//            }
//        });

    }

    /**
     * Retrieve the data from the database and store in the arraylist
     */
    public void retrieveData() {
        Long profileId = getIntent().getLongExtra("profile id", 0);
        Log.w(TAG, "retrieveData: " + profileId);

        int i = 0;

        //add the profile datas to the arguments
        Profile.getByID(profileId, (profile)-> {
            mButton = (profile.getButtons());

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
        RecyclerView recyclerView = findViewById(R.id.recyclerv_view);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(mImageID,this, mImageName, mMacro, mButton);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
    }

//    going back to the previous page
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }






}
