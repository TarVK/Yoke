package com.yoke.activities.macro.tabs;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.yoke.R;
import com.yoke.activities.macro.MacroActivity;
import com.yoke.connection.ComposedMessage;
import com.yoke.connection.CompoundMessage;
import com.yoke.connection.Connection;
import com.yoke.connection.Message;
import com.yoke.connection.RepeatMessage;
import com.yoke.connection.client.MultiClientConnection;

import java.util.ArrayList;


public class MacroSequence extends Fragment implements MacroSequenceStartDragListener {

    private static final String TAG = "MacroSequence";

    protected Connection connection;

    RecyclerView recyclerView;
    ItemTouchHelper dragHelper;
    FloatingActionButton addAction;

    // Adapter for the RecyclerView
    MacroSequenceAdapter adapter;

    // Reference to the macro activity
    private MacroActivity activity;

    // List of Actions
    private ArrayList<RepeatMessage> mRepeatMessage;

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
        activity = (MacroActivity) getActivity();
        connection = MultiClientConnection.getInstance(activity); // Gets the connection
        View view = inflater.inflate(R.layout.activity_macro_sequence,
                container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        addAction = (FloatingActionButton) view.findViewById(R.id.addAction);
        activity.setNewThemeColour(addAction);

        CommandRequester commandRequester = new CommandRequester(getContext());

        activity.loadMacro(() -> {
            constructSequence();
            activity.mRepeatMessage = this.mRepeatMessage;
            populateRecyclerView();

            addAction.setOnClickListener(v -> {
                activity.runOnUiThread(() -> {
                    commandRequester.requestCommand(action -> {
                        if (action != null) {
                            mRepeatMessage.add(new RepeatMessage(action));
                            adapter.notifyItemInserted(mRepeatMessage.size() - 1);
                        } else {
                            Toast.makeText(getContext(),
                                    getString(R.string.selectMacroFailed),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                });
            });
        });

        // Inflate the layout for this fragment
        return view;
    }

    private void constructSequence() {
        mRepeatMessage = new ArrayList<>();

        // Check if any action is stored at all
        Message action;
        try {
            action = activity.macro.getAction();
        } catch (Exception e){
            return;
        }

        if (action instanceof CompoundMessage) {
            for (ComposedMessage.MessageDelay md : ((CompoundMessage) action)) {
                mRepeatMessage.add(new RepeatMessage(md.message, 1, md.delay));
            }

            ArrayList<RepeatMessage> tempArray = new ArrayList<>();
            int count = 1;
            for (int i = 0; i < mRepeatMessage.size() - 1; i++) {
                if (mRepeatMessage.get(i).message == mRepeatMessage.get(i + 1).message
                        && mRepeatMessage.get(i).frequency == mRepeatMessage.get(i + 1).frequency) {
                    count++;
                } else {
                    tempArray.add(new RepeatMessage(mRepeatMessage.get(i).message, count, mRepeatMessage.get(i).frequency));
                    count = 1;
                }
            }
            tempArray.add(new RepeatMessage(mRepeatMessage.get(mRepeatMessage.size() - 1).message, count, mRepeatMessage.get(mRepeatMessage.size() - 1).frequency));
            mRepeatMessage = tempArray;
        } else {
            mRepeatMessage.add(new RepeatMessage(action));
        }
    }



    private void populateRecyclerView() {

        adapter = new MacroSequenceAdapter(mRepeatMessage,this, activity);

        adapter.notifyItemInserted(mRepeatMessage.size() - 1);

        ItemTouchHelper.Callback callback =
                new MacroSequenceItemMoveCallback(adapter);
        dragHelper  = new ItemTouchHelper(callback);
        dragHelper.attachToRecyclerView(recyclerView);

        recyclerView.setAdapter(adapter);
        LinearLayoutManager llm = new LinearLayoutManager(this.getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
    }

    @Override
    public void requestDrag(RecyclerView.ViewHolder viewHolder) {
        dragHelper.startDrag(viewHolder);
    }
}
