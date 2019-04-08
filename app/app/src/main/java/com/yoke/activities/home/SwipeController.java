package com.yoke.activities.home;

import android.graphics.Canvas;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;

import com.example.yoke.R;

import java.util.HashMap;

/**
 * A class to handle the swiping of profiles to reveal the delete option
 * Source of partially followed guide: https://codeburst.io/android-swipe-menu-with-recyclerview-8f28a235ff28
 */
public class SwipeController extends ItemTouchHelper.Callback {
    // The width that a layout_button should have
    protected static float buttonWidth = -1;

    // The initial x position of the card view
    protected static float xOffset = -1;

    // Whether the item should move back to it's original position (instead of going off the screen)
    protected boolean swipeBack = false;

    // Track whether an viewholder's layout_button has been swiped right
    protected HashMap<RecyclerView.ViewHolder, Boolean> buttonSwiped = new HashMap<>();

    // Whether or not the item is resetting after the layout_button having been visible
    protected HashMap<RecyclerView.ViewHolder, Boolean> buttonOffset = new HashMap<>();

    // Store a reference to the activity
    protected HomeActivity activity;

    /**
     * Creates an instance of the SwipeController
     * @param activity  A reference to the home activity
     */
    SwipeController(HomeActivity activity) {
        this.activity = activity;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(0,
                ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
    }

    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
        if (swipeBack) {
            swipeBack = false;
            return 0;
        }
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }

    @Override
    public void onChildDraw(Canvas c,
                            RecyclerView recyclerView,
                            RecyclerView.ViewHolder viewHolder,
                            float dX, float dY,
                            int actionState, boolean isCurrentlyActive) {

        // Get the card view to move
        CardView cv = viewHolder.itemView.findViewById(R.id.card);

        // Get the layout_button width
        if (buttonWidth == -1) {
            ImageButton button = viewHolder.itemView.findViewById(R.id.delete);
            buttonWidth = button.getWidth();
        }

        // Get the xOffset for the card view
        if (xOffset == -1) {
            xOffset = cv.getX();
        }

        // Track when the item should return to the left
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            setTouchListener(recyclerView);
        }

        // If we have swipped right far enough, make the layout_button visible
        if (dX > buttonWidth) {
            buttonSwiped.put(viewHolder, true);
            activity.dragController.isDraggable = false;
        }

        if (dX < buttonWidth) {
            // If we swiped left far enough, we should indicate this
            if (isCurrentlyActive) {
                buttonSwiped.put(viewHolder, false);

            // If we swiped right far enough, we should lock the layout_button to being visible
            } else if(buttonSwiped.get(viewHolder) != null && buttonSwiped.get(viewHolder)) {
                // We should also indicate the item has an offset of the layout_button's width when starting swipping back
                buttonOffset.put(viewHolder, true);

                cv.setX(xOffset + buttonWidth);
                super.onChildDraw(c, recyclerView, viewHolder, 0, dY, actionState, isCurrentlyActive);
                return;
            }
        }

        // If the item has an offset
        if (buttonOffset.get(viewHolder) != null && buttonOffset.get(viewHolder)) {
            // Reset this offset if the item is swiped left far enough
            if (dX <= -buttonWidth) {
                buttonOffset.put(viewHolder, false);

                new Handler().postDelayed(() -> {
                    activity.dragController.isDraggable = true;
                }, 400);
            }

            // Add the offset to the x position
            dX += buttonWidth;
        }

        // Make sure x is never smaller than 0
        if (dX < 0) {
            dX = 0;
        }

        cv.setX(xOffset + dX);

        super.onChildDraw(c, recyclerView, viewHolder, 0, dY, actionState, isCurrentlyActive);
    }

    /**
     * Adds a touch listener to the recycler view to update swipeBack
     * @param recyclerView  The recycler view to add the listener to
     */
    private void setTouchListener(RecyclerView recyclerView) {
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                swipeBack = event.getAction() == MotionEvent.ACTION_CANCEL
                         || event.getAction() == MotionEvent.ACTION_UP;

                return false;
            }
        });
    }
};