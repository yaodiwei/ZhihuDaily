package com.yao.zhihudaily.ui.theme;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.yao.zhihudaily.R;
import com.yao.zhihudaily.model.ThemeJson;
import com.yao.zhihudaily.net.OkHttpSync;
import com.yao.zhihudaily.net.UrlConstants;
import com.yao.zhihudaily.tool.DividerItemDecoration;

import java.io.IOException;

import okhttp3.Response;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Created by Administrator on 2016/9/10.
 */
public class ThemeActivity extends Activity {

    private static final String TAG = "ThemeActivity";

    private ImageView ivBackground;
    private RecyclerView rvStories;
    private ThemeJson themeJson;
    private int id;
    private TextView tvDescription;
    private StoryAdapter storyAdapter;
    private LinearLayoutManager linearLayoutManager;
    private CollapsingToolbarLayout collapsingToolbarLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme);

        id = getIntent().getIntExtra("id", 0);
        rvStories = (RecyclerView) findViewById(R.id.rvStories);
        ivBackground = (ImageView) findViewById(R.id.ivBackground);
        tvDescription = (TextView) findViewById(R.id.tvDescription);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbarLayout);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbarLayout);
        //已经在xml中设置
//        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
//        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.mipmap.back);//设置导航栏图标
//        toolbar.setLogo(R.mipmap.ic_launcher);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        storyAdapter = new StoryAdapter(this);
        rvStories.setAdapter(storyAdapter);
        rvStories.setLayoutManager(linearLayoutManager = new LinearLayoutManager(this));
        rvStories.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));


        Subscription subscription = Observable.create(new Observable.OnSubscribe<Boolean>() {

            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                try {
                    Response response = OkHttpSync.get(String.format(UrlConstants.THEME, String.valueOf(id)));
                    if (response.isSuccessful()) {
                        themeJson = new Gson().fromJson(response.body().string(), ThemeJson.class);
                        Log.e("YAO", "ThemeActivity.java - call() ---------- "+ themeJson.getStories().size() + themeJson);
                        storyAdapter.addList(themeJson.getStories());
                        subscriber.onCompleted();
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
                .subscribe(new Subscriber<Boolean>() {

                    @Override
                    public void onCompleted() {
                        storyAdapter.notifyDataSetChanged();
                        Glide.with(ThemeActivity.this).load(themeJson.getBackground()).into(ivBackground);
                        collapsingToolbarLayout.setTitle(themeJson.getName());
                        tvDescription.setText("        " + themeJson.getDescription());
                        Log.e("YAO", "ThemeMainFragment.java - onCompleted() ---------- ");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: " + e.toString());
                    }

                    @Override
                    public void onNext(Boolean isRefreshing) {
                    }
                });


    }
}
