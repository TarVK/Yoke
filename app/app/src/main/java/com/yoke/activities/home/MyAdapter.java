package com.example.dblapp;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private ArrayList<String> mDataset = new ArrayList<String>();
    private Context mContext;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public ConstraintLayout parentLayout;

        public MyViewHolder(TextView v) {
            super(v);
            textView = itemView.findViewById(R.id.my_textview);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }

    //Constructor
    public MyAdapter(ArrayList<String> myDataset, Context context) {
        mDataset = myDataset;
        mContext = context;
    }

    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TextView v = (TextView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_listitem, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.textView.setText(mDataset.get(position));
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
