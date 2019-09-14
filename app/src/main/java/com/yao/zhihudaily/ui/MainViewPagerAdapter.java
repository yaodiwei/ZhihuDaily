package com.yao.zhihudaily.ui;

import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

/**
 * @author Yao
 * @date 2016/9/5
 */
public class MainViewPagerAdapter extends FragmentPagerAdapter {

    private List<BaseFragment> mBaseFragmentList = new ArrayList<>(4);
    private BaseFragment mCurrentFragment;

    public MainViewPagerAdapter(FragmentManager fm, ArrayList<BaseFragment> baseFragments) {
        super(fm);
        mBaseFragmentList.addAll(baseFragments);
    }

    @Override
    public Fragment getItem(int position) {
        return mBaseFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mBaseFragmentList.size();
    }

    @Override
    public void setPrimaryItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        if (getCurrentFragment() != object) {
            mCurrentFragment = ((BaseFragment) object);
        }
        super.setPrimaryItem(container, position, object);
    }

    /**
     * Get the current fragment
     */
    public BaseFragment getCurrentFragment() {
        return mCurrentFragment;
    }
}
