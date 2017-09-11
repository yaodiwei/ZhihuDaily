package com.yao.zhihudaily.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.orhanobut.logger.Logger;
import com.yao.zhihudaily.R;
import com.yao.zhihudaily.model.DailyJson;
import com.yao.zhihudaily.model.StoryExtra;
import com.yao.zhihudaily.net.UrlConstants;
import com.yao.zhihudaily.net.ZhihuHttp;
import com.yao.zhihudaily.ui.daily.CommentsActivity;
import com.yao.zhihudaily.util.HtmlUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by Administrator on 2016/7/28.
 */
public class NewsDetailActivity extends BaseActivity {

    private static final String TAG = "NewsDetailActivity";
    @BindView(R.id.ivImage)
    ImageView ivImage;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvSource)
    TextView tvSource;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.collapsingToolbarLayout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.webView)
    WebView webView;

    private StoryExtra storyExtra;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        ButterKnife.bind(this);

        final int id = getIntent().getIntExtra("id", 0);

        //也可以在xml中设置
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedDisappearAppBar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);


        toolbar.setNavigationIcon(R.mipmap.back);//设置导航栏图标
        toolbar.inflateMenu(R.menu.new_detail_menu);//设置右上角的填充菜单
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
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
                        intent.putExtra("storyExtra", storyExtra);
                        startActivity(intent);
                        break;
                }
                return true;
            }
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
        Observer subscriber = new Observer<DailyJson>() {

            @Override
            public void onSubscribe(@NonNull Disposable d) {
                mCompositeDisposable.add(d);
            }

            @Override
            public void onNext(DailyJson dailyJson) {
                webView.loadData(HtmlUtil.createHtmlData(dailyJson), HtmlUtil.MIME_TYPE, HtmlUtil.ENCODING);
                tvTitle.setText(dailyJson.getTitle());
                tvSource.setText(dailyJson.getImageSource());
                if (dailyJson.getRecommenders() == null) {
                    collapsingToolbarLayout.setTitle("并没有推荐者");
                } else {
                    collapsingToolbarLayout.setTitle(dailyJson.getRecommenders().size() + "个推荐者");
                    toolbar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(NewsDetailActivity.this, RecommendersActivity.class);
                            intent.putExtra("id", id);
                            startActivity(intent);
                        }
                    });
                }
                Glide.with(NewsDetailActivity.this).load(dailyJson.getImage()).placeholder(R.mipmap.liukanshan).into(ivImage);
            }

            @Override
            public void onComplete() {

            }

            @Override
            public void onError(Throwable e) {
                Logger.e(e, "Subscriber onError()");
            }
        };

        ZhihuHttp.getZhihuHttp().getNews(subscriber, String.valueOf(id));
    }

    private void getStoryExtra(final int id) {
        Observer subscriber = new Observer<StoryExtra>() {

            @Override
            public void onSubscribe(@NonNull Disposable d) {
                mCompositeDisposable.add(d);
            }

            @Override
            public void onNext(StoryExtra storyExtra) {
                NewsDetailActivity.this.storyExtra = storyExtra;
            }

            @Override
            public void onComplete() {

            }

            @Override
            public void onError(Throwable e) {
                Logger.e(e, "Subscriber onError()");
            }
        };

        ZhihuHttp.getZhihuHttp().getStoryExtra(subscriber, String.valueOf(id));
    }
}
