package com.yao.zhihudaily.net

import com.yao.zhihudaily.BuildConfig
import com.yao.zhihudaily.model.*
import io.reactivex.Observable
import okhttp3.CertificatePinner
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

            // Debug 模式下信息所有证书，方便开发时候抓包
//            if (BuildConfig.DEBUG) {
//                builder.sslSocketFactory(createSSLSocketFactory()!!, TrustAllManager());
//                builder.hostnameVerifier(TrustAllHostnameVerifier());
//            }

            // 只信任网站对应的证书
//            val certificatePinner = CertificatePinner.Builder()
//                    //正常请求下的证书验证链路
//                    .add("news-at.zhihu.com", "sha256/f5fNYvDJUKFsO51UowKkyKAlWXZXpaGK6Bah4yX9zmI=")//CN=*.zhihu.com,OU=IT,O=智者四海（北京）技术有限公司,L=北京市,C=CN
//                    .add("news-at.zhihu.com", "sha256/zUIraRNo+4JoAYA7ROeWjARtIoN4rIEbCpfCRQT6N6A=")//CN=GeoTrust RSA CA 2018,OU=www.digicert.com,O=DigiCert Inc,C=US
//                    .add("news-at.zhihu.com", "sha256/r/mIkG3eEpVdm+u/ko/cwxzOMo1bk4TyHIlByibiA5E=")//CN=DigiCert Global Root CA,OU=www.digicert.com,O=DigiCert Inc,C=US
//                    //charles 抓包下的配置
//                    .add("news-at.zhihu.com", "sha256/dVUJFtUhQtJki5t0/j+hMYzTgtVkETqjsogUuyquPPo=")//CN=*.zhihu.com,OU=IT,O=智者四海（北京）技术有限公司,L=北京市,C=CN
//                    .add("news-at.zhihu.com", "sha256/54ZQa+M6vq6DhdR7DLkc1X6fWmVEZ6wLZaaYwoR4Uvw=")//C=NZ,ST=Auckland,L=Auckland,O=XK72 Ltd,OU=https://charlesproxy.com/ssl,CN=Charles Proxy CA (2 十月 2017\, YaodeMacBook-Pro.local)
//                    .build();
//            builder.certificatePinner(certificatePinner);

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
