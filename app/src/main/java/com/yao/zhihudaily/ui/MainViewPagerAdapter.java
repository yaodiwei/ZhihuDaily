package com.yao.zhihudaily.ui;

import android.app.Fragment;
import android.app.FragmentManager;
import android.view.ViewGroup;

import com.yao.zhihudaily.tool.FragmentPagerAdapter;

import java.util.ArrayList;

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
