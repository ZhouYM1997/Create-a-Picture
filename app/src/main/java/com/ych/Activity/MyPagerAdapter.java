package com.ych.Activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


import java.util.List;

class MyPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> mlist;

    public MyPagerAdapter(FragmentManager fragmentManager, List<Fragment> list){
        super(fragmentManager);
        mlist = list;
    }

    @Override
    public Fragment getItem(int position) {
        return mlist.get(position);
    }

    @Override
    public int getCount() {
        return mlist.size();
    }
}
