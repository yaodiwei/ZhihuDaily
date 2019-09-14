package com.yao.zhihudaily.ui.theme;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.orhanobut.logger.Logger;
import com.yao.zhihudaily.R;
import com.yao.zhihudaily.model.ThemeJson;
import com.yao.zhihudaily.net.ZhihuHttp;
import com.yao.zhihudaily.tool.DividerItemDecoration;
import com.yao.zhihudaily.ui.BaseActivity;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;


/**
 *
 * @author Yao
 * @date 2016/9/10
 */
public class ThemeActivity extends BaseActivity {

    private static final String TAG = "ThemeActivity";
    @BindView(R.id.iv_background)
    ImageView mIvBackground;
    @BindView(R.id.tv_description)
    TextView mTvDescription;
    @BindView(R.id.collapsing_toolbar_layout)
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    @BindView(R.id.rv_stories)
    RecyclerView mRvStories;

    private ThemeJson mThemeJson;
    private int mId;
    private ThemeStoryAdapter mThemeStoryAdapter;
    private Disposable mDisposable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme);
        ButterKnife.bind(this);

        mId = getIntent().getIntExtra("mId", 0);
        //已经在xml中设置
        //mCollapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        //mCollapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.mipmap.back);//设置导航栏图标
        //toolbar.setLogo(R.mipmap.ic_launcher);
        toolbar.setNavigationOnClickListener(view -> finish());


        mThemeStoryAdapter = new ThemeStoryAdapter(this);
        mRvStories.setAdapter(mThemeStoryAdapter);
        LinearLayoutManager linearLayoutManager;
        mRvStories.setLayoutManager(new LinearLayoutManager(this));
        mRvStories.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

        getThemeData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDisposable != null) {
            mDisposable.dispose();
        }
    }

    private void getThemeData() {
        ZhihuHttp.getZhihuHttp().getTheme(String.valueOf(mId)).subscribe(new Observer<ThemeJson>() {

            @Override
            public void onSubscribe(@NonNull Disposable d) {
                mDisposable = d;
            }

            @Override
            public void onNext(ThemeJson themeJson) {
                mThemeStoryAdapter.addList(themeJson.getStories());
                mThemeStoryAdapter.notifyDataSetChanged();
                Glide.with(ThemeActivity.this).load(themeJson.getBackground()).into(mIvBackground);
                mCollapsingToolbarLayout.setTitle(themeJson.getName());
                mTvDescription.setText("        " + themeJson.getDescription());
            }

            @Override
            public void onComplete() {

            }

            @Override
            public void onError(Throwable e) {
                Logger.e(e, "Subscriber onError()");
            }
        });
    }
}
