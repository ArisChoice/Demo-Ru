package com.app.rum_a.utils;


import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringListener;
import com.facebook.rebound.SpringSystem;

public class SpringAnimation implements SpringListener, View.OnTouchListener {

    ImageView view;
    private Spring mSpring;

    public SpringAnimation() {
        SpringSystem mSpringSystem = SpringSystem.create();
        mSpring = mSpringSystem.createSpring();
        mSpring.addListener(this);
        double TENSION = 800;
        double FRICTION = 20;
        SpringConfig config = new SpringConfig(TENSION, FRICTION);
        mSpring.setSpringConfig(config);
    }

    public void setAnimation(ImageView imageView) {
        imageView.setOnTouchListener(this);
        imageView.setTag(imageView);
    }

    public void setAnimation(float offset, ImageView view) {
        this.view = view;
        mSpring.setEndValue(offset);
        if(offset == 0f){
            view.setScaleX(1.0f);
            view.setScaleY(1.0f);
        }
    }

    @Override
    public void onSpringUpdate(Spring spring) {

        float value = (float) spring.getCurrentValue();
        float scale = 1f - (value * 0.2f);
        view.setScaleX(scale);
        view.setScaleY(scale);
    }

    @Override
    public void onSpringAtRest(Spring spring) {

    }

    @Override
    public void onSpringActivate(Spring spring) {

    }

    @Override
    public void onSpringEndStateChange(Spring spring) {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        view = (ImageView) v.getTag();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mSpring.setEndValue(0.4f);
                return false;
            case MotionEvent.ACTION_UP:
                mSpring.setEndValue(0f);
                return false;
            case MotionEvent.ACTION_MOVE:
                return false;
            case MotionEvent.ACTION_CANCEL:
                mSpring.setEndValue(0f);
                return false;
        }
        return false;
    }
}


