package com.yao.zhihudaily.net;

import com.yao.zhihudaily.model.DailiesJson;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

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
                            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
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

    public void getDailies(Subscriber<DailiesJson> subscriber) {
        zhihuApi.getDailies()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
}
