package com.yao.zhihudaily.ui.daily

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.PagerAdapter
import java.util.*

/**
 *
 * @author Yao
 * @date 2016/9/4
 */
class CommentPagerAdapter : FragmentPagerAdapter {

    private var mFragments: ArrayList<Fragment>? = null
    private lateinit var mTitles: ArrayList<String>
    private var mFragmentManager: FragmentManager? = null

    constructor(fragmentManager: FragmentManager) : super(fragmentManager) {
        this.mFragmentManager = fragmentManager
    }

    constructor(fragmentManager: FragmentManager, fragments: ArrayList<Fragment>, titles: ArrayList<String>) : super(fragmentManager) {
        this.mFragmentManager = fragmentManager
        this.mFragments = fragments
        this.mTitles = titles
    }

    override fun getItem(position: Int): Fragment {
        return mFragments!![position]
    }

    override fun getCount(): Int {
        return mFragments!!.size
    }

    override fun getItemPosition(`object`: Any): Int {
        return PagerAdapter.POSITION_NONE
    }

    fun setFragments(fragments: ArrayList<Fragment>) {
        if (this.mFragments != null) {
            val ft = mFragmentManager!!.beginTransaction()
            for (f in this.mFragments!!) {
                ft.remove(f)
            }
            ft.commit()
            mFragmentManager!!.executePendingTransactions()
        }
        this.mFragments = fragments
        notifyDataSetChanged()
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mTitles[position]
    }
}
