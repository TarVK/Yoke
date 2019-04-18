package com.yoke.activities.macro.tabs;

import android.support.v7.widget.RecyclerView;

public interface MacroSequenceStartDragListener {

    /**
     * Method description for the Drag 'n Drop functionality
     * @param viewHolder MacroSequence item to specify for its position
     */
    void requestDrag(RecyclerView.ViewHolder viewHolder);
}
