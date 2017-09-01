package com.example.android.map.RouteDesign;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Howard on 28/08/2017.
 */

public class SectionPagerAdapter extends FragmentPagerAdapter {

    private static final String TAG = SectionPagerAdapter.class.getSimpleName();
    private final List<Fragment> mFragmentList = new ArrayList<Fragment>();
    public SectionPagerAdapter(FragmentManager fm) {super(fm);}

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }
    @Override
    public int getCount(){
        return mFragmentList.size();
    }

    public void addFragment(Fragment fragment){mFragmentList.add(fragment);
    }
}
