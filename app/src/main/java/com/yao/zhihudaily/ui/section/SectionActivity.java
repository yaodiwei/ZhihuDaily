package com.yao.zhihudaily.ui.section;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.yao.zhihudaily.R;
import com.yao.zhihudaily.model.SectionJson;
import com.yao.zhihudaily.net.OkHttpSync;
import com.yao.zhihudaily.net.UrlConstants;
import com.yao.zhihudaily.tool.DividerItemDecoration;
import com.yao.zhihudaily.tool.RecyclerViewOnLoadMoreListener;

import java.io.IOException;

import okhttp3.Response;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Created by Administrator on 2016/9/13.
 */
public class SectionActivity extends Activity {

    private static final String TAG = "SectionActivity";

    private RecyclerView rvStories;
    private SectionJson sectionJson;
    private int id;
    private String name;
    private SectionStoryAdapter sectionStoryAdapter;
    private LinearLayoutManager linearLayoutManager;
    private Toolbar toolbar;

    private RecyclerViewOnLoadMoreListener listener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_section);

        id = getIntent().getIntExtra("id", 0);
        name = getIntent().getStringExtra("name");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.mipmap.back);//设置导航栏图标
        toolbar.setTitle(name);//设置主标题
        toolbar.setNavigationOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });

        rvStories = (RecyclerView) findViewById(R.id.rvStories);
        sectionStoryAdapter = new SectionStoryAdapter(this);
        rvStories.setAdapter(sectionStoryAdapter);
        rvStories.setLayoutManager(linearLayoutManager = new LinearLayoutManager(this));
        rvStories.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        rvStories.addOnScrollListener(listener = new RecyclerViewOnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                getSectionStories(sectionJson.getTimestamp());
            }
        });

        getSectionStories(-1);
    }

    /**
     *
     * @param timestamp
     * targetDate为-1表示首次刷新或者下拉刷新, 获取最新数据
     * 为0表示没有更多数据
     * 其他值为加载更多数据
     */
    private void getSectionStories(final long timestamp) {
        Observable.create(new Observable.OnSubscribe<Boolean>() {

            private Response response;

            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                try {
                    if (timestamp == -1) {
                        response = OkHttpSync.get(String.format(UrlConstants.SECTION, String.valueOf(id)));
                    } else if (timestamp == 0) {
                    } else {
                        response = OkHttpSync.get(String.format(UrlConstants.SECTION_BEFORE, String.valueOf(id), timestamp));
                    }
                    if (response == null) {
                    } else if (response.isSuccessful()) {
                        sectionJson = new Gson().fromJson(response.body().string(), SectionJson.class);
                        sectionStoryAdapter.addList(sectionJson.getStories());
                        Log.e("YAO", "SectionActivity.java - call() ---------- sectionJson" + sectionJson.getTimestamp());
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
                        sectionStoryAdapter.notifyDataSetChanged();
                        if (timestamp > 0) {
                            listener.setLoading(false);
                        }
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
