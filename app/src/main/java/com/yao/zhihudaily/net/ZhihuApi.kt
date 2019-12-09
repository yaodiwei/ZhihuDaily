package com.yao.zhihudaily.net

import com.yao.zhihudaily.model.*
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Streaming
import retrofit2.http.Url

/**
 * @author Yao
 * @date 2016/9/21
 */
interface ZhihuApi {

    @get:GET("4/start-image/1080*1776")
    val startImage: Observable<StartImageJson>

    @get:GET("4/news/latest")
    val dailies: Observable<DailiesJson>

    @get:GET("4/themes")
    val themes: Observable<ThemesJson>

    @get:GET("3/news/hot")
    val hot: Observable<HotJson>

    @get:GET("3/sections")
    val sections: Observable<SectionsJson>

    @Streaming
    @GET
    fun getStartImageFile(@Url url: String): Observable<ResponseBody> //通过@Url覆盖baseurl

    @GET("4/news/before/{date}")
    fun getDailiesBefore(@Path("date") date: String): Observable<DailiesJson>

    @GET("4/news/{id}")
    fun getNews(@Path("id") id: String): Observable<DailyJson>

    @GET("4/story-extra/{id}")
    fun getStoryExtra(@Path("id") id: String): Observable<StoryExtra>

    @GET("4/story/{id}/short-comments")
    fun getShortComments(@Path("id") id: String): Observable<CommentJson>

    @GET("4/story/{id}/long-comments")
    fun getLongComments(@Path("id") id: String): Observable<CommentJson>

    @GET("4/theme/{id}")
    fun getTheme(@Path("id") id: String): Observable<ThemeJson>

    @GET("3/section/{id}")
    fun getSection(@Path("id") id: String): Observable<SectionJson>

    @GET("3/section/{id}/before/{timestamp}")
    fun getSectionBefore(@Path("id") id: String, @Path("timestamp") timestamp: String): Observable<SectionJson>

    @GET("4/story/{id}/recommenders")
    fun getRecommends(@Path("id") id: String): Observable<RecommendsJson>

    @GET("4/editor/{id}/profile-page/android")
    fun getEditor(@Path("id") id: String): Observable<*>

}
