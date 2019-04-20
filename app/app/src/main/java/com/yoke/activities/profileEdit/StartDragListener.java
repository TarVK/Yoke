package com.yoke.activities.profileEdit;

import android.support.v7.widget.RecyclerView;

/**
 * src: https://www.journaldev.com/23208/android-recyclerview-drag-and-drop
 */

//interface for dragging listener
public interface StartDragListener {
    void requestDrag(RecyclerView.ViewHolder viewHolder);
}
