package com.yoke.activities.home;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.yoke.activities.profileEdit.ButtonsEditRecyclerViewAdapter;

public class DragController extends ItemTouchHelper.Callback {

    private final ItemTouchHelperContract mAdapter;
    public boolean isDraggable = true;

    public DragController(ItemTouchHelperContract adapter) {
        mAdapter = adapter;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView,
                                @NonNull RecyclerView.ViewHolder viewHolder) {

        //flags define the movement direction for each movement states
        int dragFlags = isDraggable ? ItemTouchHelper.UP | ItemTouchHelper.DOWN : 0;

        return makeMovementFlags(dragFlags, 0);

    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView,
                          @NonNull RecyclerView.ViewHolder viewHolder,
                          @NonNull RecyclerView.ViewHolder target) {

        mAdapter.onRowMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    //only move the activated items
    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            if (viewHolder instanceof ButtonsEditRecyclerViewAdapter.ViewHolder) {
                ButtonsEditRecyclerViewAdapter.ViewHolder myViewHolder =
                        (ButtonsEditRecyclerViewAdapter.ViewHolder) viewHolder;
                mAdapter.onRowSelected(myViewHolder);
            }
        }

        super.onSelectedChanged(viewHolder, actionState);
    }

    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);

        if (viewHolder instanceof ButtonsEditRecyclerViewAdapter.ViewHolder) {
            ButtonsEditRecyclerViewAdapter.ViewHolder myViewHolder =
                    (ButtonsEditRecyclerViewAdapter.ViewHolder) viewHolder;
            mAdapter.onRowClear(myViewHolder);
        }
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

    }


    public interface ItemTouchHelperContract {

        void onRowMoved(int fromPosition, int toPosition);
        void onRowSelected(ButtonsEditRecyclerViewAdapter.ViewHolder myViewHolder);
        void onRowClear(ButtonsEditRecyclerViewAdapter.ViewHolder myViewHolder);


    }
}
