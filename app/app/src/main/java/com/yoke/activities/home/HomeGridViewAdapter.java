package com.yoke.activities.home;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.yoke.R;
import com.yoke.activities.profile.ProfileActivity;
import com.yoke.connection.Connection;
import com.yoke.connection.client.MultiClientConnection;
import com.yoke.database.types.Profile;

/**
 *
 */
public class HomeGridViewAdapter extends RecyclerView.Adapter<HomeGridViewAdapter.ViewHolder>{

    private static final String TAG = "HomeGridViewAdapter";
    protected Connection connection =
            MultiClientConnection.getInstance(); // Gets the connection
    private Context context;
    private Profile profile;

    /**
     *
     * @param profile profile that is referenced
     */
    public HomeGridViewAdapter(Context context, Profile profile) {
        this.context = context;
        this.profile = profile;
    }


    //inflates the view
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.layout_button_home, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    /**
     *
     * @param holder
     * @param i
     *
     * it sets up the image and manages the macro clicks
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        Log.d(TAG, "onBindViewHolder: called. ");

        //put the image in the viewHolder
        holder.buttonImage.setImageBitmap(profile.getButtons().get(i).getMacro().getCombinedImage());

        // Listen for opening profile
        holder.parentLayout.setOnClickListener(v -> {
            Log.d(TAG, "Open profile: " + profile.getID());

            Intent intent = new Intent(context, ProfileActivity.class);
            intent.putExtra("profile id", profile.getID());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return profile.getButtons().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView buttonImage; //this is the imageView that images are put into
        //        TextView profileTitle;
        RelativeLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            buttonImage = itemView.findViewById(R.id.image);
            parentLayout = itemView.findViewById(R.id.parent_layout);

        }
    }
}
