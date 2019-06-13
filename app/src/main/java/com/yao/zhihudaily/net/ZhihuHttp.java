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
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author Yao
 * @date 2016/9/21
 */

public class ZhihuHttp {

    public static final String ZHIHU_BASE_URL = "http://news-at.zhihu.com/api/";

    private static final ZhihuHttp zhihuHttp = new ZhihuHttp();

    private OkHttpClient okHttpClient;

    private Retrofit retrofit;

    private ZhihuApi zhihuApi;

    private ZhihuHttp() {
        okHttpClient = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).build();

        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(ZHIHU_BASE_URL)
                .build();

        zhihuApi = retrofit.create(ZhihuApi.class);
    }

    public static ZhihuHttp getZhihuHttp() {
        return zhihuHttp;
    }

    public Observable<StartImageJson> getStartImage() {
        return zhihuApi.getStartImage()
                .compose(applySchedulers())
                .observeOn(Schedulers.io());
    }

    public Observable<DailiesJson> getDailies() {
        return zhihuApi.getDailies().compose(applySchedulers());
    }

    public Observable<DailiesJson> getDailiesBefore(String date) {
        return zhihuApi.getDailiesBefore(date).compose(applySchedulers());
    }

    public Observable<DailyJson> getNews(String id) {
        return zhihuApi.getNews(id).compose(applySchedulers());
    }

    public Observable<StoryExtra> getStoryExtra(String id) {
        return zhihuApi.getStoryExtra(id).compose(applySchedulers());
    }

    public Observable<CommentJson> getShortComments(String id) {
        return zhihuApi.getShortComments(id).compose(applySchedulers());
    }

    public Observable<CommentJson> getLongComments(String id) {
        return zhihuApi.getLongComments(id).compose(applySchedulers());
    }

    public Observable<ThemesJson> getThemes() {
        return zhihuApi.getThemes().compose(applySchedulers());
    }

    public Observable<ThemeJson> getTheme(String id) {
        return zhihuApi.getTheme(id).compose(applySchedulers());
    }

    public Observable<HotJson> getHot() {
        return zhihuApi.getHot().compose(applySchedulers());
    }

    public Observable<SectionsJson> getSections() {
        return zhihuApi.getSections().compose(applySchedulers());
    }

    public Observable<SectionJson> getSection(String id) {
        return zhihuApi.getSection(id).compose(applySchedulers());
    }

    public Observable<SectionJson> getSectionBefore(String id, String timestamp) {
        return zhihuApi.getSectionBefore(id, timestamp).compose(applySchedulers());
    }

    public Observable<RecommendsJson> getRecommends(String id) {
        return zhihuApi.getRecommends(id).compose(applySchedulers());
    }

    private static <T> ObservableTransformer<T, T> applySchedulers() {
        return upstream -> upstream
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
