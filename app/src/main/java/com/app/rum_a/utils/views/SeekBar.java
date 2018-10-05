package com.app.rum_a.utils.views;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

/**
 * Created by tarun on 16/6/15.
 */
public class SeekBar extends android.support.v7.widget.AppCompatSeekBar {

    Drawable mThumb;
    public SeekBar(Context context) {
        super(context);
    }

    public SeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public void setThumb(Drawable thumb) {
        super.setThumb(thumb);
        this.mThumb = thumb;
    }


    public Drawable getThumb(){
        return mThumb;
    }



}
