package com.app.rum_a.utils.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.app.rum_a.R;

/**
 * Created by tarun on 27/08/2018.
 */
public class RumCheckBox extends android.support.v7.widget.AppCompatCheckBox {


    public RumCheckBox(Context context) {
        super(context);
        init(null);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if ((!isClickable()) && (event.getAction() == MotionEvent.ACTION_DOWN))
            return super.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    public RumCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public RumCheckBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }


    public void init(AttributeSet attrs) {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Raleway_Regular.ttf");
        Typeface tfBold = Typeface.createFromAsset(getContext().getAssets(), "fonts/Raleway_SemiBold.ttf");
        TypedArray a = null;
        setCursorVisible(true);

        try {
            a = getContext().obtainStyledAttributes(attrs, R.styleable.CheckBox);
            if (a.getBoolean(R.styleable.CheckBox_isBoldCheckText, false)) {

                setTypeface(tfBold, Typeface.BOLD);
            } else {
                setTypeface(tf, Typeface.NORMAL);
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}
