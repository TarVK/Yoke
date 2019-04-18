package com.yoke.activities.splash;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.View;

import com.example.yoke.R;

import java.io.InputStream;

public class GifView extends View {

    private InputStream gifInputStream;
    private Movie gifMovie;
    private int movieWidth;
    private int movieHeight;
    private long movieDuration;
    private long movieStart;

    /**
     * Constructor
     * @param context
     */
    public GifView(Context context) {
        super(context);
        init(context);
    }

    /**
     * Constructor
     * @param context
     * @param attrs
     */
    public GifView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    /**
     * Constructor
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public GifView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /**
     * Get the information for the gif
     * @param context
     */
    private void init(Context context) {
        setFocusable(true);
        gifInputStream = context.getResources().openRawResource(R.drawable.yoke);

        gifMovie = Movie.decodeStream(gifInputStream);
        movieWidth = gifMovie.width();
        movieHeight = gifMovie.height();
        movieDuration = gifMovie.duration();
    }

    /**
     * Sets the gifview to have the same size as the gif
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(movieWidth, movieHeight);
    }

    /**
     * Getter for the width of the gif
     * @return
     */
    public int getMovieWidth() {
        return movieWidth;
    }

    /**
     * Getter for the height of the gif
     * @return
     */
    public int getMovieHeight() {
        return movieHeight;
    }

    /**
     * Getter for the duration of the gif
     * @return
     */
    public long getMovieDuration() {
        return movieDuration;
    }

    /**
     * Draws the gifview
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        long now = SystemClock.uptimeMillis();

        if (movieStart == 0) {
            movieStart = now;
        }

        if (gifMovie != null) {
            int dur = gifMovie.duration();

            if (dur == 0) {
                dur = 1000;
            }

            int relTime = (int)((now - movieStart) % dur);

            gifMovie.setTime(relTime);

            gifMovie.draw(canvas, 0, 0);
            invalidate();
        }
    }
}
