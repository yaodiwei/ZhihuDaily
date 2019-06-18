package com.yao.zhihudaily.ui;

import android.view.ViewGroup;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

/**
 * @author Yao
 * @date 2016/9/5
 */
public class MainViewPagerAdapter extends FragmentPagerAdapter {
    private ArrayList<BaseFragment> fragments = new ArrayList<>();
    private BaseFragment currentFragment;

    public MainViewPagerAdapter(FragmentManager fm, ArrayList<BaseFragment> baseFragments) {
        super(fm);
        fragments.addAll(baseFragments);
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
    public void setPrimaryItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        if (getCurrentFragment() != object) {
            currentFragment = ((BaseFragment) object);
        }
        super.setPrimaryItem(container, position, object);
    }

    /**
     * Get the current fragment
     */
    public BaseFragment getCurrentFragment() {
        return currentFragment;
    }
}
