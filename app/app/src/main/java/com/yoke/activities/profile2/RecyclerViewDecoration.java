package com.yoke.activities.profile2;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class RecyclerViewDecoration extends RecyclerView.ItemDecoration {

    private int mSpace;
    private int mMargin;

    public RecyclerViewDecoration(int space, int margin) {
        mSpace = space;
        mMargin = margin;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {

        outRect.left = mSpace;
        outRect.right = mSpace;
        outRect.bottom = mSpace;

    }
}
