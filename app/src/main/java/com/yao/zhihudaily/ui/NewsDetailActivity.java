package com.yao.zhihudaily.ui;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.orhanobut.logger.Logger;
import com.yao.zhihudaily.R;
import com.yao.zhihudaily.model.DailyJson;
import com.yao.zhihudaily.model.StoryExtra;
import com.yao.zhihudaily.net.UrlConstants;
import com.yao.zhihudaily.net.ZhihuHttp;
import com.yao.zhihudaily.ui.daily.CommentsActivity;
import com.yao.zhihudaily.util.HtmlUtil;

import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * @author Administrator
 * @date 2016/7/28
 */
public class NewsDetailActivity extends BaseActivity {

    private static final String TAG = "NewsDetailActivity";
    @BindView(R.id.ivImage)
    ImageView mIvImage;
    @BindView(R.id.tvTitle)
    TextView mTvTitle;
    @BindView(R.id.tvSource)
    TextView mTvSource;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.collapsingToolbarLayout)
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    @BindView(R.id.webView)
    WebView mWebView;

    private StoryExtra mStoryExtra;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        ButterKnife.bind(this);

        final int id = getIntent().getIntExtra("id", 0);

        //也可以在xml中设置
        mCollapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedDisappearAppBar);
        mCollapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);


        mToolbar.setNavigationIcon(R.mipmap.back);//设置导航栏图标
        mToolbar.inflateMenu(R.menu.new_detail_menu);//设置右上角的填充菜单
        mToolbar.setNavigationOnClickListener(view -> finish());
        mToolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.itemShare:
                    Toast.makeText(NewsDetailActivity.this, "点击分享", Toast.LENGTH_SHORT).show();
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, "发现一篇好文章分享给你，地址:" + String.format(UrlConstants.STORY_SHARE, id));
                    sendIntent.setType("text/plain");
                    startActivity(sendIntent);
                    break;
                case R.id.itemComment:
                    Intent intent = new Intent(NewsDetailActivity.this, CommentsActivity.class);
                    intent.putExtra("id", id);
                    intent.putExtra("story_extra", mStoryExtra);
                    startActivity(intent);
                    break;
                default:
                    break;
            }
            return true;
        });

        getNews(id);
        getStoryExtra(id);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCompositeDisposable.clear();
    }

    private void getNews(final int id) {
        ZhihuHttp.getZhihuHttp().getNews(String.valueOf(id)).subscribe(new Observer<DailyJson>() {

            @Override
            public void onSubscribe(@NonNull Disposable d) {
                mCompositeDisposable.add(d);
            }

            @Override
            public void onNext(DailyJson dailyJson) {
                mWebView.loadData(HtmlUtil.createHtmlData(dailyJson), HtmlUtil.MIME_TYPE, HtmlUtil.ENCODING);
                mTvTitle.setText(dailyJson.getTitle());
                mTvSource.setText(dailyJson.getImageSource());
                if (dailyJson.getRecommenders() == null) {
                    mCollapsingToolbarLayout.setTitle("并没有推荐者");
                } else {
                    mCollapsingToolbarLayout.setTitle(dailyJson.getRecommenders().size() + "个推荐者");
                    mToolbar.setOnClickListener(view -> {
                        Intent intent = new Intent(NewsDetailActivity.this, RecommendersActivity.class);
                        intent.putExtra("id", id);
                        startActivity(intent);
                    });
                }
                Glide.with(NewsDetailActivity.this).load(dailyJson.getImage()).placeholder(R.mipmap.liukanshan).into(mIvImage);
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

    private void getStoryExtra(final int id) {
        ZhihuHttp.getZhihuHttp().getStoryExtra(String.valueOf(id)).subscribe(new Observer<StoryExtra>() {

            @Override
            public void onSubscribe(@NonNull Disposable d) {
                mCompositeDisposable.add(d);
            }

            @Override
            public void onNext(StoryExtra storyExtra) {
                NewsDetailActivity.this.mStoryExtra = storyExtra;
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
