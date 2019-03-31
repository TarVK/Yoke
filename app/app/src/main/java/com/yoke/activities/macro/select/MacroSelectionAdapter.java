package com.yoke.activities.macro.select;

import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yoke.R;
import com.yoke.activities.profile.ProfileActivity;
import com.yoke.database.types.Macro;

import java.util.ArrayList;

public class MacroSelectionAdapter extends RecyclerView.Adapter<MacroSelectionAdapter.MyViewHolder> {

    private static final String TAG = "MacroSelectionAdapter";

    private ArrayList<Macro> mDataset;
    private Context mContext;
    private long profileID;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView selectionImage;
        public TextView selectionText;
        public ConstraintLayout parentLayout;

        public MyViewHolder(View v) {
            super(v);
            selectionImage = v.findViewById(R.id.selectionImage);
            selectionText = v.findViewById(R.id.selectionText);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }

    //Constructor
    public MacroSelectionAdapter(ArrayList<Macro> myDataset, Context context, long pID) {
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

        String text = mDataset.get(position).getAction().toString();
        holder.textView.setText(text);
        holder.parentLayout.setOnClickListener(v -> {
            //TODO set new button


            Intent intent = new Intent(mContext, ProfileActivity.class); //TODO create macro
            intent.putExtra("profile id", profileID);
            mContext.startActivity(intent);
        });


    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
