package com.yao.zhihudaily.ui.feed;

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
import com.yao.zhihudaily.model.Story;
import com.yao.zhihudaily.model.StoryExtra;
import com.yao.zhihudaily.model.StoryJson;
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
public class StoryDetailActivity extends Activity {

    private static final String TAG = "StoryDetailActivity";
    private WebView webView;
    private TextView tvTitle, tvSource;
    private ImageView ivImage;
    private CollapsingToolbarLayout collapsingToolbarLayout;

    private StoryExtra storyExtra;


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
                        Toast.makeText(StoryDetailActivity.this, "点击分享", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.itemComment:
                        Intent intent = new Intent(StoryDetailActivity.this, CommentsActivity.class);
                        intent.putExtra("id", story.getId());
                        intent.putExtra("storyExtra", storyExtra);
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
        Observable.create(new Observable.OnSubscribe<StoryJson>() {

            @Override
            public void call(Subscriber<? super StoryJson> subscriber) {
                try {
                    Response response = OkHttpSync.get(String.format(UrlConstants.STORY, story.getId()));
                    if (response.isSuccessful()) {
                        String json = response.body().string();
                        StoryJson storyJson = new Gson().fromJson(json, StoryJson.class);
                        subscriber.onNext(storyJson);
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
                .subscribe(new Subscriber<StoryJson>() {

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: " + e.toString());
                    }

                    @Override
                    public void onNext(StoryJson storyJson) {
                        webView.loadData(HtmlUtil.createHtmlData(storyJson), HtmlUtil.MIME_TYPE, HtmlUtil.ENCODING);
                        tvTitle.setText(storyJson.getTitle());
                        tvSource.setText(storyJson.getImageSource());
                        Glide.with(StoryDetailActivity.this).load(storyJson.getImage()).into(ivImage);
                    }
                });


        //获取长评论数,点赞总数,短评论数,评论总数
        Observable.create(new Observable.OnSubscribe<StoryExtra>() {

            @Override
            public void call(Subscriber<? super StoryExtra> subscriber) {
                try {
                    Response response = OkHttpSync.get(String.format(UrlConstants.STORY_EXTRA, story.getId()));
                    if (response.isSuccessful()) {
                        String json = response.body().string();
                        storyExtra = new Gson().fromJson(json, StoryExtra.class);
                        subscriber.onNext(storyExtra);
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
                .subscribe(new Subscriber<StoryExtra>() {

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: " + e.toString());
                    }

                    @Override
                    public void onNext(StoryExtra storyExtra) {

                    }
                });
    }
}
