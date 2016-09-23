package com.yao.zhihudaily.net;

import com.yao.zhihudaily.model.CommentJson;
import com.yao.zhihudaily.model.DailiesJson;
import com.yao.zhihudaily.model.DailyJson;
import com.yao.zhihudaily.model.HotJson;
import com.yao.zhihudaily.model.RecommendsJson;
import com.yao.zhihudaily.model.SectionJson;
import com.yao.zhihudaily.model.SectionsJson;
import com.yao.zhihudaily.model.StoryExtra;
import com.yao.zhihudaily.model.ThemeJson;
import com.yao.zhihudaily.model.ThemesJson;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/9/21.
 */

public class ZhihuHttp {

    public static final String ZHIHU_BASE_URL = "http://news-at.zhihu.com/api/";

    private static final ZhihuHttp zhihuHttp = new ZhihuHttp();

    private OkHttpClient okHttpClient;

    private Retrofit retrofit;

    private ZhihuApi zhihuApi;

    private ZhihuHttp() {
        if (zhihuHttp == null) {
            synchronized (ZhihuHttp.this) {
                if (zhihuHttp == null) {
                    okHttpClient = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).build();

                    retrofit = new Retrofit.Builder()
                            .client(okHttpClient)
                            .addConverterFactory(GsonConverterFactory.create())
                            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                            .baseUrl(ZHIHU_BASE_URL)
                            .build();

                    zhihuApi = retrofit.create(ZhihuApi.class);
                }
            }
        }
    }

    public static ZhihuHttp getZhihuHttp() {
        return zhihuHttp;
    }

    public void getDailies(Subscriber<DailiesJson> subscriber) {
        Observable observable = zhihuApi.getDailies();
        toSubscribe(observable, subscriber);
    }

    public void getDailiesBefore(Subscriber<DailiesJson> subscriber, String date) {
        Observable observable = zhihuApi.getDailiesBefore(date);
        toSubscribe(observable, subscriber);
    }

    public void getNews(Subscriber<DailyJson> subscriber, String id) {
        Observable observable = zhihuApi.getNews(id);
        toSubscribe(observable, subscriber);
    }

    public void getStoryExtra(Subscriber<StoryExtra> subscriber, String id) {
        Observable observable = zhihuApi.getStoryExtra(id);
        toSubscribe(observable, subscriber);
    }

    public void getShortComments(Subscriber<CommentJson> subscriber, String id) {
        Observable observable = zhihuApi.getShortComments(id);
        toSubscribe(observable, subscriber);
    }

    public void getLongComments(Subscriber<CommentJson> subscriber, String id) {
        Observable observable = zhihuApi.getLongComments(id);
        toSubscribe(observable, subscriber);
    }

    public void getThemes(Subscriber<ThemesJson> subscriber) {
        Observable observable = zhihuApi.getThemes();
        toSubscribe(observable, subscriber);
    }

    public void getTheme(Subscriber<ThemeJson> subscriber, String id) {
        Observable observable = zhihuApi.getTheme(id);
        toSubscribe(observable, subscriber);
    }

    public void getHot(Subscriber<HotJson> subscriber) {
        Observable observable = zhihuApi.getHot();
        toSubscribe(observable, subscriber);
    }

    public void getSections(Subscriber<SectionsJson> subscriber) {
        Observable observable = zhihuApi.getSections();
        toSubscribe(observable, subscriber);
    }

    public void getSection(Subscriber<SectionJson> subscriber, String id) {
        Observable observable = zhihuApi.getSection(id);
        toSubscribe(observable, subscriber);
    }

    public void getSectionBefore(Subscriber<SectionJson> subscriber, String id, String timestamp) {
        Observable observable = zhihuApi.getSectionBefore(id, timestamp);
        toSubscribe(observable, subscriber);
    }

    public void getRecommends(Subscriber<RecommendsJson> subscriber, String id) {
        Observable observable = zhihuApi.getRecommends(id);
        toSubscribe(observable, subscriber);
    }


    private void toSubscribe(Observable o, Subscriber s){
        o.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s);
    }
}
