package com.yao.zhihudaily.ui.theme;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.yao.zhihudaily.R;
import com.yao.zhihudaily.model.ThemesJson;
import com.yao.zhihudaily.net.OkHttpSync;
import com.yao.zhihudaily.net.UrlConstants;
import com.yao.zhihudaily.tool.DividerGridItemDecoration;
import com.yao.zhihudaily.ui.MainFragment;

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
public class ThemeMainFragment extends MainFragment{

    private static final String TAG = "ThemeMainFragment";
    
    private RecyclerView rvThemes;
    private GridLayoutManager gridLayoutManager;
    private ThemeAdapter themeAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_theme, container, false);

        rvThemes = (RecyclerView) view.findViewById(R.id.rvThemes);

        rvThemes.setLayoutManager(gridLayoutManager = new GridLayoutManager(getActivity(), 3));
        rvThemes.addItemDecoration(new DividerGridItemDecoration(getActivity()));
        rvThemes.setAdapter(themeAdapter = new ThemeAdapter(this));

        Subscription subscription = Observable.create(new Observable.OnSubscribe<Boolean>() {

            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                try {
                    Response response = OkHttpSync.get(UrlConstants.THEMES);
                    if (response.isSuccessful()) {
                        ThemesJson themesJson = new Gson().fromJson(response.body().string(), ThemesJson.class);
                        Log.e("YAO", "ThemeMainFragment.java - call() ---------- " + themesJson);
                        themeAdapter.addList(themesJson.getOthers());
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
                        themeAdapter.notifyDataSetChanged();
                        Log.e("YAO", "ThemeMainFragment.java - onCompleted() ---------- ");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: " + e.toString());
                    }

                    @Override
                    public void onNext(Boolean isRefreshing) {
//                        swipeRefreshLayout.setRefreshing(isRefreshing);
                    }
                });


        return view;
    }
}
