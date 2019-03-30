package com.yoke.activities.home;

import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.yoke.R;
import com.yoke.activities.profile.ProfileActivity;
import com.yoke.database.types.Profile;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private ArrayList<Profile> mDataset = new ArrayList<>();
    private Context mContext;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public ConstraintLayout parentLayout;

        public MyViewHolder(View v) {
            super(v);
            textView = v.findViewById(R.id.my_textview);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }

    //Constructor
    public MyAdapter(ArrayList<Profile> myDataset, Context context) {
        mDataset = myDataset;
        mContext = context;
    }

    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_listitem, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.textView.setText(mDataset.get(position).getName());
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ProfileActivity.class);
                intent.putExtra("profile id", mDataset.get(position).getID());
                mContext.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
