package com.yoke.activities.profile2;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.yoke.R;
import com.yoke.connection.Connection;
import com.yoke.database.types.Macro;

import java.util.ArrayList;

/**
 * Implement longClick, drag, add, delete, done
 */
public class RecyclerViewAdapterEdit extends
        RecyclerView.Adapter<RecyclerViewAdapterEdit.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapterEdit";

    protected Connection connection; //establishes the connection
    private ArrayList<Integer> mImage = new ArrayList<>(); //store the images
    private ArrayList<String> mName = new ArrayList<>();
    private ArrayList<Macro> mMacro = new ArrayList<>();
    private Context mContext;

    /**
     * Put list of items in the constructor
     */
    public RecyclerViewAdapterEdit(ArrayList<Integer> image, Context context, ArrayList<String> name,
                                   ArrayList<Macro> macro) {
        mImage = image;
        mName = name;
        mMacro = macro;
        mContext = context;
    }

    //inflates the view
    @NonNull
    @Override
    public RecyclerViewAdapterEdit.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.aa_layout_listitem, parent, false);

        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    //implement drag
    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapterEdit.ViewHolder viewHolder, int i) {
        Log.d(TAG, "onBindViewHolder: ");


    }

    @Override
    public int getItemCount() {
        return mImage.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public class LongClick {

    }


}
