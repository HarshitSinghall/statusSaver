package com.whatsappbusiness.statussaver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class onBoardingActivity extends AppCompatActivity {


    //Variables
    ViewPager viewPager;
    LinearLayout dotsLayout;
    SliderAdapter sliderAdapter;
    TextView[] dots;
    Button letsGetStarted, nextBtn;
    SharedPreferences sp;
    Animation animation;
    int currentPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding);

        sp = getSharedPreferences("MY_PREF", MODE_PRIVATE);

        //Hooks
        viewPager = findViewById(R.id.slider);
        dotsLayout = findViewById(R.id.dots);
        letsGetStarted = findViewById(R.id.get_started_btn);
        nextBtn = findViewById(R.id.next_btn);

        //Call adapter
        sliderAdapter = new SliderAdapter(this);
        viewPager.setAdapter(sliderAdapter);

        //Dots
        addDots(0);
        viewPager.addOnPageChangeListener(changeListener);

        letsGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp.edit().putBoolean("onBoarding", true).apply();
                startActivity(new Intent(onBoardingActivity.this, MainActivity.class));
                finish();
            }
        });

    }

    public void skip(View view) {
        sp.edit().putBoolean("onBoarding", true).apply();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    public void next(View view) {
        viewPager.setCurrentItem(currentPos + 1);
    }

    private void addDots(int position) {

        dots = new TextView[8];
        dotsLayout.removeAllViews();

        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);

            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0) {
            dots[position].setTextColor(getResources().getColor(R.color.primary));
        }

    }

    ViewPager.OnPageChangeListener changeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addDots(position);
            currentPos = position;

            if (position == 0) {
                letsGetStarted.setVisibility(View.INVISIBLE);
                nextBtn.setVisibility(View.VISIBLE);
            } else if (position == 1) {
                letsGetStarted.setVisibility(View.INVISIBLE);
                nextBtn.setVisibility(View.VISIBLE);
            } else if (position == 2) {
                letsGetStarted.setVisibility(View.INVISIBLE);
                nextBtn.setVisibility(View.VISIBLE);
            } else if (position == 3) {
                letsGetStarted.setVisibility(View.INVISIBLE);
                nextBtn.setVisibility(View.VISIBLE);
            } else if (position == 4) {
                letsGetStarted.setVisibility(View.INVISIBLE);
                nextBtn.setVisibility(View.VISIBLE);
            } else if (position == 5) {
                letsGetStarted.setVisibility(View.INVISIBLE);
                nextBtn.setVisibility(View.VISIBLE);
            } else if (position == 6) {
                letsGetStarted.setVisibility(View.INVISIBLE);
                nextBtn.setVisibility(View.VISIBLE);
            }else {
//                animation = AnimationUtils.loadAnimation(onBoardingActivity.this, R.anim.bottom_anim);
//                letsGetStarted.setAnimation(animation);
                letsGetStarted.setVisibility(View.VISIBLE);
                nextBtn.setVisibility(View.INVISIBLE);
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };


}

