package com.yoke.activities.macro.tabs;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yoke.R;
import com.yoke.connection.Connection;
import com.yoke.connection.RepeatMessage;
import com.yoke.connection.client.MultiClientConnection;

import java.util.ArrayList;
import java.util.Collections;

public class MacroSequenceAdapter extends RecyclerView.Adapter<MacroSequenceAdapter.MyViewHolder> implements MacroSequenceItemMoveCallback.ItemTouchHelperContract {

    private static final String TAG = "MacroSequenceAdapter";

    private final MacroSequenceStartDragListener mStartDragListener;
    protected Connection connection;

    private ArrayList<RepeatMessage> mRepeatMessage;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        View rowView;

        TextView actionText;
        TextView editRepeat;
        TextView editDelay;
        ImageView increaseRepeat;
        ImageView decreaseRepeat;
        ImageView increaseDelay;
        ImageView decreaseDelay;
        ImageView draggable;

        public MyViewHolder(View itemView) {
            super(itemView);

            rowView = itemView;
            actionText = itemView.findViewById(R.id.actionText);
            editRepeat = itemView.findViewById(R.id.editRepeat);
            editDelay = itemView.findViewById(R.id.editDelay);
            increaseRepeat = itemView.findViewById(R.id.increaseRepeat);
            decreaseRepeat = itemView.findViewById(R.id.decreaseRepeat);
            increaseDelay = itemView.findViewById(R.id.increaseDelay);
            decreaseDelay = itemView.findViewById(R.id.decreaseDelay);
            draggable = itemView.findViewById(R.id.draggable);
        }
    }

    public MacroSequenceAdapter(ArrayList<RepeatMessage> mRepeatMessage, MacroSequenceStartDragListener startDragListener, Context context) {
        mStartDragListener = startDragListener;
        this.mRepeatMessage = mRepeatMessage;
        connection = MultiClientConnection.getInstance(context); // Gets the connection
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_sequence_action, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: called. ");

        RepeatMessage rMessage = mRepeatMessage.get(position);
        CommandRequester commandRequester = new CommandRequester(holder.rowView.getContext());

        holder.actionText.setText(rMessage.message.toString());
        holder.editRepeat.setText(String.valueOf(rMessage.repeatAmount));
        holder.editDelay.setText(String.valueOf(rMessage.frequency));

        holder.actionText.setOnClickListener(v -> {
            commandRequester.requestCommand(action -> {
                if (action != null) {
                    holder.actionText.setText(action.toString());
                    rMessage.message = action;
                } else {
                    Toast.makeText(holder.actionText.getContext(),
                            holder.actionText.getContext().getString(R.string.selectMacroFailed),
                            Toast.LENGTH_LONG).show();
                }
            });
        });

        holder.increaseRepeat.setOnClickListener(v -> {
            int repeat = rMessage.repeatAmount + 1;
            rMessage.repeatAmount = repeat;
            holder.editRepeat.setText(String.valueOf(repeat));
        });

        holder.decreaseRepeat.setOnClickListener(v -> {
            int repeat = rMessage.repeatAmount - 1;
            if (repeat > 0) {
                rMessage.repeatAmount = repeat;
                holder.editRepeat.setText(String.valueOf(repeat));
            }
        });

        holder.increaseDelay.setOnClickListener(v -> {
            double delay = rMessage.frequency + 1;
            rMessage.frequency = delay;
            holder.editDelay.setText(String.valueOf(delay));
        });

        holder.decreaseDelay.setOnClickListener(v -> {
            double delay = rMessage.frequency - 1;
            if (delay > 0) {
                rMessage.frequency = delay;
                holder.editDelay.setText(String.valueOf(delay));
            }
        });

        holder.editRepeat.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (holder.editRepeat.getText().length() > 0) {
                    String text = holder.editRepeat.getText().toString();
                    int number = Integer.parseInt(text);
                    if (number > 0) {
                        rMessage.repeatAmount = number;
                    } else {
                        holder.editRepeat.setText("1");
                    }
                }
            }
        });

        holder.editDelay.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (holder.editDelay.getText().length() > 0) {
                    String text = holder.editDelay.getText().toString();
                    rMessage.frequency = Double.parseDouble(text);
                }
            }
        });

        holder.draggable.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                mStartDragListener.requestDrag(holder);
            }
            return false;
        });

        holder.rowView.setOnLongClickListener(v -> {
            new AlertDialog.Builder(v.getContext())
                    .setTitle("Delete action")
                    .setMessage("Are you sure you want to delete this action?")
                    .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                        mRepeatMessage.remove(position);
                        notifyDataSetChanged();
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .show();
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return mRepeatMessage.size();
    }

    @Override
    public void onRowMoved(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(mRepeatMessage, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(mRepeatMessage, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onRowSelected(MyViewHolder myViewHolder) {
        myViewHolder.rowView.setBackgroundColor(Color.LTGRAY);
    }

    @Override
    public void onRowClear(MyViewHolder myViewHolder) {
        myViewHolder.rowView.setBackgroundColor(Color.WHITE);

    }
}