package com.yao.zhihudaily

import android.app.Application

import com.orhanobut.logger.LogLevel
import com.orhanobut.logger.Logger
import com.yao.zhihudaily.util.AppUtil

import org.greenrobot.eventbus.EventBus

/**
 * @author Yao
 * @date 2016/7/23
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        AppUtil.init(this)

        val eventBus = EventBus.builder()
                .logNoSubscriberMessages(false)
                .sendNoSubscriberEvent(false)
                .build()

        //Logger配置
        Logger.init("Yao")// default PRETTYLOGGER or use just init()
                .methodCount(3)// default 2  记录方法调用链的行数,0为隐藏这个模块
                .hideThreadInfo()// default shown
                .logLevel(LogLevel.FULL)// default LogLevel.FULL
                .methodOffset(0)// default 0

    }

}
