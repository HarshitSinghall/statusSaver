package com.whatsappbusiness.statussaver;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class SliderAdapter extends PagerAdapter {

    Context context;

    public SliderAdapter(Context context) {
        this.context = context;
    }

    int images[] = {
            R.drawable.onboarding_1,
            R.drawable.onboarding_2,
            R.drawable.onboarding_3,
            R.drawable.onboarding_4,
            R.drawable.onboarding_5,
            R.drawable.onboarding_6,
            R.drawable.onboarding_7,
            R.drawable.onboarding_8
    };

    int titles[] = {
            R.string.onBoarding_title_1,
            R.string.onBoarding_title_2,
            R.string.onBoarding_title_3,
            R.string.onBoarding_title_4,
            R.string.onBoarding_title_5,
            R.string.onBoarding_title_6,
            R.string.onBoarding_title_7,
            R.string.onBoarding_title_8
    };


    int desc[] = {
            R.string.onBoarding_desc_1,
            R.string.onBoarding_desc_2,
            R.string.onBoarding_desc_3,
            R.string.onBoarding_desc_4,
            R.string.onBoarding_desc_5,
            R.string.onBoarding_desc_6,
            R.string.onBoarding_desc_7,
            R.string.onBoarding_desc_8
    };


    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (ConstraintLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        View view = LayoutInflater.from(context).inflate(R.layout.slider_layout, container, false);

        TextView titleText = view.findViewById(R.id.slider_heading);
        TextView descText = view.findViewById(R.id.slider_desc);
        ImageView imageView = view.findViewById(R.id.slider_image);

        titleText.setText(titles[position]);
        descText.setText(desc[position]);
        imageView.setImageResource(images[position]);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(View container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }
}
