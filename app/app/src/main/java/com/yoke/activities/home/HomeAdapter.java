package com.yoke.activities.home;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
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

    private static final String TAG = "HomeAdapter";

    private ArrayList<Profile> profiles = new ArrayList<>();
    private HomeActivity activity;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public ConstraintLayout parentLayout;
        public ImageButton removeButton;

        final RecyclerView gridView;

        public MyViewHolder(View v) {
            super(v);
            textView = v.findViewById(R.id.my_textview);
            parentLayout = itemView.findViewById(R.id.parent_layout);
            removeButton = itemView.findViewById(R.id.delete);

            gridView = v.findViewById(R.id.home_button_list);
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

        HomeGridViewAdapter adapter =
                new HomeGridViewAdapter(activity, profile);

        holder.gridView.setAdapter(adapter);
        holder.gridView.setLayoutManager(new GridLayoutManager(activity, 3));

        // Listen for opening of profile
        holder.parentLayout.setOnClickListener(v -> {
            Log.d(TAG, "Open profile: " + profile.getID());

            Intent intent = new Intent(activity, ProfileActivity.class);
            intent.putExtra("profile id", profile.getID());
            activity.startActivity(intent);
        });

        // Listen for deletion of profile
        holder.removeButton.setOnClickListener(v -> {
            Log.d(TAG, "Delete profile: " + profile.getID());

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
        });

    }

    /**
     * Completely deletes a profile from the database
     * @param profile  The profile to be deleted
     */
    protected void deleteProfile(Profile profile) {
        long profileID = profile.getID();
        int profileAtIndex = profiles.indexOf(profile);

        profile.delete(activity);
        profiles.remove(profileAtIndex);
        notifyItemRemoved(profileAtIndex);
        notifyItemRangeChanged(profileAtIndex, profiles.size());
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
