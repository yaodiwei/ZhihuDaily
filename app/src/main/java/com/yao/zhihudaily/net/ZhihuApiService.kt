package com.yao.zhihudaily.net

import com.yao.zhihudaily.BuildConfig
import com.yao.zhihudaily.model.*
import io.reactivex.Observable
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Streaming
import retrofit2.http.Url
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.*

/**
 * @author Yao
 * @date 2016/9/21
 */
interface ZhihuApiService {

    private class TrustAllManager : X509TrustManager {
        @Throws(CertificateException::class)
        override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {
        }

        @Throws(CertificateException::class)
        override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
        }

        override fun getAcceptedIssuers(): Array<X509Certificate> {
            return emptyArray()
        }
    }

    private class TrustAllHostnameVerifier : HostnameVerifier {
        override fun verify(hostname: String, session: SSLSession): Boolean {
            return true
        }
    }

    companion object Factory {

        val ZHIHU_BASE_URL = "https://news-at.zhihu.com/api/"

        val mZhihuApiService = create()

        fun createSSLSocketFactory(): SSLSocketFactory? {
            var sSLSocketFactory: SSLSocketFactory? = null
            try {
                val sc = SSLContext.getInstance("TLS")
                sc.init(null, arrayOf<TrustManager>(TrustAllManager()),
                        SecureRandom())
                sSLSocketFactory = sc.socketFactory
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return sSLSocketFactory
        }

        fun create(): ZhihuApiService {

            val builder = OkHttpClient.Builder()
            builder.connectTimeout(10, TimeUnit.SECONDS)
            if (BuildConfig.DEBUG) {
                builder.sslSocketFactory(createSSLSocketFactory()!!, TrustAllManager())
                builder.hostnameVerifier(TrustAllHostnameVerifier())
            }
            val okHttpClient = builder.build()

            val retrofit = Retrofit.Builder()
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .baseUrl(ZHIHU_BASE_URL)
                    .build()

            return retrofit.create(ZhihuApiService::class.java)
        }
    }

    @GET("4/start-image/1080*1776")
    fun startImage(): Observable<StartImageJson>

    @Streaming
    @GET
    fun getStartImageFile(@Url url: String): Observable<ResponseBody> //通过@Url覆盖baseurl

    @GET("4/news/latest")
    fun getDailies(): Observable<DailiesJson>

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

    @GET("4/themes")
    fun getThemes(): Observable<ThemesJson>

    @GET("4/theme/{id}")
    fun getTheme(@Path("id") id: String): Observable<ThemeJson>

    @GET("3/news/hot")
    fun getHot(): Observable<HotJson>

    @GET("3/news/hot")
    suspend fun getHotCoroutine(): HotJson

    @GET("3/sections")
    fun getSections(): Observable<SectionsJson>

    @GET("3/section/{id}")
    fun getSection(@Path("id") id: String): Observable<SectionJson>

    @GET("3/section/{id}/before/{timestamp}")
    fun getSectionBefore(@Path("id") id: String, @Path("timestamp") timestamp: String): Observable<SectionJson>

    @GET("4/story/{id}/recommenders")
    fun getRecommends(@Path("id") id: String): Observable<RecommendsJson>

    @GET("4/editor/{id}/profile-page/android")
    fun getEditor(@Path("id") id: String): Observable<*>

}
