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

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by Administrator on 2016/9/21.
 */

public interface ZhihuApi {

    @GET("4/start-image/1080*1776")
    Observable<StartImageJson> getStartImage();

    @Streaming//注明为流文件，防止retrofit将大文件读入内存
    @GET
    Observable<ResponseBody> getStartImageFile(@Url String url);//通过@Url覆盖baseurl

    @GET("4/news/latest")
    Observable<DailiesJson> getDailies();

    @GET("4/news/before/{date}")
    Observable<DailiesJson> getDailiesBefore(@Path("date") String date);

    @GET("4/news/{id}")
    Observable<DailyJson> getNews(@Path("id") String id);

    @GET("4/story-extra/{id}")
    Observable<StoryExtra> getStoryExtra(@Path("id") String id);

    @GET("4/story/{id}/short-comments")
    Observable<CommentJson> getShortComments(@Path("id") String id);

    @GET("4/story/{id}/long-comments")
    Observable<CommentJson> getLongComments(@Path("id") String id);

    @GET("4/themes")
    Observable<ThemesJson> getThemes();

    @GET("4/theme/{id}")
    Observable<ThemeJson> getTheme(@Path("id") String id);

    @GET("3/news/hot")
    Observable<HotJson> getHot();

    @GET("3/sections")
    Observable<SectionsJson> getSections();

    @GET("3/section/{id}")
    Observable<SectionJson> getSection(@Path("id") String id);

    @GET("3/section/{id}/before/{timestamp}")
    Observable<SectionJson> getSectionBefore(@Path("id") String id, @Path("timestamp") String timestamp);

    @GET("4/story/{id}/recommenders")
    Observable<RecommendsJson> getRecommends(@Path("id") String id);

    @GET("4/editor/{id}/profile-page/android")
    Observable getEditor(@Path("id") String id);

}
