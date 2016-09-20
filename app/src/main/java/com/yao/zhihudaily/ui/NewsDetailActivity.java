package com.yao.zhihudaily.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.yao.zhihudaily.R;
import com.yao.zhihudaily.model.DailyExtra;
import com.yao.zhihudaily.model.DailyJson;
import com.yao.zhihudaily.net.OkHttpSync;
import com.yao.zhihudaily.net.UrlConstants;
import com.yao.zhihudaily.ui.daily.CommentsActivity;
import com.yao.zhihudaily.util.HtmlUtil;

import java.io.IOException;

import okhttp3.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/7/28.
 */
public class NewsDetailActivity extends Activity {

    private static final String TAG = "NewsDetailActivity";
    private WebView webView;
    private TextView tvTitle, tvSource;
    private ImageView ivImage;
    private CollapsingToolbarLayout collapsingToolbarLayout;


    private DailyExtra dailyExtra;
    private Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        final int id = getIntent().getIntExtra("id", 0);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbarLayout);
        //也可以在xml中设置
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedDisappearAppBar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
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
                        break;
                    case R.id.itemComment:
                        Intent intent = new Intent(NewsDetailActivity.this, CommentsActivity.class);
                        intent.putExtra("id", id);
                        intent.putExtra("dailyExtra", dailyExtra);
                        startActivity(intent);
                        break;
                }
                return true;
            }
        });


        webView = (WebView) findViewById(R.id.webView);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvSource = (TextView) findViewById(R.id.tvSource);
        ivImage = (ImageView) findViewById(R.id.ivImage);


        //获取文章内容
        Observable.create(new Observable.OnSubscribe<DailyJson>() {

            @Override
            public void call(Subscriber<? super DailyJson> subscriber) {
                try {
                    Response response = OkHttpSync.get(String.format(UrlConstants.NEWS, id));
                    Log.e("YAO", "NewsDetailActivity.java - call() ---------- id" + id );
                    if (response.isSuccessful()) {
                        String json = response.body().string();
                        DailyJson dailyJson = new Gson().fromJson(json, DailyJson.class);
                        subscriber.onNext(dailyJson);
                    } else {
                        subscriber.onError(new Exception("error"));
                    }
                } catch (IOException e) {
                    subscriber.onError(e);
                }
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<DailyJson>() {

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: " + e.toString());
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
                });


        //获取长评论数,点赞总数,短评论数,评论总数
        Observable.create(new Observable.OnSubscribe<DailyExtra>() {

            @Override
            public void call(Subscriber<? super DailyExtra> subscriber) {
                try {
                    Response response = OkHttpSync.get(String.format(UrlConstants.STORY_EXTRA, id));
                    if (response.isSuccessful()) {
                        String json = response.body().string();
                        dailyExtra = new Gson().fromJson(json, DailyExtra.class);
                        subscriber.onNext(dailyExtra);
                    } else {
                        subscriber.onError(new Exception("error"));
                    }
                } catch (IOException e) {
                    subscriber.onError(e);
                }
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<DailyExtra>() {

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: " + e.toString());
                    }

                    @Override
                    public void onNext(DailyExtra dailyExtra) {

                    }
                });


    }
}
