package com.yao.zhihudaily

import android.app.Application

import com.orhanobut.logger.LogLevel
import com.orhanobut.logger.Logger
import com.umeng.analytics.MobclickAgent
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

        umengAnalytics()
    }

    private fun umengAnalytics() {
        /* -------------------- 友盟统计配置 -------------------- */
        //打开友盟
        MobclickAgent.setDebugMode(true)

        //场景设置:普通统计场景类型
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL)
        //设置多少秒间隔后,从后台返回app前端,会被认为是两次独立的启动
        //        MobclickAgent.setSessionContinueMillis(30);
        //设置日志加密
        MobclickAgent.enableEncrypt(true)//6.0.0版本及以后
        //报告信息
        //MobclickAgent.reportError(this, DeviceUtil.getAllDeviceInfo());
        /* -------------------- 友盟统计配置 -------------------- */
    }

}
