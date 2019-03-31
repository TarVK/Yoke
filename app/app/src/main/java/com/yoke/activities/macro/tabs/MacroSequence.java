package com.yoke.activities.macro.tabs;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import android.support.v7.widget.helper.ItemTouchHelper;

import com.example.yoke.R;
import android.support.v7.widget.RecyclerView;

import com.yoke.activities.splash.SplashActivity;
import com.yoke.database.types.Button;
import com.yoke.database.types.Macro;
import com.yoke.database.types.Profile;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class MacroSequence extends Fragment implements MacroSequenceStartDragListener {

    private static final String TAG = "MacroSequence";

    RecyclerView recyclerView;
    MacroSequenceRecyclerViewAdapter mAdapter;
    //ArrayList<String> stringArrayList = new ArrayList<>();
    ItemTouchHelper touchHelper;

    private View view;

    private ArrayList<Integer> mImageID = new ArrayList<>(); //save the index of the buttons
    private ArrayList<String> mImageName = new ArrayList<>(); //save the name of the buttons
    private List<Button> mButton;
    ArrayList<Macro> mMacro = new ArrayList<>();

    public MacroSequence() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.activity_macro_sequence,
                container, false);


        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);


//        retrieveData();
//
//        populateRecyclerView();

        // Inflate the layout for this fragment
        return view;
    }



    /**
     * Retrieve the data from the database and store in the arraylist
     */
    public void retrieveData() {
        Long profileId = getActivity().getIntent().getLongExtra("profile id", 0);
        Log.w(TAG, "retrieveData: " + profileId);

        int i = 0;

        //add the profile datas to the arguments
//        Profile.getByID(profileId, (profile)-> {
//            mButton = (profile.getButtons());
//
//            //sort the buttons so they are in order and displayed in a correct order on the layout
//            mButton.sort(new Comparator<Button>() {
//                @Override
//                public int compare(Button o1, Button o2) {
//                    return o1.getIndex() - o2.getIndex();
//                }
//            });
//            profile.getName();
//            profile.getIndex();
//        });


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

    private void populateRecyclerView() {
        mMacro.add(new Macro("name1"));
        mMacro.add(new Macro("name2"));
        mMacro.add(new Macro("name3"));


        mAdapter = new MacroSequenceRecyclerViewAdapter(mMacro,this);

        mAdapter.notifyItemInserted(mMacro.size() - 1); //TODO MAY NOT BE NEEDED

        ItemTouchHelper.Callback callback =
                new MacroSequenceItemMoveCallback(mAdapter);
        touchHelper  = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);

        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void requestDrag(RecyclerView.ViewHolder viewHolder) {
        touchHelper.startDrag(viewHolder);
    }

}
