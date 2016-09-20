package com.yao.zhihudaily.net;

import com.yao.zhihudaily.model.DailiesJson;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by Administrator on 2016/9/21.
 */

public interface ZhihuApi {

    @GET("4/news/latest")
    Observable<DailiesJson> getDailies();

    @GET("4/news/before/{date}")
    void getDailiesBefore(@Path("date") String date);

    @GET("4/news/{id}")
    void getNews(@Path("id") String id);

    @GET("4/story-extra/{id}")
    void getStoryExtra(@Path("id") String id);

    @GET("4/story/{id}/short-comments")
    void getShortComments(@Path("id") String id);

    @GET("4/story/{id}/long-comments")
    void getLongComments(@Path("id") String id);

    @GET("4/themes")
    void getThemes();

    @GET("4/theme/{id}")
    void getTheme(@Path("id") String id);

    @GET("3/news/hot")
    void getHot();

    @GET("3/sections")
    void getSections();

    @GET("3/section/{id}")
    void getSection(@Path("id") String id);

    @GET("3/section/{id}/before/{timestamp}")
    void getSectionBefore(@Path("id") String id, @Path("timestamp") String timestamp);

    @GET("4/story/{id}/recommenders")
    void getRecommends(@Path("id") String id);

    @GET("4/editor/{id}/profile-page/android")
    void getEditor(@Path("id") String id);

}
