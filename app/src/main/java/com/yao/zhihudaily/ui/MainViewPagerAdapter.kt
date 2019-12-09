package com.yao.zhihudaily.ui

import android.view.ViewGroup

import java.util.ArrayList
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

/**
 * @author Yao
 * @date 2016/9/5
 */
class MainViewPagerAdapter(fm: FragmentManager, baseFragments: ArrayList<BaseFragment>) : FragmentPagerAdapter(fm) {

    private val mBaseFragmentList = ArrayList<BaseFragment>(4)
    /**
     * Get the current fragment
     */
    var currentFragment: BaseFragment? = null
        private set

    init {
        mBaseFragmentList.addAll(baseFragments)
    }

    override fun getItem(position: Int): Fragment {
        return mBaseFragmentList[position]
    }

    override fun getCount(): Int {
        return mBaseFragmentList.size
    }

    override fun setPrimaryItem(container: ViewGroup, position: Int, `object`: Any) {
        if (currentFragment !== `object`) {
            currentFragment = `object` as BaseFragment
        }
        super.setPrimaryItem(container, position, `object`)
    }
}
