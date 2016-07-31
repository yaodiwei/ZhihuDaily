package com.yao.zhihudaily;

import android.app.Application;

/**
 * Created by Administrator on 2016/7/23.
 */
public class App extends Application{

    public static App app;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
    }
}
