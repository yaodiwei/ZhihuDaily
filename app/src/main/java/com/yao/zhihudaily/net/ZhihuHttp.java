package com.yao.zhihudaily.net;

import com.yao.zhihudaily.model.CommentJson;
import com.yao.zhihudaily.model.DailiesJson;
import com.yao.zhihudaily.model.DailyJson;
import com.yao.zhihudaily.model.HotJson;
import com.yao.zhihudaily.model.RecommendsJson;
import com.yao.zhihudaily.model.SectionJson;
import com.yao.zhihudaily.model.SectionsJson;
import com.yao.zhihudaily.model.StartImageJson;
import com.yao.zhihudaily.model.StoryExtra;
import com.yao.zhihudaily.model.ThemeJson;
import com.yao.zhihudaily.model.ThemesJson;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

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
                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
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

    public Observable<StartImageJson> getStartImage() {
        return zhihuApi.getStartImage().subscribeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io());
    }

    public void getDailies(Observer<DailiesJson> observer) {
        Observable observable = zhihuApi.getDailies();
        toSubscribe(observable, observer);
    }

    public void getDailiesBefore(Observer<DailiesJson> observer, String date) {
        Observable observable = zhihuApi.getDailiesBefore(date);
        toSubscribe(observable, observer);
    }

    public void getNews(Observer<DailyJson> observer, String id) {
        Observable observable = zhihuApi.getNews(id);
        toSubscribe(observable, observer);
    }

    public void getStoryExtra(Observer<StoryExtra> observer, String id) {
        Observable observable = zhihuApi.getStoryExtra(id);
        toSubscribe(observable, observer);
    }

    public void getShortComments(Observer<CommentJson> observer, String id) {
        Observable observable = zhihuApi.getShortComments(id);
        toSubscribe(observable, observer);
    }

    public void getLongComments(Observer<CommentJson> observer, String id) {
        Observable observable = zhihuApi.getLongComments(id);
        toSubscribe(observable, observer);
    }

    public void getThemes(Observer<ThemesJson> observer) {
        Observable observable = zhihuApi.getThemes();
        toSubscribe(observable, observer);
    }

    public void getTheme(Observer<ThemeJson> observer, String id) {
        Observable observable = zhihuApi.getTheme(id);
        toSubscribe(observable, observer);
    }

    public void getHot(Observer<HotJson> observer) {
        Observable observable = zhihuApi.getHot();
        toSubscribe(observable, observer);
    }

    public void getSections(Observer<SectionsJson> observer) {
        Observable observable = zhihuApi.getSections();
        toSubscribe(observable, observer);
    }

    public void getSection(Observer<SectionJson> observer, String id) {
        Observable observable = zhihuApi.getSection(id);
        toSubscribe(observable, observer);
    }

    public void getSectionBefore(Observer<SectionJson> observer, String id, String timestamp) {
        Observable observable = zhihuApi.getSectionBefore(id, timestamp);
        toSubscribe(observable, observer);
    }

    public void getRecommends(Observer<RecommendsJson> observer, String id) {
        Observable observable = zhihuApi.getRecommends(id);
        toSubscribe(observable, observer);
    }


    private void toSubscribe(Observable observable, Observer observer){
        observable.subscribeOn(Schedulers.io())
//                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
}
