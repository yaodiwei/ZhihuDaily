package com.yao.zhihudaily.net

import com.yao.zhihudaily.model.*
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * @author Yao
 * @date 2016/9/21
 */
class ZhihuHttp private constructor() {

    private val okHttpClient: OkHttpClient

    private val retrofit: Retrofit

    private val zhihuApi: ZhihuApi

    val startImage: Observable<StartImageJson>
        get() = zhihuApi.startImage
                .compose(applySchedulers())
                .observeOn(Schedulers.io())

    val dailies: Observable<DailiesJson>
        get() = zhihuApi.dailies.compose(applySchedulers())

    val themes: Observable<ThemesJson>
        get() = zhihuApi.themes.compose(applySchedulers())

    val hot: Observable<HotJson>
        get() = zhihuApi.hot.compose(applySchedulers())

    val sections: Observable<SectionsJson>
        get() = zhihuApi.sections.compose(applySchedulers())

    init {
        okHttpClient = OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).build()

        retrofit = Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(ZHIHU_BASE_URL)
                .build()

        zhihuApi = retrofit.create(ZhihuApi::class.java)
    }

    fun getDailiesBefore(date: String): Observable<DailiesJson> {
        return zhihuApi.getDailiesBefore(date).compose(applySchedulers())
    }

    fun getNews(id: String): Observable<DailyJson> {
        return zhihuApi.getNews(id).compose(applySchedulers())
    }

    fun getStoryExtra(id: String): Observable<StoryExtra> {
        return zhihuApi.getStoryExtra(id).compose(applySchedulers())
    }

    fun getShortComments(id: String): Observable<CommentJson> {
        return zhihuApi.getShortComments(id).compose(applySchedulers())
    }

    fun getLongComments(id: String): Observable<CommentJson> {
        return zhihuApi.getLongComments(id).compose(applySchedulers())
    }

    fun getTheme(id: String): Observable<ThemeJson> {
        return zhihuApi.getTheme(id).compose(applySchedulers())
    }

    fun getSection(id: String): Observable<SectionJson> {
        return zhihuApi.getSection(id).compose(applySchedulers())
    }

    fun getSectionBefore(id: String, timestamp: String): Observable<SectionJson> {
        return zhihuApi.getSectionBefore(id, timestamp).compose(applySchedulers())
    }

    fun getRecommends(id: String): Observable<RecommendsJson> {
        return zhihuApi.getRecommends(id).compose(applySchedulers())
    }

    companion object {

        val ZHIHU_BASE_URL = "http://news-at.zhihu.com/api/"

        val zhihuHttp = ZhihuHttp()

        private fun <T> applySchedulers(): ObservableTransformer<T, T> {
            return ObservableTransformer { upstream ->
                upstream
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
            }
        }
    }
}
