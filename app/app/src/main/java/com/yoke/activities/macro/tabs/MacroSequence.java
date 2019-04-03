package com.yoke.activities.macro.tabs;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.yoke.R;

import com.yoke.activities.macro.select.MacroSelection;
import com.yoke.connection.CompoundMessage;
import com.yoke.connection.Message;
import com.yoke.database.types.Button;
import com.yoke.database.types.Macro;

import java.util.ArrayList;
import java.util.List;


public class MacroSequence extends Fragment implements MacroSequenceStartDragListener {

    private static final String TAG = "MacroSequence";

    RecyclerView recyclerView;
    MacroSequenceAdapter mAdapter;
    //ArrayList<String> stringArrayList = new ArrayList<>();
    ItemTouchHelper touchHelper;

    private View view;

//    private ArrayList<Integer> mImageID = new ArrayList<>(); //save the index of the buttons
//    private ArrayList<String> mImageName = new ArrayList<>(); //save the name of the buttons
//    private List<Button> mButton;

    CompoundMessage mAction = new CompoundMessage();

    FloatingActionButton addAction;

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



        addAction.setOnClickListener(v -> {
            profile.save(()-> {
                macro.save(() -> {
                    runOnUiThread(() -> {
                        Intent intent = new Intent(getActivity().getApplicationContext(), MacroSelection.class);
                        intent.putExtra("macro id", macro.getID());
                        startActivity(intent);
                    });
                })

            });
        });


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
        Long macroID = getActivity().getIntent().getLongExtra("macro id", 0);
        Log.w(TAG, "retrieveData: " + macroID);

        Macro.getByID(macroID, (macro) -> {
            mAction = macro.getAction();
        });




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



        mAdapter = new MacroSequenceAdapter(mAction,this);

        mAdapter.notifyItemInserted(mAction); //TODO MAY NOT BE NEEDED

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
