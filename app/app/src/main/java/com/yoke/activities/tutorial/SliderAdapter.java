package com.yoke.activities.tutorial;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yoke.R;

public class SliderAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    public SliderAdapter (Context context) {
        this.context = context;
    }

    /**
     * The images for on the slides
     */
    public int[] slide_images = {
            R.drawable.eat_icon,
            R.drawable.sleep_icon,
            R.drawable.code_icon
    };

    /**
     * The headings for on the slides
     */
    public int[] slide_headings = {
            R.string.heading1,
            R.string.heading2,
            R.string.heading3
    };

    /**
     * The descriptions for on the slides
     */
    public int[] slide_descs = {
            R.string.description1,
            R.string.description2,
            R.string.description3
    };

    /**
     * Returns the amount of slides
     * @return number of slides
     */
    @Override
    public int getCount() {
        return slide_headings.length;
    }

    /**
     * Check whether the view is equal to the object
     * @param view view to be checked
     * @param o object to be checked
     * @return boolean whether view is equal to object
     */
    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    /**
     * creates current slide
     * @param container holder of slides
     * @param position id of current slide
     * @return view with slide
     */
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_layout, container, false);

        ImageView slideImageView = view.findViewById(R.id.slide_image);
        TextView slideHeading = view.findViewById(R.id.slide_heading);
        TextView slideDescription = view.findViewById(R.id.slide_text);

        slideImageView.setImageResource(slide_images[position]);
        slideHeading.setText(slide_headings[position]);
        slideDescription.setText(slide_descs[position]);

        container.addView(view);

        return view;
    }

    /**
     * Destroys layout that contained the slides
     * @param container holder for slide layout
     * @param position current slide
     * @param object layout to be removed
     */
    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ConstraintLayout) object);
    }
}
