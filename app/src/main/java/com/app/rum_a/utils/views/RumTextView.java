package com.app.rum_a.utils.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;

import com.app.rum_a.R;


/**
 * Created by Harish on 26/08/18.
 */
public class RumTextView extends android.support.v7.widget.AppCompatTextView {


    public RumTextView(Context context) {
        super(context);
        init(null);
    }

    public RumTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public RumTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public void init(AttributeSet attrs) {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Raleway_Regular.ttf");
        Typeface tfBold = Typeface.createFromAsset(getContext().getAssets(), "fonts/Raleway_SemiBold.ttf");
        TypedArray a = null;
        try {
            a = getContext().obtainStyledAttributes(attrs, R.styleable.TextView);
            if (a.getBoolean(R.styleable.TextView_isBoldTextView, false)) {
                setTypeface(tfBold, Typeface.BOLD);
            } else {
                setTypeface(tf, Typeface.NORMAL);
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}
