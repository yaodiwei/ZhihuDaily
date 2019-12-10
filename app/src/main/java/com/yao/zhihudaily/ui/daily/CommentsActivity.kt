package com.yao.zhihudaily.ui.daily

import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.yao.zhihudaily.R
import com.yao.zhihudaily.model.StoryExtra
import com.yao.zhihudaily.net.UrlConstants
import com.yao.zhihudaily.tool.Constant
import com.yao.zhihudaily.ui.BaseActivity
import kotlinx.android.synthetic.main.activity_comments.*
import java.util.*

/**
 *
 * @author Yao
 * @date 2016/8/30
 */
class CommentsActivity : BaseActivity() {

    private var mStoryExtra: StoryExtra? = null
    private val mFragmentArrayList = ArrayList<Fragment>(2)

    private val pageListener = object : ViewPager.OnPageChangeListener {


        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

        }

        override fun onPageSelected(position: Int) {

        }

        override fun onPageScrollStateChanged(state: Int) {

        }
    }


    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comments)

        val id = intent.getIntExtra(Constant.ID, 0)
        mStoryExtra = intent.getSerializableExtra(Constant.STORY_EXTRA) as StoryExtra

        initToolbar(toolbar)


        val tabList = ArrayList<String>()
        tabList.add("短评论(" + mStoryExtra!!.shortComments + ")")
        tabList.add("长评论(" + mStoryExtra!!.longComments + ")")
        tab_layout!!.addTab(tab_layout!!.newTab().setText(tabList[0]))//添加tab选项卡
        tab_layout!!.addTab(tab_layout!!.newTab().setText(tabList[1]))

        //短评论界面
        val shortCommentsFragment = CommentsFragment()
        val bundleForShortComments = Bundle()
        bundleForShortComments.putInt(Constant.ID, id)
        bundleForShortComments.putString(Constant.URL, UrlConstants.SHORT_COMMENTS)
        bundleForShortComments.putInt(Constant.COUNT, mStoryExtra!!.shortComments)
        shortCommentsFragment.arguments = bundleForShortComments

        //长评论界面
        val longCommentsFragment = CommentsFragment()
        val bundleForLongComments = Bundle()
        bundleForLongComments.putInt(Constant.ID, id)
        bundleForLongComments.putString(Constant.URL, UrlConstants.LONG_COMMENTS)
        bundleForShortComments.putInt(Constant.COUNT, mStoryExtra!!.longComments)
        longCommentsFragment.arguments = bundleForLongComments

        mFragmentArrayList.add(shortCommentsFragment)
        mFragmentArrayList.add(longCommentsFragment)
        val adapter = CommentPagerAdapter(supportFragmentManager, mFragmentArrayList, tabList)
        //view_pager.setOffscreenPageLimit(0);
        view_pager!!.adapter = adapter
        view_pager!!.addOnPageChangeListener(pageListener)

        tab_layout!!.setupWithViewPager(view_pager)//将TabLayout和ViewPager关联起来。
        tab_layout!!.setTabsFromPagerAdapter(adapter)//给Tabs设置适配器
    }

    private fun initToolbar(toolbar: Toolbar) {
        toolbar.setNavigationIcon(R.mipmap.back)//设置导航栏图标
        toolbar.title = "共" + mStoryExtra!!.shortComments + "条"//设置主标题
        toolbar.setNavigationOnClickListener { v -> finish() }
    }
}
