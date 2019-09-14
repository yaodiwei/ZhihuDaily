package com.yao.zhihudaily.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationAdapter;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.google.android.material.navigation.NavigationView;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.PermissionListener;
import com.yao.zhihudaily.R;
import com.yao.zhihudaily.ui.daily.DailyMainFragment;
import com.yao.zhihudaily.ui.hot.HotMainFragment;
import com.yao.zhihudaily.ui.section.SectionMainFragment;
import com.yao.zhihudaily.ui.theme.ThemeMainFragment;
import com.yao.zhihudaily.util.ResUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Yao
 * @date 2016/7/21
 */
public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static final int REQUEST_PERMISSION_STORAGE = 200;

    private static final String TAG = "MainActivity";

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

    private BaseFragment currentFragment;
    private MainViewPagerAdapter adapter;
    private ArrayList<AHBottomNavigationItem> bottomNavigationItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initView();

        AndPermission.with(this)
                .requestCode(REQUEST_PERMISSION_STORAGE)
                .permission(Permission.STORAGE)
                .rationale((requestCode, rationale) ->
                        new AlertDialog.Builder(MainActivity.this)
                                .setTitle(R.string.tip)
                                .setMessage(R.string.permission_storage_rationale)
                                .setCancelable(false)
                                .setPositiveButton(R.string.open_permission_dialog, (dialog, which) -> rationale.resume())
                                .setNegativeButton(R.string.cancel, null)
                                .show())
                .callback(new PermissionListener() {
                    @Override
                    public void onSucceed(int requestCode, @NonNull List<String> grantedPermissions) {
                        if (requestCode == REQUEST_PERMISSION_STORAGE) {
                            listStorageDir();
                        }
                    }

                    @Override
                    public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {
                        if (requestCode == REQUEST_PERMISSION_STORAGE) {
                            new AlertDialog.Builder(MainActivity.this)
                                    .setTitle(R.string.tip)
                                    .setMessage(R.string.permission_storage_failed)
                                    .setCancelable(false)
                                    .setPositiveButton(R.string.go_to_setting, (dialog, which) -> goToSetting())
                                    .setNegativeButton(R.string.cancel, null)
                                    .show();
                        }
                    }
                });//.start();
    }

    private void goToSetting() {
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
                builder.append(str).append(", ");
            }
            builder.substring(0, builder.length() - 2);
            Log.e("YAO", "MainActivity.java - onCreate() ----- builder\n " + builder.toString());
        }
    }

    private void initView() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.setDrawerIndicatorEnabled(false);//隐藏左上角的DrawerLayout图标
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);//暂时关闭侧边栏,因为没有什么业务好写
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);


        toolbar.inflateMenu(R.menu.main);//设置右上角的填充菜单
        toolbar.setOnMenuItemClickListener(item -> {
            int menuItemId = item.getItemId();
            if (menuItemId == R.id.action_settings) {
                startActivity(new Intent(MainActivity.this, SettingActivity.class));
            } else if (menuItemId == R.id.action_introduce) {
                startActivity(new Intent(MainActivity.this, SoftwareIntroductionActivity.class));
            }
            return true;
        });

        //是否用Menu资源去完成,menu资源即对应的menu布局文件. 否则就是用代码new出来并且添加上去的AHBottomNavigationItem
        boolean useMenuResource = true;
        if (useMenuResource) {//方式一:通过menu菜单去完成
            int[] tabColors = getApplicationContext().getResources().getIntArray(R.array.tab_colors);
            AHBottomNavigationAdapter navigationAdapter = new AHBottomNavigationAdapter(this, R.menu.bottom_navigation_menu_3);
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
        bottomNavigation.setAccentColor(ResUtil.getColor(R.color.zhihu_blue)); //设置选中的颜色
        bottomNavigation.setInactiveColor(ResUtil.getColor(R.color.bottomnavigation_inactive));//设置闲置的颜色
        bottomNavigation.setDefaultBackgroundColor(ResUtil.getColor(R.color.bottomnavigation_bg));//设置背景颜色

        //bottomNavigation.setNotification("", position);//给Item设置通知图标
        viewPager.setOffscreenPageLimit(3);

        DailyMainFragment feedMainFragment = new DailyMainFragment();
        ThemeMainFragment themeMainFragment = new ThemeMainFragment();
        HotMainFragment hotMainFragment = new HotMainFragment();
        SectionMainFragment sectionMainFragment = new SectionMainFragment();
        ArrayList<BaseFragment> baseFragments = new ArrayList<>();
        baseFragments.add(feedMainFragment);
        baseFragments.add(themeMainFragment);
        baseFragments.add(hotMainFragment);
        baseFragments.add(sectionMainFragment);
        adapter = new MainViewPagerAdapter(getSupportFragmentManager(), baseFragments);
        viewPager.setAdapter(adapter);
        currentFragment = adapter.getCurrentFragment();

        bottomNavigation.setOnTabSelectedListener((position, wasSelected) -> {//wasSelected为真时,表示当前显示与当前点击的是同一个Item


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
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
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
