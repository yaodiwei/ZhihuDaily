package com.yao.zhihudaily.ui.daily;

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
import com.yao.zhihudaily.model.Daily;
import com.yao.zhihudaily.model.DailyJson;
import com.yao.zhihudaily.net.OkHttpSync;
import com.yao.zhihudaily.net.UrlConstants;
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
public class DailyDetailActivity extends Activity {

    private static final String TAG = "DailyDetailActivity";
    private WebView webView;
    private TextView tvTitle, tvSource;
    private ImageView ivImage;
    private CollapsingToolbarLayout collapsingToolbarLayout;

    private DailyExtra dailyExtra;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_detail);

        final Daily daily = (Daily) getIntent().getSerializableExtra("daily");

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbarLayout);
        collapsingToolbarLayout.setTitle("五个推荐者");
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
//        collapsingToolbarLayout.setCollapsedTitleTextColor(0xFF000000);
//        collapsingToolbarLayout.setExpandedTitleColor(0x00FFFFFF);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.mipmap.back);//设置导航栏图标
//        toolbar.setLogo(R.mipmap.ic_launcher);
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
                        Toast.makeText(DailyDetailActivity.this, "点击分享", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.itemComment:
                        Intent intent = new Intent(DailyDetailActivity.this, CommentsActivity.class);
                        intent.putExtra("id", daily.getId());
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
                    Response response = OkHttpSync.get(String.format(UrlConstants.DAILY, daily.getId()));
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
                        Glide.with(DailyDetailActivity.this).load(dailyJson.getImage()).into(ivImage);
                    }
                });


        //获取长评论数,点赞总数,短评论数,评论总数
        Observable.create(new Observable.OnSubscribe<DailyExtra>() {

            @Override
            public void call(Subscriber<? super DailyExtra> subscriber) {
                try {
                    Response response = OkHttpSync.get(String.format(UrlConstants.DAILY_EXTRA, daily.getId()));
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
