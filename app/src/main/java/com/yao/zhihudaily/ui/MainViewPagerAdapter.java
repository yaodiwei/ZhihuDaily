package com.yao.zhihudaily.ui;

import android.view.ViewGroup;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

/**
 * Created by Administrator on 2016/9/4.
 */
public class MainViewPagerAdapter extends FragmentPagerAdapter {
    private ArrayList<MainFragment> fragments = new ArrayList<>();
    private MainFragment currentFragment;

    public MainViewPagerAdapter(FragmentManager fm, ArrayList<MainFragment> mainFragments) {
        super(fm);
        fragments.addAll(mainFragments);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        if (getCurrentFragment() != object) {
            currentFragment = ((MainFragment) object);
        }
        super.setPrimaryItem(container, position, object);
    }

    /**
     * Get the current fragment
     */
    public MainFragment getCurrentFragment() {
        return currentFragment;
    }
}
