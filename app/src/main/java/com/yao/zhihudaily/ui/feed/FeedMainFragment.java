package com.yao.zhihudaily.ui.feed;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.yao.zhihudaily.R;
import com.yao.zhihudaily.model.StoriesJson;
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
 * Created by Administrator on 2016/7/22.
 */
public class FeedMainFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "FeedMainFragment";

    private SwipeRefreshLayout swipeRefreshLayout;

    private RecyclerView rvStories;

    private StoryAdapter storyAdapter;

    private String endDate;//当前App里有的最新日报的时间

    private String startDate;//当前App下拉加载到的最早时间

    private LinearLayoutManager linearLayoutManager;

    private boolean isLoadMore;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed, container, false);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        rvStories = (RecyclerView) view.findViewById(R.id.rvStories);
        swipeRefreshLayout.setOnRefreshListener(this);

        rvStories.setLayoutManager(linearLayoutManager = new LinearLayoutManager(getActivity()));
        rvStories.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        rvStories.setAdapter(storyAdapter = new StoryAdapter(this));
        rvStories.addOnScrollListener(new RecyclerView.OnScrollListener() {

            private int lastVisibleItemPosition;
            private int visibleItemCount;
            private int totalItemCount;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                visibleItemCount = layoutManager.getChildCount();
                lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                totalItemCount = layoutManager.getItemCount();
                if (visibleItemCount > 0 && newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastVisibleItemPosition >= totalItemCount - 1) {
                    //加载更多
                    if (!isLoadMore) {
                        getNews(startDate);
                        isLoadMore = true;
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        getNews(null);

        return view;
    }

    /**
     *
     * @param tartgetDate
     * targetDate为空表示首次刷新或者下拉刷新, 获取最新数据
     * 不为空表示加载更多, 获取指定日期历史数据
     */
    private void getNews(final String tartgetDate) {
        Subscription subscription = Observable.create(new Observable.OnSubscribe<Boolean>() {

            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                try {
                    Response response = null;
                    if (tartgetDate == null) {
                        response = OkHttpSync.get(UrlConstants.STORIES);
                    } else {
                        response = OkHttpSync.get(String.format(UrlConstants.STORIES_BEFORE, tartgetDate));
                    }
                    if (response.isSuccessful()) {
                        StoriesJson storiesJson = new Gson().fromJson(response.body().string(), StoriesJson.class);
                        if (TextUtils.isEmpty(endDate)) { //如果是首次加载这个界面
                            startDate = storiesJson.getDate();
                            endDate = storiesJson.getDate();
                            storyAdapter.addList(storiesJson.getStories());
                            subscriber.onCompleted();
                        } else if (tartgetDate == null) { //表示下拉刷新
                            if (endDate.equals(storiesJson.getDate())) { //App的最晚时间 等于 下拉新获取的时间
                                subscriber.onNext(false);
                            } else { ////App的最晚时间 不等于 下拉新获取的时间
                                endDate = storiesJson.getDate();
                                storyAdapter.addListToHeader(storiesJson.getStories());
                                subscriber.onCompleted();
                            }
                        } else { //表示上拉加载
                            startDate = storiesJson.getDate();
                            storyAdapter.addList(storiesJson.getStories());
                            subscriber.onCompleted();
                        }
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
                        if (tartgetDate != null) {
                            isLoadMore = false;
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: " + e.toString());
                    }

                    @Override
                    public void onNext(Boolean isRefreshing) {
                        swipeRefreshLayout.setRefreshing(isRefreshing);
                    }
                });
    }


    @Override
    public void onRefresh() {
        getNews(null);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}