package com.yao.zhihudaily.ui

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation.OnTabSelectedListener
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationAdapter
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem
import com.google.android.material.navigation.NavigationView
import com.yao.zhihudaily.R
import com.yao.zhihudaily.ui.daily.DailyMainFragment
import com.yao.zhihudaily.ui.hot.HotMainFragment
import com.yao.zhihudaily.ui.section.SectionMainFragment
import com.yao.zhihudaily.util.ResUtil
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

/**
 * @author Yao
 * @date 2016/7/21
 */
class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    private var currentFragment: BaseFragment? = null
    private var adapter: MainViewPagerAdapter? = null
    private val bottomNavigationItems = ArrayList<AHBottomNavigationItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
    }

    private fun initView() {
        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        toggle.isDrawerIndicatorEnabled = false//隐藏左上角的DrawerLayout图标
        drawer_layout!!.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)//暂时关闭侧边栏,因为没有什么业务好写
        drawer_layout!!.addDrawerListener(toggle)
        toggle.syncState()

        navigation_view!!.setNavigationItemSelectedListener(this)


        toolbar!!.inflateMenu(R.menu.main)//设置右上角的填充菜单
        toolbar!!.setOnMenuItemClickListener { item ->
            val menuItemId = item.itemId
            if (menuItemId == R.id.action_settings) {
                startActivity(Intent(this@MainActivity, SettingActivity::class.java))
            } else if (menuItemId == R.id.action_introduce) {
                startActivity(Intent(this@MainActivity, SoftwareIntroductionActivity::class.java))
            }
            true
        }

        //是否用Menu资源去完成,menu资源即对应的menu布局文件. 否则就是用代码new出来并且添加上去的AHBottomNavigationItem
        val useMenuResource = true
        if (useMenuResource) {//方式一:通过menu菜单去完成
            val tabColors = applicationContext.resources.getIntArray(R.array.tab_colors)
            val navigationAdapter = AHBottomNavigationAdapter(this, R.menu.bottom_navigation_menu_3)
            navigationAdapter.setupWithBottomNavigation(bottom_navigation, tabColors)
        } else {//方式二:通过代码new出去并且添加上去完成
            val item1 = AHBottomNavigationItem(R.string.daily, R.mipmap.ic_bottom_navigation_daily, R.color.color_tab_1)
            val item2 = AHBottomNavigationItem(R.string.theme, R.mipmap.ic_bottom_navigation_theme, R.color.color_tab_2)
            val item3 = AHBottomNavigationItem(R.string.hot, R.mipmap.ic_bottom_navigation_hot, R.color.color_tab_3)
            val item4 = AHBottomNavigationItem(R.string.more, R.mipmap.ic_bottom_navigation_more, R.color.color_tab_4)

            bottomNavigationItems.add(item1)
            bottomNavigationItems.add(item2)
            bottomNavigationItems.add(item3)
            bottomNavigationItems.add(item4)

            bottom_navigation!!.addItems(bottomNavigationItems)
        }

        bottom_navigation!!.isBehaviorTranslationEnabled = true//重要属性 设置向上滑动时是否隐藏底部栏
        bottom_navigation!!.accentColor = ResUtil.getColor(R.color.zhihu_blue) //设置选中的颜色
        bottom_navigation!!.inactiveColor = ResUtil.getColor(R.color.bottom_navigation_inactive)//设置闲置的颜色
        bottom_navigation!!.defaultBackgroundColor = ResUtil.getColor(R.color.bottom_navigation_bg)//设置背景颜色

        //bottom_navigation.setNotification("", position);//给Item设置通知图标
        view_pager!!.offscreenPageLimit = 3

        val feedMainFragment = DailyMainFragment()
        //ThemeMainFragment themeMainFragment = new ThemeMainFragment();
        val hotMainFragment = HotMainFragment()
        val sectionMainFragment = SectionMainFragment()
        val baseFragments = ArrayList<BaseFragment>()
        baseFragments.add(feedMainFragment)
        //baseFragments.add(themeMainFragment);
        baseFragments.add(hotMainFragment)
        baseFragments.add(sectionMainFragment)
        adapter = MainViewPagerAdapter(supportFragmentManager, baseFragments)
        view_pager!!.adapter = adapter
        currentFragment = adapter!!.currentFragment

        //wasSelected为真时,表示当前显示与当前点击的是同一个Item
        bottom_navigation!!.setOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(position: Int, wasSelected: Boolean): Boolean {
                if (currentFragment == null) {
                    currentFragment = adapter!!.currentFragment
                }

                if (wasSelected) {//为真时,刷新一个当前item就行
                    currentFragment!!.refresh()
                    return true
                }

                if (currentFragment != null) {
                    currentFragment!!.willBeHidden()
                }

                view_pager!!.setCurrentItem(position, false)
                currentFragment = adapter!!.currentFragment
                currentFragment!!.willBeDisplayed()
                return true
            }
        })
    }

    override fun onBackPressed() {
        if (drawer_layout!!.isDrawerOpen(GravityCompat.START)) {
            drawer_layout!!.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        val id = item.itemId

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        drawer_layout!!.closeDrawer(GravityCompat.START)
        return true
    }

    companion object {

        val REQUEST_PERMISSION_STORAGE = 200

        private val TAG = "MainActivity"
    }

}
