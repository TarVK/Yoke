package com.yoke.activities.tutorial;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.yoke.R;
import com.yoke.activities.BaseActivity;
import com.yoke.activities.settings.Settings;
import com.yoke.activities.splash.SplashActivity;

public class TutorialActivity extends BaseActivity {

    private ViewPager mSlideViewPager;
    private LinearLayout mDotLayout;

    private TextView[] mDots;

    private SliderAdapter sliderAdapter;

    private Button mNextBtn;
    private Button mBackBtn;

    private int mCurrentPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        mSlideViewPager = findViewById(R.id.slideViewPager);
        mDotLayout = findViewById(R.id.dotsLayout);

        mNextBtn = findViewById(R.id.nextBtn);
        mBackBtn = findViewById(R.id.prevBtn);

        sliderAdapter = new SliderAdapter(this);

        mSlideViewPager.setAdapter(sliderAdapter);

        addDotsIndicator(0);

        mSlideViewPager.addOnPageChangeListener(viewListener);

        /**
         * The onClickListener for the next button
         */
        mNextBtn.setOnClickListener(closeTutView -> {
            if( mCurrentPage == mDots.length - 1) {
                closeTutorial();
            } else {
                mSlideViewPager.setCurrentItem(mCurrentPage + 1);
            }
        });

        /**
         * The onClickListener for the back button
         */
        mBackBtn.setOnClickListener(goBackView -> mSlideViewPager.setCurrentItem(mCurrentPage - 1));
    }

    /**
     * Creates the dots that represent the slides.
     * The dot for the current page is in a different colour.
     *
     * @param position current slide
     */
    public void addDotsIndicator(int position) {
        mDotLayout.removeAllViews();
        mDots = new TextView[3];

        for(int i = 0; i < mDots.length; i++) {
            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226;"));
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(getResources().getColor(R.color.colorTransparentWhite));
            mDotLayout.addView(mDots[i]);
        }

        if(mDots.length > 0) {
            mDots[position].setTextColor(getResources().getColor(R.color.colorWhite));
        }
    }

    /**
     * Create new viewListener for the viewPager
     */
    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        /**
         * Gives the buttons the correct text and visibility depending on the current slide
         * @param i current slide
         */
        @Override
        public void onPageSelected(int i) {
            addDotsIndicator(i);
            mCurrentPage = i;

            if(i == 0) {

                mNextBtn.setEnabled(true);
                mBackBtn.setEnabled(false);
                mBackBtn.setVisibility(View.INVISIBLE);

                mNextBtn.setText(R.string.nextBtn);
                mBackBtn.setText("");

            } else if( i == mDots.length - 1) {

                mNextBtn.setEnabled(true);
                mBackBtn.setEnabled(true);
                mBackBtn.setVisibility(View.VISIBLE);

                mNextBtn.setText(R.string.finishBtn);
                mBackBtn.setText(R.string.backBtn);

            } else {

                mNextBtn.setEnabled(true);
                mBackBtn.setEnabled(true);
                mBackBtn.setVisibility(View.VISIBLE);

                mNextBtn.setText(R.string.nextBtn);
                mBackBtn.setText(R.string.backBtn);

            }
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    };

    /**
     * Closes the tutorial
     */
    public void closeTutorial() {
        Intent intent = new Intent(this, Settings.class);
        startActivity(intent);
    }
}
