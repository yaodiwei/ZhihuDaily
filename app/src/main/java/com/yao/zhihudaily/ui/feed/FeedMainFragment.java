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
import com.yao.zhihudaily.model.NewsJson;
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

    private String currentDate;

    private LinearLayoutManager linearLayoutManager;

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
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);


            }
        });

        getLatestNews();

        return view;
    }

    private void getLatestNews() {
        Subscription subscription = Observable.create(new Observable.OnSubscribe<Boolean>() {

            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                try {
                    Response response = OkHttpSync.get(UrlConstants.NEWS);
                    if (response.isSuccessful()) {
                        NewsJson newsJson = new Gson().fromJson(response.body().string(), NewsJson.class);

                        if (TextUtils.isEmpty(currentDate) || !currentDate.equals(newsJson.getDate())) {
                            //下拉刷新, 如果有更加新的数据
                            currentDate = newsJson.getDate();
                            storyAdapter.addList(newsJson.getStories());
                            subscriber.onCompleted();
                        } else {
                            //下拉刷新, 如果没有更加新的数据
                            subscriber.onNext(false);
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
        getLatestNews();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}