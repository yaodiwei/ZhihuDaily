package com.yao.zhihudaily.net

import com.yao.zhihudaily.model.*
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * @author Yao
 * @date 2016/9/21
 */
class ZhihuHttp private constructor() {

    private val mZhihuApiService = ZhihuApiService.mZhihuApiService

    fun startImage(): Observable<StartImageJson> {
        return mZhihuApiService.startImage()
                .compose(applySchedulers())
                .observeOn(Schedulers.io())
    }

    fun getDailies(): Observable<DailiesJson> {
        return mZhihuApiService.getDailies().compose(applySchedulers())
    }

    fun getDailiesBefore(date: String): Observable<DailiesJson> {
        return mZhihuApiService.getDailiesBefore(date).compose(applySchedulers())
    }

    fun getThemes(): Observable<ThemesJson> {
        return mZhihuApiService.getThemes().compose(applySchedulers())
    }

    fun getHot(): Observable<HotJson> {
        return mZhihuApiService.getHot().compose(applySchedulers())
    }

    suspend fun getHotCoroutine(): HotJson {
        return mZhihuApiService.getHotCoroutine()
    }

    fun getSections(): Observable<SectionsJson> {
        return mZhihuApiService.getSections().compose(applySchedulers())
    }

    fun getNews(id: String): Observable<DailyJson> {
        return mZhihuApiService.getNews(id).compose(applySchedulers())
    }

    fun getStoryExtra(id: String): Observable<StoryExtra> {
        return mZhihuApiService.getStoryExtra(id).compose(applySchedulers())
    }

    fun getShortComments(id: String): Observable<CommentJson> {
        return mZhihuApiService.getShortComments(id).compose(applySchedulers())
    }

    fun getLongComments(id: String): Observable<CommentJson> {
        return mZhihuApiService.getLongComments(id).compose(applySchedulers())
    }

    fun getTheme(id: String): Observable<ThemeJson> {
        return mZhihuApiService.getTheme(id).compose(applySchedulers())
    }

    fun getSection(id: String): Observable<SectionJson> {
        return mZhihuApiService.getSection(id).compose(applySchedulers())
    }

    fun getSectionBefore(id: String, timestamp: String): Observable<SectionJson> {
        return mZhihuApiService.getSectionBefore(id, timestamp).compose(applySchedulers())
    }

    fun getRecommends(id: String): Observable<RecommendsJson> {
        return mZhihuApiService.getRecommends(id).compose(applySchedulers())
    }

    companion object {

        val mZhihuHttp = ZhihuHttp()

        private fun <T> applySchedulers(): ObservableTransformer<T, T> {
            return ObservableTransformer { upstream ->
                upstream
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
            }
        }
    }
}
