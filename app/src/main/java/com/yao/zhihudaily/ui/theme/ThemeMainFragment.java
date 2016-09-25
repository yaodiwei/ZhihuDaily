package com.yao.zhihudaily.ui.theme;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.yao.zhihudaily.R;
import com.yao.zhihudaily.model.ThemesJson;
import com.yao.zhihudaily.net.OkHttpSync;
import com.yao.zhihudaily.net.UrlConstants;
import com.yao.zhihudaily.net.ZhihuHttp;
import com.yao.zhihudaily.tool.GridItemDecoration;
import com.yao.zhihudaily.ui.MainFragment;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/7/22.
 */
public class ThemeMainFragment extends MainFragment {

    private static final String TAG = "ThemeMainFragment";
    @BindView(R.id.rvThemes)
    RecyclerView rvThemes;

    private GridLayoutManager gridLayoutManager;
    private ThemeAdapter themeAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_theme, container, false);
        ButterKnife.bind(this, view);


        rvThemes.setLayoutManager(gridLayoutManager = new GridLayoutManager(getActivity(), 3));
        rvThemes.addItemDecoration(new GridItemDecoration(10, 3));
        rvThemes.setAdapter(themeAdapter = new ThemeAdapter(this));

        getThemes();


        return view;
    }

    private void getThemes() {
        Subscriber subscriber = new Subscriber<ThemesJson>() {

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Logger.e(e, "Subscriber onError()");
            }

            @Override
            public void onNext(ThemesJson themesJson) {
                themeAdapter.addList(themesJson.getOthers());
                themeAdapter.notifyDataSetChanged();
            }
        };

        ZhihuHttp.getZhihuHttp().getThemes(subscriber);
    }

    @Deprecated
    private void getThemesOld() {
        Observable.create(new Observable.OnSubscribe<ThemesJson>() {

            @Override
            public void call(Subscriber<? super ThemesJson> subscriber) {
                try {
                    Response response = OkHttpSync.get(UrlConstants.THEMES);
                    if (response.isSuccessful()) {
                        ThemesJson themesJson = new Gson().fromJson(response.body().string(), ThemesJson.class);
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
                .subscribe(new Subscriber<ThemesJson>() {

                    @Override
                    public void onCompleted() {
                        themeAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e(e, "Subscriber onError()");
                    }

                    @Override
                    public void onNext(ThemesJson themesJson) {
                    }
                });
    }
}
