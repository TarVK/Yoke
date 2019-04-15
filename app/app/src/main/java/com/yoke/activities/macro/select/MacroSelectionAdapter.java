package com.yoke.activities.macro.select;

import android.app.Activity;
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
import com.yoke.activities.profileEdit.ProfileEditActivity;
import com.yoke.database.types.Macro;

import java.util.ArrayList;

public class MacroSelectionAdapter extends RecyclerView.Adapter<MacroSelectionAdapter.MyViewHolder> {

    private static final String TAG = "MacroSelectionAdapter";

    private ArrayList<Macro> mDataset;
    private Activity mContext;
    private long profileID;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView selectionImage;
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
    public MacroSelectionAdapter(ArrayList<Macro> myDataset, Activity context, long pID) {
        mDataset = myDataset;
        mContext = context;
        profileID = pID;
    }

    @Override
    public MacroSelectionAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_macro_selection_object, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: called.");

        Macro macro = mDataset.get(position);

        String name = macro.getName();
        String action;
        try {
            action = macro.getAction().toString();
        } catch (IllegalStateException e) {
            // If no valid action was stored
            action = "Undefined";
        }

        Bitmap bitmap = macro.getCombinedImage();

        holder.selectionImage.setImageBitmap(bitmap);

        //TODO Add support for compoundactions

        holder.selectionName.setText(name);
        holder.selectionAction.setText(action);
        holder.parentLayout.setOnClickListener(v -> {
            Log.d(TAG, "Add Macro to Profile, mID: " + macro.getID() + ", pID: " + profileID);


            Intent intent = new Intent(mContext, ProfileEditActivity.class);
            intent.putExtra("profile id", profileID);
            intent.putExtra("macro id", macro.getID());

            mContext.startActivity(intent);
            mContext.finish();
        });

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
