package com.yoke.activities.profileEdit;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.yoke.R;
import com.yoke.connection.Connection;
import com.yoke.database.types.Button;

import java.util.Collections;
import java.util.List;

/**
 * Implement longClick, drag, add, delete, done
 * src: https://github.com/journaldev/journaldev/tree/master/Android/AndroidRecyclerViewDragAndDrop
 */
public class ButtonsEditRecyclerViewAdapter extends
        RecyclerView.Adapter<ButtonsEditRecyclerViewAdapter.ViewHolder> implements
        ItemMoveCallback.ItemTouchHelperContract {

    private static final String TAG = "ButtonsEditRVAdapter";

    protected Connection connection; //establishes the connection
    private ProfileEditActivity mProfile;
    private List<com.yoke.database.types.Button> mButton;
    private final StartDragListener mStartDragListener;
    private Context mContext ;
    private RecyclerView mRecyclerView;

    public boolean activateDelete = false;

    public class ViewHolder extends RecyclerView.ViewHolder {

        View rowView;
        ImageView buttonImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rowView = itemView;
            buttonImage = itemView.findViewById(R.id.image);

        }
    }

    /**
     * Put list of items in the constructor
     */
    public ButtonsEditRecyclerViewAdapter(ProfileEditActivity profile, List<Button> button, StartDragListener startDragListener, RecyclerView recyclerView) {
        mProfile = profile;
        mButton = button;
        mStartDragListener = startDragListener;
        mContext = profile.getApplicationContext();
        mRecyclerView = recyclerView;
    }

    //inflates the view
    @NonNull
    @Override
    public ButtonsEditRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view;
        if (mProfile.isLandscape) {
            view = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.layout_button_edit, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.layout_button, parent, false);
        }

        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    //implement drag and stop
    @Override
    public void onBindViewHolder(@NonNull ButtonsEditRecyclerViewAdapter.ViewHolder viewHolder, int i) {
        Log.d(TAG, "onBindViewHolder: called");
        com.yoke.database.types.Button button = mButton.get(i);
        viewHolder.buttonImage.setImageBitmap(button.getMacro().getCombinedImage());

//        buttonViewHolders.add(viewHolder);

        //each button with image is subject to dragging interaction
        viewHolder.buttonImage.setOnTouchListener(new View.OnTouchListener() {

            //Select macro
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    //sets the background color to white in default
                    select(viewHolder, button);
                }
                return false;
            }
        });

        if (mProfile.getSelectedButton() == null) {
            select(viewHolder, button);
        }
    }

    @Override
    public void onViewRecycled(@NonNull ViewHolder holder) {
        super.onViewRecycled(holder);
//        buttonViewHolders.remove(holder);
    }

    @Override
    public int getItemCount() {
        return mButton.size();
    }

    //It correctly swaps the position of button when it is dragged to a target position
    @Override
    public void onRowMoved(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(mButton, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(mButton, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    //selected button will be displayed in gray
    @Override
    public void onRowSelected(ViewHolder myViewHolder) {
        myViewHolder.rowView.setBackgroundColor(Color.GRAY);
    }

    //defines an action when dragging interaction is stopped
    //currently commented out as it was not used
    @Override
    public void onRowClear(ViewHolder myViewHolder) {
//        myViewHolder.rowView.setBackgroundColor(Color.WHITE);
    }

    public boolean canDelete() {
        return activateDelete;
    }

    public void select(ViewHolder viewHolder, com.yoke.database.types.Button button) {

        //reset the background color of the macros to white
        int childCount = mRecyclerView.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final ViewHolder buttonViewHolder =
                    (ViewHolder) mRecyclerView.getChildViewHolder(mRecyclerView.getChildAt(i));
            buttonViewHolder.rowView.setBackgroundColor(Color.WHITE);
        }

        viewHolder.rowView.setBackgroundColor(Color.GRAY);

        mStartDragListener.requestDrag(viewHolder);
        mProfile.selectButton(button);
    }

}
