package com.yoke.activities.profileEdit;

import android.support.v7.widget.RecyclerView;

//interface for dragging listener
public interface StartDragListener {
    void requestDrag(RecyclerView.ViewHolder viewHolder);
}
