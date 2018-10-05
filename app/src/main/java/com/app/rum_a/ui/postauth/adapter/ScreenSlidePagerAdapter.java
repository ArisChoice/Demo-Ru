package com.app.rum_a.ui.postauth.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.app.rum_a.ui.postauth.fragments.HomeFragment;
import com.app.rum_a.ui.postauth.fragments.MyProperties;

/**
 * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
 * sequence.
 */
public class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
    private static final int NUM_PAGES = 1;

    public ScreenSlidePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new MyProperties();
//            case 1:
//                return new MyProperties();
            default:
                return new MyProperties();
        }

    }

    @Override
    public int getCount() {
        return NUM_PAGES;
    }
}
