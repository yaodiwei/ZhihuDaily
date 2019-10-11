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
import android.util.Log;

import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;
import com.tencent.tinker.anno.DefaultLifeCycle;
import com.tencent.tinker.entry.ApplicationLifeCycle;
import com.tencent.tinker.entry.DefaultApplicationLike;
import com.tencent.tinker.lib.listener.DefaultPatchListener;
import com.tencent.tinker.lib.patch.UpgradePatch;
import com.tencent.tinker.lib.reporter.DefaultLoadReporter;
import com.tencent.tinker.lib.reporter.DefaultPatchReporter;
import com.tencent.tinker.lib.service.PatchResult;
import com.tencent.tinker.loader.shareutil.ShareConstants;
import com.tinkerpatch.sdk.TinkerPatch;
import com.tinkerpatch.sdk.server.callback.ConfigRequestCallback;
import com.tinkerpatch.sdk.server.callback.RollbackCallBack;
import com.tinkerpatch.sdk.server.callback.TinkerPatchRequestCallback;
import com.tinkerpatch.sdk.tinker.callback.ResultCallBack;
import com.tinkerpatch.sdk.tinker.service.TinkerServerResultService;
import com.umeng.analytics.MobclickAgent;
import com.yao.zhihudaily.BuildConfig;
import com.yao.zhihudaily.util.AppUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;

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
    private static final String TAG = "SampleApplicationLike";

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

        initTinker();
    }

    private void initTinker() {
        if (BuildConfig.TINKER_ENABLE) {
            //开始检查是否有补丁，这里配置的是每隔访问3小时服务器是否有更新。
            TinkerPatch.init(this)
                    .reflectPatchLibrary()
                    .setPatchRollbackOnScreenOff(true)
                    .setPatchRestartOnSrceenOff(true)
                    .setFetchPatchIntervalByHours(3);

            // 获取当前的补丁版本
            Log.d(TAG, "current patch version is " + TinkerPatch.with().getPatchVersion());

            //每隔3个小时去访问后台时候有更新,通过handler实现轮训的效果
            TinkerPatch.with().fetchPatchUpdateAndPollWithInterval();
        }
    }

    /**
     * 在这里给出TinkerPatch的所有接口解释
     * 更详细的解释请参考:http://tinkerpatch.com/Docs/api
     */
    private void useSample() {
        TinkerPatch.init(this)
                //是否自动反射Library路径,无须手动加载补丁中的So文件
                //注意,调用在反射接口之后才能生效,你也可以使用Tinker的方式加载Library
                .reflectPatchLibrary()
                //向后台获取是否有补丁包更新,默认的访问间隔为3个小时
                //若参数为true,即每次调用都会真正的访问后台配置
                .fetchPatchUpdate(false)
                //设置访问后台补丁包更新配置的时间间隔,默认为3个小时
                .setFetchPatchIntervalByHours(3)
                //向后台获得动态配置,默认的访问间隔为3个小时
                //若参数为true,即每次调用都会真正的访问后台配置
                .fetchDynamicConfig(new ConfigRequestCallback() {
                    @Override
                    public void onSuccess(HashMap<String, String> hashMap) {

                    }

                    @Override
                    public void onFail(Exception e) {

                    }
                }, false)
                //设置访问后台动态配置的时间间隔,默认为3个小时
                .setFetchDynamicConfigIntervalByHours(3)
                //设置当前渠道号,对于某些渠道我们可能会想屏蔽补丁功能
                //设置渠道后,我们就可以使用后台的条件控制渠道更新
                .setAppChannel("default")
                //屏蔽部分渠道的补丁功能
                .addIgnoreAppChannel("googleplay")
                //设置tinkerpatch平台的条件下发参数
                .setPatchCondition("test", "1")
                //设置补丁合成成功后,锁屏重启程序
                //默认是等应用自然重启
                .setPatchRestartOnSrceenOff(true)
                //我们可以通过ResultCallBack设置对合成后的回调
                //例如弹框什么
                .setPatchResultCallback(new ResultCallBack() {
                    @Override
                    public void onPatchResult(PatchResult patchResult) {
                        Log.i(TAG, "onPatchResult callback here");
                    }
                })
                //设置收到后台回退要求时,锁屏清除补丁
                //默认是等主进程重启时自动清除
                .setPatchRollbackOnScreenOff(true)
                //我们可以通过RollbackCallBack设置对回退时的回调
                .setPatchRollBackCallback(new RollbackCallBack() {
                    @Override
                    public void onPatchRollback() {
                        Log.i(TAG, "onPatchRollback callback here");
                    }
                });
    }

    /**
     * 自定义Tinker类的高级用法,一般不推荐使用
     * 更详细的解释请参考:http://tinkerpatch.com/Docs/api
     */
    private void complexSample() {
        TinkerPatch.Builder builder = new TinkerPatch.Builder(this);
        //修改tinker的构造函数,自定义类
        builder.listener(new DefaultPatchListener(getApplication()))
                .loadReporter(new DefaultLoadReporter(getApplication()))
                .patchReporter(new DefaultPatchReporter(getApplication()))
                .resultServiceClass(TinkerServerResultService.class)
                .upgradePatch(new UpgradePatch())
                .patchRequestCallback(new TinkerPatchRequestCallback());

        TinkerPatch.init(builder.build());
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