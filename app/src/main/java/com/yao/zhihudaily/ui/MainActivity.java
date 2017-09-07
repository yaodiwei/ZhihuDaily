package com.yao.zhihudaily.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationAdapter;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.PermissionListener;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;
import com.yao.zhihudaily.R;
import com.yao.zhihudaily.ui.daily.DailyMainFragment;
import com.yao.zhihudaily.ui.hot.HotMainFragment;
import com.yao.zhihudaily.ui.section.SectionMainFragment;
import com.yao.zhihudaily.ui.theme.ThemeMainFragment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final int REQUEST_PERMISSION_STORAGE = 200;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.viewPager)
    ViewPager viewPager;//适配BottomNavigation的ViewPager
    @BindView(R.id.bottomNavigation)
    AHBottomNavigation bottomNavigation;//底部的BottomNavigation
    @BindView(R.id.navigationView)
    NavigationView navigationView;
    @BindView(R.id.drawerLayout)
    DrawerLayout drawerLayout;

    private MainFragment currentFragment;
    private MainViewPagerAdapter adapter;
    private AHBottomNavigationAdapter navigationAdapter;
    private ArrayList<AHBottomNavigationItem> bottomNavigationItems = new ArrayList<>();
    //是否用Menu资源去完成,menu资源即对应的menu布局文件. 否则就是用代码new出来并且添加上去的AHBottomNavigationItem
    private boolean useMenuResource = true;
    private int[] tabColors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initView();

        AndPermission.with(this)
                .requestCode(REQUEST_PERMISSION_STORAGE)
                .permission(Permission.STORAGE)
                .rationale(new RationaleListener() {
                    @Override
                    public void showRequestPermissionRationale(int requestCode, final Rationale rationale) {
                        new android.support.v7.app.AlertDialog.Builder(MainActivity.this)
                                .setTitle(R.string.tip)
                                .setMessage(R.string.permission_storage_rationale)
                                .setCancelable(false)
                                .setPositiveButton(R.string.open_permission_dialog, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        rationale.resume();
                                    }
                                })
                                .setNegativeButton(R.string.cancel, null)
                                .show();
                    }
                })
                .callback(new PermissionListener() {
                    @Override
                    public void onSucceed(int requestCode, List<String> grantedPermissions) {
                        if(requestCode == REQUEST_PERMISSION_STORAGE) {
                            listStorageDir();
                        }
                    }

                    @Override
                    public void onFailed(int requestCode, List<String> deniedPermissions) {
                        if(requestCode == REQUEST_PERMISSION_STORAGE) {
                            new android.support.v7.app.AlertDialog.Builder(MainActivity.this)
                                    .setTitle(R.string.tip)
                                    .setMessage(R.string.permission_storage_failed)
                                    .setCancelable(false)
                                    .setPositiveButton(R.string.go_to_setting, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            goToSetting();
                                        }
                                    })
                                    .setNegativeButton(R.string.cancel, null)
                                    .show();
                        }
                    }
                });//.start();
    }

    private void goToSetting(){
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.setData(Uri.fromParts("package", getPackageName(), null));
        startActivity(intent);
    }

    private void listStorageDir() {
        File file = Environment.getExternalStorageDirectory();
        final String[] strings = file.list();
        if (strings != null && strings.length > 0) {
            StringBuilder builder = new StringBuilder();
            for (String str : strings) {
                builder.append(str + ", ");
            }
            builder.substring(0, builder.length()-2);
            Log.e("YAO", "MainActivity.java - onCreate() ----- builder\n " + builder.toString());
        }
    }

    private void initView() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.setDrawerIndicatorEnabled(false);//隐藏左上角的DrawerLayout图标
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);//暂时关闭侧边栏,因为没有什么业务好写
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);


        toolbar.inflateMenu(R.menu.main);//设置右上角的填充菜单
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int menuItemId = item.getItemId();
                if (menuItemId == R.id.action_settings) {
                    startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                } else if (menuItemId == R.id.action_introduce) {
                    startActivity(new Intent(MainActivity.this, SoftwareIntroductionActivity.class));
                }
                return true;
            }
        });

        if (useMenuResource) {//方式一:通过menu菜单去完成
            tabColors = getApplicationContext().getResources().getIntArray(R.array.tab_colors);
            navigationAdapter = new AHBottomNavigationAdapter(this, R.menu.bottom_navigation_menu_3);
            navigationAdapter.setupWithBottomNavigation(bottomNavigation, tabColors);
        } else {//方式二:通过代码new出去并且添加上去完成
            AHBottomNavigationItem item1 = new AHBottomNavigationItem(R.string.daily, R.mipmap.ic_bottomnavigation_daily, R.color.color_tab_1);
            AHBottomNavigationItem item2 = new AHBottomNavigationItem(R.string.theme, R.mipmap.ic_bottomnavigation_theme, R.color.color_tab_2);
            AHBottomNavigationItem item3 = new AHBottomNavigationItem(R.string.hot, R.mipmap.ic_bottomnavigation_hot, R.color.color_tab_3);
            AHBottomNavigationItem item4 = new AHBottomNavigationItem(R.string.more, R.mipmap.ic_bottomnavigation_more, R.color.color_tab_4);

            bottomNavigationItems.add(item1);
            bottomNavigationItems.add(item2);
            bottomNavigationItems.add(item3);
            bottomNavigationItems.add(item4);

            bottomNavigation.addItems(bottomNavigationItems);
        }

        bottomNavigation.setBehaviorTranslationEnabled(true);//重要属性 设置向上滑动时是否隐藏底部栏
        bottomNavigation.setAccentColor(getResources().getColor(R.color.zhihu_blue)); //设置选中的颜色
        bottomNavigation.setInactiveColor(getResources().getColor(R.color.bottomnavigation_inactive));//设置闲置的颜色
        bottomNavigation.setDefaultBackgroundColor(getResources().getColor(R.color.bottomnavigation_bg));//设置背景颜色

//        bottomNavigation.setNotification("", position);//给Item设置通知图标
        viewPager.setOffscreenPageLimit(3);

        DailyMainFragment feedMainFragment = new DailyMainFragment();
        ThemeMainFragment themeMainFragment = new ThemeMainFragment();
        HotMainFragment hotMainFragment = new HotMainFragment();
        SectionMainFragment sectionMainFragment = new SectionMainFragment();
        ArrayList<MainFragment> mainFragments = new ArrayList<MainFragment>();
        mainFragments.add(feedMainFragment);
        mainFragments.add(themeMainFragment);
        mainFragments.add(hotMainFragment);
        mainFragments.add(sectionMainFragment);
        adapter = new MainViewPagerAdapter(getFragmentManager(), mainFragments);
        viewPager.setAdapter(adapter);
        currentFragment = adapter.getCurrentFragment();

        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {//wasSelected为真时,表示当前显示与当前点击的是同一个Item


                if (currentFragment == null) {
                    currentFragment = adapter.getCurrentFragment();
                }

                if (wasSelected) {//为真时,刷新一个当前item就行
                    currentFragment.refresh();
                    return true;
                }

                if (currentFragment != null) {
                    currentFragment.willBeHidden();
                }

                viewPager.setCurrentItem(position, false);
                currentFragment = adapter.getCurrentFragment();
                currentFragment.willBeDisplayed();
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
