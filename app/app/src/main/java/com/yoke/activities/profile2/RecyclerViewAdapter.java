package com.yoke.activities.profile2;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.yoke.R;
import com.yoke.connection.Connection;
import com.yoke.connection.client.MultiClientConnection;
import com.yoke.database.types.Button;
import com.yoke.database.types.Macro;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    private static final String TAG = "RecyclerViewAdapter";
    protected Connection connection = MultiClientConnection.getInstance(); //establishes the connection
    private ArrayList<Integer> mImage = new ArrayList<>(); //store the images
    private ArrayList<String> mName = new ArrayList<>();
    private ArrayList<Macro> mMacro = new ArrayList<>();
    private List<com.yoke.database.types.Button> mButton;
    private Context mContext;


    /**
     *
     * @param image
     * @param context
     * @param name
     * @param macro list of macros of the selected profile
     */
    public RecyclerViewAdapter(ArrayList<Integer> image, Context context, ArrayList<String> name,
                               ArrayList<Macro> macro, List<Button> button) {
        mImage = image;
        mName = name;
        mMacro = macro;
        mContext = context;
        mButton = button;
    }


    //inflates the view
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.aa_layout_listitem, parent, false);
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
//        viewHolder.buttonImage.setImageResource(mButton.get(i).getIndex());

        //THIS IS DRAWABLE SOURCES
//        viewHolder.buttonImage.setImageResource(mImage.get(i));

        //it should retrieve the action of the button
        viewHolder.buttonImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicked on: " + mImage.get(i));
//                Toast.makeText(mContext, mName.get(i), Toast.LENGTH_LONG).show();
//                String path = mMacro.get(i).getAction().toString();

                //either one should be selected?
//                connection.send(new OpenURLCmd(path));
//                connection.send(new OpenProgramCmd(path));
//                connection.send(new OpenURLCmd("youtube.com"));
                connection.send(mButton.get(i).getMacro().getAction());

            }
        });
    }

    @Override
    public int getItemCount() {
        return mButton.size();
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
