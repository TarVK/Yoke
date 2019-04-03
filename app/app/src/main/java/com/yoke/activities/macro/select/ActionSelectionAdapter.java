package com.yoke.activities.macro.select;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yoke.R;
import com.yoke.activities.macro.tabs.MacroSequence;
import com.yoke.connection.Message;
import com.yoke.database.types.Button;
import com.yoke.database.types.Macro;
import com.yoke.database.types.Profile;

import java.util.ArrayList;

public class ActionSelectionAdapter extends RecyclerView.Adapter<ActionSelectionAdapter.MyViewHolder> {

    private static final String TAG = "MacroSelectionAdapter";

    private Message mDataset;
    private Context mContext;
    private long macroID;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView selectionImage;
        public TextView selectionName;
        public TextView selectionAction;
        public ConstraintLayout parentLayout;

        public MyViewHolder(View v) {
            super(v);
            selectionImage = v.findViewById(R.id.selectionImage);
            selectionName = v.findViewById(R.id.selectionName);
            selectionAction = v.findViewById(R.id.selectionAction);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }

    //Constructor
    public ActionSelectionAdapter(Message myDataset, Context context, long mID) {
        mDataset = myDataset;
        mContext = context;
        macroID = mID;
    }

    @Override
    public ActionSelectionAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_macro_action_selection_object, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: called.");


        holder.parentLayout.setOnClickListener(v -> {
            // Set new Action to Macro
            Macro.getByID(macroID, (macro) -> {
                Message mAction = macro.getAction();

                macro.addButton(mDataset.get(position));


                macro.save(() -> {
                    Log.d(TAG, "Add Action to Macro, mID: " + macro.getID());

                    Intent intent = new Intent(mContext, MacroSequence.class);
                    intent.putExtra("profile id", profileID);
                    mContext.startActivity(intent);
                });
            });



        });

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
