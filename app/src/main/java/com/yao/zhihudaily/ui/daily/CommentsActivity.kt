package com.yao.zhihudaily.ui.daily

import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import butterknife.BindView
import butterknife.ButterKnife
import com.google.android.material.tabs.TabLayout
import com.yao.zhihudaily.R
import com.yao.zhihudaily.model.StoryExtra
import com.yao.zhihudaily.net.UrlConstants
import com.yao.zhihudaily.tool.Constant
import com.yao.zhihudaily.ui.BaseActivity
import java.util.*

/**
 *
 * @author Yao
 * @date 2016/8/30
 */
class CommentsActivity : BaseActivity() {

    @JvmField
    @BindView(R.id.tab_layout)
    internal var mTabLayout: TabLayout? = null
    @JvmField
    @BindView(R.id.view_pager)
    internal var mViewPager: ViewPager? = null
    @JvmField
    @BindView(R.id.toolbar)
    internal var mToolbar: Toolbar? = null

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
        ButterKnife.bind(this)

        val id = intent.getIntExtra(Constant.ID, 0)
        mStoryExtra = intent.getSerializableExtra(Constant.STORY_EXTRA) as StoryExtra

        initToolbar(mToolbar!!)


        val tabList = ArrayList<String>()
        tabList.add("短评论(" + mStoryExtra!!.shortComments + ")")
        tabList.add("长评论(" + mStoryExtra!!.longComments + ")")
        mTabLayout!!.addTab(mTabLayout!!.newTab().setText(tabList[0]))//添加tab选项卡
        mTabLayout!!.addTab(mTabLayout!!.newTab().setText(tabList[1]))

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
        //mViewPager.setOffscreenPageLimit(0);
        mViewPager!!.adapter = adapter
        mViewPager!!.addOnPageChangeListener(pageListener)

        mTabLayout!!.setupWithViewPager(mViewPager)//将TabLayout和ViewPager关联起来。
        mTabLayout!!.setTabsFromPagerAdapter(adapter)//给Tabs设置适配器
    }

    private fun initToolbar(toolbar: Toolbar) {
        toolbar.setNavigationIcon(R.mipmap.back)//设置导航栏图标
        toolbar.title = "共" + mStoryExtra!!.shortComments + "条"//设置主标题
        toolbar.setNavigationOnClickListener { v -> finish() }
    }
}
