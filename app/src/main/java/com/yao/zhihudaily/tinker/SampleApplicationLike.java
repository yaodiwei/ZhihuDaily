/*
 * Tencent is pleased to support the open source community by making Tinker available.
 *
 * Copyright (C) 2016 THL A29 Limited, a Tencent company. All rights reserved.
 *
 * Licensed under the BSD 3-Clause License (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * https://opensource.org/licenses/BSD-3-Clause
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" basis, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.yao.zhihudaily.tinker;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;
import com.tencent.tinker.anno.DefaultLifeCycle;
import com.tencent.tinker.entry.ApplicationLifeCycle;
import com.tencent.tinker.entry.DefaultApplicationLike;
import com.tencent.tinker.lib.tinker.Tinker;
import com.tencent.tinker.loader.shareutil.ShareConstants;
import com.umeng.analytics.MobclickAgent;
import com.yao.zhihudaily.util.AppUtil;

import org.greenrobot.eventbus.EventBus;

import androidx.multidex.MultiDex;

/**
 * because you can not use any other class in your application, we need to
 * move your implement of Application to {@link ApplicationLifeCycle}
 * As Application, all its direct reference class should be in the main dex.
 *
 * We use tinker-android-anno to make sure all your classes can be patched.
 *
 * application: if it is start with '.', we will add SampleApplicationLifeCycle's package name
 *
 * flags:
 * TINKER_ENABLE_ALL: support dex, lib and resource
 * TINKER_DEX_MASK: just support dex
 * TINKER_NATIVE_LIBRARY_MASK: just support lib
 * TINKER_RESOURCE_MASK: just support resource
 *
 * loaderClass: define the tinker loader class, we can just use the default TinkerLoader
 *
 * loadVerifyFlag: whether check files' md5 on the load time, defualt it is false.
 *
 * Created by zhangshaowen on 16/3/17.
 */
@SuppressWarnings("unused")
@DefaultLifeCycle(application = "com.yao.zhihudaily.tinker.SampleApplication",
                  flags = ShareConstants.TINKER_ENABLE_ALL,
                  loadVerifyFlag = false)
public class SampleApplicationLike extends DefaultApplicationLike {
    private static final String TAG = "Tinker.SampleApplicationLike";

    public SampleApplicationLike(Application application, int tinkerFlags, boolean tinkerLoadVerifyFlag,
                                 long applicationStartElapsedTime, long applicationStartMillisTime, Intent tinkerResultIntent) {
        super(application, tinkerFlags, tinkerLoadVerifyFlag, applicationStartElapsedTime, applicationStartMillisTime, tinkerResultIntent);
    }

    /**
     * install multiDex before install tinker
     * so we don't need to put the tinker lib classes in the main dex
     *
     * @param base
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void onBaseContextAttached(Context base) {
        super.onBaseContextAttached(base);
        //you must install multiDex whatever tinker is installed!
        MultiDex.install(base);

        SampleApplicationContext.application = getApplication();
        SampleApplicationContext.context = getApplication();
        TinkerManager.setTinkerApplicationLike(this);

        TinkerManager.initFastCrashProtect();
        //should set before tinker is installed
        TinkerManager.setUpgradeRetryEnable(true);

        //optional set logIml, or you can use default debug log
        //TinkerInstaller.setLogIml(new MyLogImp());

        //installTinker after load multiDex
        //or you can put com.tencent.tinker.** to main dex
        TinkerManager.installTinker(this);
        Tinker tinker = Tinker.with(getApplication());
    }

    @Override
    public void onCreate() {
        super.onCreate();

        AppUtil.init(getApplication());

        EventBus eventBus = EventBus.builder()
                .logNoSubscriberMessages(false)
                .sendNoSubscriberEvent(false)
                .build();

        //Logger配置
        Logger.init("Yao")// default PRETTYLOGGER or use just init()
                .methodCount(3)// default 2  记录方法调用链的行数,0为隐藏这个模块
                .hideThreadInfo()// default shown
                .logLevel(LogLevel.FULL)// default LogLevel.FULL
                .methodOffset(0);// default 0

        umengAnalytics();
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public void registerActivityLifecycleCallbacks(Application.ActivityLifecycleCallbacks callback) {
        getApplication().registerActivityLifecycleCallbacks(callback);
    }

    private void umengAnalytics() {
        /* -------------------- 友盟统计配置 -------------------- */
        //打开友盟
        MobclickAgent.setDebugMode(true);

        //场景设置:普通统计场景类型
        MobclickAgent.setScenarioType(getApplication(), MobclickAgent.EScenarioType.E_UM_NORMAL);
        //设置多少秒间隔后,从后台返回app前端,会被认为是两次独立的启动
//        MobclickAgent.setSessionContinueMillis(30);
        //设置日志加密
        MobclickAgent.enableEncrypt(true);//6.0.0版本及以后
        //报告信息
        //MobclickAgent.reportError(this, DeviceUtil.getAllDeviceInfo());
        /* -------------------- 友盟统计配置 -------------------- */
    }

}
