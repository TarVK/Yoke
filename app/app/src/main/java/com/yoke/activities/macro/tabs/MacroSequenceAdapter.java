package com.yoke.activities.macro.tabs;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

import com.example.yoke.R;
import com.yoke.connection.ComposedMessage;
import com.yoke.connection.Connection;
import com.yoke.connection.Message;
import com.yoke.connection.RepeatMessage;
import com.yoke.connection.client.MultiClientConnection;
import com.yoke.database.types.Button;
import com.yoke.database.types.Macro;

import java.util.List;

public class MacroSequenceAdapter extends RecyclerView.Adapter<MacroSequenceAdapter.MyViewHolder> implements MacroSequenceItemMoveCallback.ItemTouchHelperContract {

    private ArrayList<Macro> data;

    private final MacroSequenceStartDragListener mStartDragListener;

    private static final String TAG = "MacroSequenceRVAdapter";
    protected Connection connection = MultiClientConnection.getInstance(); //establishes the connection
//    private ArrayList<String> mName = new ArrayList<>();
//    private ArrayList<Macro> mMacro = new ArrayList<>();
//    private List<Button> mButton;
//    private Context mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView actionText;
        TextView editRepetitions;
        TextView editDelay;
        ImageView increaseRepeat;
        ImageView decreaseRepeat;
        ImageView increaseDelay;
        ImageView decreaseDelay;

        View rowView;
        ImageView dragObject;

        public MyViewHolder(View itemView) {
            super(itemView);

            rowView = itemView;
            actionText = itemView.findViewById(R.id.actionText);
            editRepetitions = itemView.findViewById(R.id.editRepetitions);
            editDelay = itemView.findViewById(R.id.editDelay);
            increaseRepeat = itemView.findViewById(R.id.increaseRepeat);
            decreaseRepeat = itemView.findViewById(R.id.decreaseRepeat);
            increaseDelay = itemView.findViewById(R.id.increaseDelay);
            decreaseDelay = itemView.findViewById(R.id.decreaseDelay);
            dragObject = itemView.findViewById(R.id.dragObject);
        }
    }

    public MacroSequenceAdapter(ArrayList<Macro> data, MacroSequenceStartDragListener startDragListener) {
        mStartDragListener = startDragListener;
        this.data = data;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_macro_sequence_object, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: called. ");



        holder.actionText.setText(position);

        holder.increaseRepeat.setOnClickListener(v -> {

        });

        holder.decreaseRepeat.setOnClickListener(v -> {

        });

        holder.increaseDelay.setOnClickListener(v -> {

        });

        holder.decreaseDelay.setOnClickListener(v -> {

        });


        holder.dragObject.setOnTouchListener((v, event) -> {
            if (event.getAction() ==
                    MotionEvent.ACTION_DOWN) {
                mStartDragListener.requestDrag(holder);
            }
            return false;
        });
    }


    @Override
    public int getItemCount() {
        return data.size();
    }


    @Override
    public void onRowMoved(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(data, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(data, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onRowSelected(MyViewHolder myViewHolder) {
        myViewHolder.rowView.setBackgroundColor(Color.GRAY);

    }

    @Override
    public void onRowClear(MyViewHolder myViewHolder) {
        myViewHolder.rowView.setBackgroundColor(Color.WHITE);

    }
}
