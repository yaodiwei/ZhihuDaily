package com.yao.zhihudaily.ui.daily;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.yao.zhihudaily.R;
import com.yao.zhihudaily.model.StoryExtra;
import com.yao.zhihudaily.net.UrlConstants;
import com.yao.zhihudaily.ui.BaseActivity;

import java.util.ArrayList;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 * @author Administrator
 * @date 2016/8/30
 */
public class CommentsActivity extends BaseActivity {

    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    private StoryExtra mStoryExtra;
    private ArrayList<Fragment> mFragmentArrayList = new ArrayList<>(2);


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        ButterKnife.bind(this);

        int id = getIntent().getIntExtra("id", 0);
        mStoryExtra = (StoryExtra) getIntent().getSerializableExtra("mStoryExtra");

        initToolbar(mToolbar);


        ArrayList<String> tabList = new ArrayList<>();
        tabList.add("短评论(" + mStoryExtra.getShortComments() + ")");
        tabList.add("长评论(" + mStoryExtra.getLongComments() + ")");
        mTabLayout.addTab(mTabLayout.newTab().setText(tabList.get(0)));//添加tab选项卡
        mTabLayout.addTab(mTabLayout.newTab().setText(tabList.get(1)));

        //短评论界面
        CommentsFragment shortCommentsFragment = new CommentsFragment();
        Bundle bundleForShortComments = new Bundle();
        bundleForShortComments.putInt("id", id);
        bundleForShortComments.putString("url", UrlConstants.SHORT_COMMENTS);
        bundleForShortComments.putInt("count", mStoryExtra.getShortComments());
        shortCommentsFragment.setArguments(bundleForShortComments);

        //长评论界面
        CommentsFragment longCommentsFragment = new CommentsFragment();
        Bundle bundleForLongComments = new Bundle();
        bundleForLongComments.putInt("id", id);
        bundleForLongComments.putString("url", UrlConstants.LONG_COMMENTS);
        bundleForShortComments.putInt("counts", mStoryExtra.getLongComments());
        longCommentsFragment.setArguments(bundleForLongComments);

        mFragmentArrayList.add(shortCommentsFragment);
        mFragmentArrayList.add(longCommentsFragment);
        CommentPagerAdapter adapter = new CommentPagerAdapter(getSupportFragmentManager(), mFragmentArrayList, tabList);
        //mViewPager.setOffscreenPageLimit(0);
        mViewPager.setAdapter(adapter);
        mViewPager.addOnPageChangeListener(pageListener);

        mTabLayout.setupWithViewPager(mViewPager);//将TabLayout和ViewPager关联起来。
        mTabLayout.setTabsFromPagerAdapter(adapter);//给Tabs设置适配器
    }

    private void initToolbar(Toolbar toolbar) {
        toolbar.setNavigationIcon(R.mipmap.back);//设置导航栏图标
        toolbar.setTitle("共" + mStoryExtra.getShortComments() + "条");//设置主标题
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private ViewPager.OnPageChangeListener pageListener = new ViewPager.OnPageChangeListener() {


        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}
