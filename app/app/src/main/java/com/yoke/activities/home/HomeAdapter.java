package com.yoke.activities.home;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.yoke.R;
import com.yoke.activities.profile.ProfileActivity;
import com.yoke.activities.profileEdit.ButtonsEditRecyclerViewAdapter;
import com.yoke.database.types.Profile;

import java.util.ArrayList;
import java.util.Collections;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> implements
        DragController.ItemTouchHelperContract {
    private ArrayList<Profile> profiles = new ArrayList<>();
    private HomeActivity activity;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public ConstraintLayout parentLayout;
        public ImageButton removeButton;

        public MyViewHolder(View v) {
            super(v);
            textView = v.findViewById(R.id.my_textview);
            parentLayout = itemView.findViewById(R.id.parent_layout);
            removeButton = itemView.findViewById(R.id.delete);
        }
    }

    //Constructor
    public HomeAdapter(ArrayList<Profile> myDataset, HomeActivity activity) {
        profiles = myDataset;
        this.activity = activity;
    }

    @Override
    public HomeAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_profile_item, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Profile profile = profiles.get(position);
        holder.textView.setText(profile.getName());

        // Listen for opening
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, ProfileActivity.class);
                intent.putExtra("profile id", profile.getID());
                activity.startActivity(intent);
            }
        });

        // Listen for deletion
        holder.removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder2 =
                        new AlertDialog.Builder(activity);
                builder2.setTitle("Delete profile");
                builder2.setMessage("Are you sure you want to remove this profile?")
                        .setPositiveButton("Yes",
                                (dialog, id) -> {
                                    deleteProfile(profile);
                                })
                        .setNegativeButton("No", (dialog, id) -> dialog.dismiss());
                AlertDialog alertDialog = builder2.create();
                alertDialog.show();
            }
        });

    }

    /**
     * Completely deletes a profile from the database
     * @param profile  The profile to be deleted
     */
    protected void deleteProfile(Profile profile) {
        long profileID = profile.getID();
        int profileListIndex = profiles.indexOf(profile);

        profile.delete();
        profiles.remove(profileListIndex);
        notifyItemRemoved(profileListIndex);
        notifyItemRangeChanged(profileListIndex, profiles.size());
    }

    @Override
    public int getItemCount() {
        return profiles.size();
    }


    @Override
    public void onRowMoved(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(profiles, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(profiles, i, i - 1);
            }
        }
        activity.orderChanged = true;
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onRowSelected(ButtonsEditRecyclerViewAdapter.ViewHolder myViewHolder) {

    }

    @Override
    public void onRowClear(ButtonsEditRecyclerViewAdapter.ViewHolder myViewHolder) {

    }
}
