package com.example.qanh.appmusic;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by Q.Anh on 28/04/2017.
 */

public class PagerAdapter extends FragmentStatePagerAdapter {
    public Fragment1 mFragment1;
    public Fragment2 mFragment2;
    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                mFragment1 = new Fragment1();
                return mFragment1;
            case 1:
                mFragment2 = new Fragment2();
                return mFragment2;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
