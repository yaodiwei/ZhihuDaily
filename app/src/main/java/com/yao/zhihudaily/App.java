package com.yao.zhihudaily;

import android.app.Application;

import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by Administrator on 2016/7/23.
 */
public class App extends Application{

    public static App app;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;

        //Logger配置
        Logger
            .init("DIWEI")                 // default PRETTYLOGGER or use just init()
            .methodCount(3)                 // default 2  记录方法调用链的行数,0为隐藏这个模块
            .hideThreadInfo()               // default shown
            .logLevel(LogLevel.FULL)        // default LogLevel.FULL
            .methodOffset(0);                // default 0

        //umeng配置
        //打开友盟
        MobclickAgent.setDebugMode(true);
        //场景设置
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);
        //设置多少秒间隔后,从后台返回app前端,会被认为是两次独立的启动
//        MobclickAgent.setSessionContinueMillis(30);
        //设置日志加密
        MobclickAgent.enableEncrypt(true);//6.0.0版本及以后

        //报告信息
        //MobclickAgent.reportError(this, DeviceUtil.getAllDeviceInfo());

    }


}
