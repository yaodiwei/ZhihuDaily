package com.yao.zhihudaily.net;

import android.util.Log;

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

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author Yao
 * @date 2016/9/21
 */
public class ZhihuHttp {

    public static final String ZHIHU_BASE_URL = "https://news-at.zhihu.com/api/";

    private static final ZhihuHttp zhihuHttp = new ZhihuHttp();

    private OkHttpClient okHttpClient;

    private Retrofit retrofit;

    private ZhihuApi zhihuApi;

    private static SSLSocketFactory createSSLSocketFactory() {
        SSLSocketFactory sSLSocketFactory = null;
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{new TrustAllManager()},
                    new SecureRandom());
            sSLSocketFactory = sc.getSocketFactory();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sSLSocketFactory;
    }

    private static class TrustAllManager implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }

    private static class TrustAllHostnameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }

    private ZhihuHttp() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(10, TimeUnit.SECONDS);

        // Debug 模式下信息所有证书，方便开发时候抓包
//        if (BuildConfig.DEBUG) {
//            builder.sslSocketFactory(createSSLSocketFactory(), new TrustAllManager());
//            builder.hostnameVerifier(new TrustAllHostnameVerifier());
//        }

        // 只信任网站对应的证书
//        CertificatePinner certificatePinner = new CertificatePinner.Builder()
//                //正常请求下的证书验证链路
//                .add("news-at.zhihu.com", "sha256/f5fNYvDJUKFsO51UowKkyKAlWXZXpaGK6Bah4yX9zmI=")//CN=*.zhihu.com,OU=IT,O=智者四海（北京）技术有限公司,L=北京市,C=CN
//                .add("news-at.zhihu.com", "sha256/zUIraRNo+4JoAYA7ROeWjARtIoN4rIEbCpfCRQT6N6A=")//CN=GeoTrust RSA CA 2018,OU=www.digicert.com,O=DigiCert Inc,C=US
//                .add("news-at.zhihu.com", "sha256/r/mIkG3eEpVdm+u/ko/cwxzOMo1bk4TyHIlByibiA5E=")//CN=DigiCert Global Root CA,OU=www.digicert.com,O=DigiCert Inc,C=US
//
//                //charles 抓包下的配置
//                .add("news-at.zhihu.com", "sha256/dVUJFtUhQtJki5t0/j+hMYzTgtVkETqjsogUuyquPPo=")//CN=*.zhihu.com,OU=IT,O=智者四海（北京）技术有限公司,L=北京市,C=CN
//                .add("news-at.zhihu.com", "sha256/54ZQa+M6vq6DhdR7DLkc1X6fWmVEZ6wLZaaYwoR4Uvw=")//C=NZ,ST=Auckland,L=Auckland,O=XK72 Ltd,OU=https://charlesproxy.com/ssl,CN=Charles Proxy CA (2 十月 2017\, YaodeMacBook-Pro.local)
//                .build();
//        builder.certificatePinner(certificatePinner);
        okHttpClient = builder.build();

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

    public void getDailiesWithCallback() {
        Request request = new Request.Builder()
                .url(ZHIHU_BASE_URL + "4/news/latest")
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e("YAO", "ZhihuHttp.java - onFailure() ----- e:" + e.toString());
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                Log.e("YAO", "ZhihuHttp.java - onResponse() ----- :" + response.toString());
            }
        });
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
