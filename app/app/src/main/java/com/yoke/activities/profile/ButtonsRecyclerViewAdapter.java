package com.yoke.activities.profile;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.yoke.R;
import com.yoke.connection.Connection;
import com.yoke.connection.client.MultiClientConnection;
import com.yoke.database.types.Button;

import java.util.List;

/**
 *
 */
public class ButtonsRecyclerViewAdapter extends RecyclerView.Adapter<ButtonsRecyclerViewAdapter.ViewHolder>{

    private static final String TAG = "ButtonsRecyclerViewAdapter";
    protected Connection connection =
            MultiClientConnection.getInstance(); // Gets the connection
    private List<com.yoke.database.types.Button> buttons;
    private Context context;


    /**
     *
     * @param buttons list of buttons of the selected profile
     */
    public ButtonsRecyclerViewAdapter(Context context, List<Button> buttons) {
        this.context = context;
        this.buttons = buttons;
    }


    //inflates the view
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.button, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    /**
     *
     * @param viewHolder
     * @param i
     *
     * it sets up the image and manages the macro clicks
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Log.d(TAG, "onBindViewHolder: called. ");

        //put the image in the viewHolder

//        viewHolder.buttonImage.setImageBitmap();
        //gets the macro foreground and background images
//        viewHolder.buttonImage.setImageBitmap(mMacro.get(i).getForegroundImage());
//        viewHolder.buttonImage.setImageBitmap(mMacro.get(i).getBackgroundImage());
//        viewHolder.buttonImage.setImageResource(mMacro.get(i).getForegroundColor());
//        viewHolder.buttonImage.setImageResource(mMacro.get(i).getBackgroundColor());

        //ACTUAL BUTTON IMAGE SOURCES
        viewHolder.buttonImage.setImageBitmap(buttons.get(i).getMacro().getCombinedImage());

        //THIS IS DRAWABLE SOURCES
//        viewHolder.buttonImage.setImageResource(mImage.get(i));

        //it should retrieve the action of the button
        viewHolder.buttonImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                Toast.makeText(mContext, mName.get(i), Toast.LENGTH_LONG).show();
//                String path = mMacro.get(i).getAction().toString();

                if (connection.getState() == Connection.CONNECTED) {
                    try {
                        connection.send(buttons.get(i).getMacro().getAction());
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(context,
                                "Something went wrong while trying to perform this action",
                                Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(context, "Command could not be sent. " +
                                    "\n Please make sure you are connected",
                            Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return buttons.size();
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
