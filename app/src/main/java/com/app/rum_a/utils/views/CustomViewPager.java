package com.app.rum_a.utils.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.app.rum_a.R;


/**
 * Created by Harish on 30/5/16.
 */
public class CustomViewPager extends ViewPager {
    public boolean swipeable;

    //    private float initialXValue;
//    private SwipeDirection direction;
    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MyViewPager);
        try {
            swipeable = a.getBoolean(R.styleable.MyViewPager_swipeable, true);
        } finally {
            a.recycle();
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return swipeable ? super.onInterceptTouchEvent(event) : false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return swipeable ? super.onTouchEvent(event) : false;
    }

    public void setPagingEnabled(boolean enabled) {
        this.swipeable = enabled;
    }
}