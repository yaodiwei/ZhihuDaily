package com.yao.zhihudaily.ui.daily;

import android.os.Bundle;
import android.view.View;

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
 * Created by Administrator on 2016/8/30.
 */
public class CommentsActivity extends BaseActivity {

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private int id;
    private StoryExtra storyExtra;
    private ArrayList<Fragment> fragments = new ArrayList<>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        ButterKnife.bind(this);

        id = getIntent().getIntExtra("id", 0);
        storyExtra = (StoryExtra) getIntent().getSerializableExtra("storyExtra");

        initToolbar(toolbar);


        ArrayList<String> tabList = new ArrayList<>();
        tabList.add("短评论(" + storyExtra.getShortComments() + ")");
        tabList.add("长评论(" + storyExtra.getLongComments() + ")");
        tabLayout.addTab(tabLayout.newTab().setText(tabList.get(0)));//添加tab选项卡
        tabLayout.addTab(tabLayout.newTab().setText(tabList.get(1)));

        //短评论界面
        CommentsFragment shortCommentsFragment = new CommentsFragment();
        Bundle bundleForShortComments = new Bundle();
        bundleForShortComments.putInt("id", id);
        bundleForShortComments.putSerializable("storyExtra", storyExtra);
        bundleForShortComments.putString("url", UrlConstants.SHORT_COMMENTS);
        bundleForShortComments.putInt("count", storyExtra.getShortComments());
        shortCommentsFragment.setArguments(bundleForShortComments);

        //长评论界面
        CommentsFragment longCommentsFragment = new CommentsFragment();
        Bundle bundleForLongComments = new Bundle();
        bundleForLongComments.putInt("id", id);
        bundleForLongComments.putSerializable("storyExtra", storyExtra);
        bundleForLongComments.putString("url", UrlConstants.LONG_COMMENTS);
        bundleForShortComments.putInt("counts", storyExtra.getLongComments());
        longCommentsFragment.setArguments(bundleForLongComments);

        fragments.add(shortCommentsFragment);
        fragments.add(longCommentsFragment);
        CommentPagerAdapter adapter = new CommentPagerAdapter(getSupportFragmentManager(), fragments, tabList);
//      mViewPager.setOffscreenPageLimit(0);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(pageListener);

        tabLayout.setupWithViewPager(viewPager);//将TabLayout和ViewPager关联起来。
        tabLayout.setTabsFromPagerAdapter(adapter);//给Tabs设置适配器
    }

    private void initToolbar(Toolbar toolbar) {
        toolbar.setNavigationIcon(R.mipmap.back);//设置导航栏图标
        toolbar.setTitle("共" + storyExtra.getShortComments() + "条");//设置主标题
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
