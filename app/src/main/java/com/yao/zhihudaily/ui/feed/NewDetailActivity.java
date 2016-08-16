package com.yao.zhihudaily.ui.feed;

import android.app.Activity;
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
import com.yao.zhihudaily.model.NewJson;
import com.yao.zhihudaily.model.Story;
import com.yao.zhihudaily.net.OkHttpSync;
import com.yao.zhihudaily.net.UrlConstants;
import com.yao.zhihudaily.util.HtmlUtil;

import java.io.IOException;

import okhttp3.Response;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/7/28.
 */
public class NewDetailActivity extends Activity {

    private static final String TAG = "NewDetailActivity";
    private WebView webView;
    private TextView tvTitle, tvSource;
    private ImageView ivImage;
    private CollapsingToolbarLayout collapsingToolbarLayout;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_detail);

        final Story story = (Story) getIntent().getSerializableExtra("story");

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
                        Toast.makeText(NewDetailActivity.this, "点击分享", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.itemComment:
                        Toast.makeText(NewDetailActivity.this, "点击评论", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });


        webView = (WebView) findViewById(R.id.webView);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvSource = (TextView) findViewById(R.id.tvSource);
        ivImage = (ImageView) findViewById(R.id.ivImage);


        Subscription subscription = Observable.create(new Observable.OnSubscribe<NewJson>() {

            @Override
            public void call(Subscriber<? super NewJson> subscriber) {
                try {
                    Response response = OkHttpSync.get(UrlConstants.NEW + story.getId());
                    if (response.isSuccessful()) {
                        String json = response.body().string();
                        Log.e(TAG, "call: " + json);
                        NewJson newJson = new Gson().fromJson(json, NewJson.class);
                        subscriber.onNext(newJson);
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
                .subscribe(new Subscriber<NewJson>() {

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: " + e.toString());
                    }

                    @Override
                    public void onNext(NewJson newJson) {
                        webView.loadData(HtmlUtil.createHtmlData(newJson), HtmlUtil.MIME_TYPE, HtmlUtil.ENCODING);
                        tvTitle.setText(newJson.getTitle());
                        tvSource.setText(newJson.getImageSource());
                        Glide.with(NewDetailActivity.this).load(newJson.getImage()).into(ivImage);
                    }
                });


    }
}
