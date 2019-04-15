package com.yoke.activities.mouse;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;

import com.example.yoke.R;
import com.yoke.activities.BaseActivity;
import com.yoke.connection.Connection;
import com.yoke.connection.client.MultiClientConnection;
import com.yoke.connection.messages.ClickMouseCmd;
import com.yoke.connection.messages.MoveMouseCmd;

/**
 * A class to create a trackpad activity that sends mouse events to the server
 */
public class MouseActivity extends BaseActivity {
    // The connection to send the events through
    protected Connection connection = MultiClientConnection.getInstance();

    // Keep track of the previous values
    protected float lastX = 0;
    protected float lastY = 0;

    // Keep track of how far we dragged
    protected float dragDistance = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mouse);
        Toolbar toolbar = findViewById(R.id.toolbar);
        this.setNewToolbarColour(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Track Pad");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Get the touchpad pane
        View view = findViewById(R.id.touchpad);

        // Add the event listener to check for dragging
        view.setOnTouchListener(new View.OnTouchListener(){
            public boolean onTouch(View v, MotionEvent m) {
                float x = m.getAxisValue(MotionEvent.AXIS_X);
                float y = m.getAxisValue(MotionEvent.AXIS_Y);

                // Check if the tocuhpad touch is started
                if (m.getAction() == android.view.MotionEvent.ACTION_DOWN) {
                    lastX = x;
                    lastY = y;
                    dragDistance = 0;
                }
                // Check if the touchpad is released
                else if (m.getAction() == android.view.MotionEvent.ACTION_UP) {
                    if (dragDistance < 40) {
                        // Send a mouse click if you didn't drag
                        connection.send(new ClickMouseCmd(ClickMouseCmd.LEFTCLICK));
                    }
                }

                // Otherwise, do the drag even
                else {
                    float dx = x - lastX;
                    float dy = y - lastY;

                    dragDistance += Math.sqrt(dx * dx + dy * dy);

                    // Move the mouse
                    connection.send(new MoveMouseCmd((int) dx, (int) dy));

                    lastX = x;
                    lastY = y;
                }
                return true;
            }

        });
    }

    // To go back to the previous page
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
