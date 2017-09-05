package com.yao.zhihudaily;

import android.app.Application;
import android.app.Notification;
import android.content.Context;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;

import java.util.Map;

/**
 * Created by Administrator on 2016/7/23.
 */
public class App extends Application {

    public static App app;
    public static String mDeviceToken;

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


        umengAnalytics();
        umengPush();
    }

    private void umengAnalytics() {
    /* -------------------- 友盟统计配置 -------------------- */
        //打开友盟
        MobclickAgent.setDebugMode(true);
        //场景设置:普通统计场景类型
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);
        //设置多少秒间隔后,从后台返回app前端,会被认为是两次独立的启动
//        MobclickAgent.setSessionContinueMillis(30);
        //设置日志加密
        MobclickAgent.enableEncrypt(true);//6.0.0版本及以后
        //报告信息
        //MobclickAgent.reportError(this, DeviceUtil.getAllDeviceInfo());
        /* -------------------- 友盟统计配置 -------------------- */
    }

    private void umengPush() {
    /* -------------------- 友盟推送配置 -------------------- */
        PushAgent mPushAgent = PushAgent.getInstance(this);
        //注册推送服务，每次调用register方法都会回调该接口
        mPushAgent.register(new IUmengRegisterCallback() {

            @Override
            public void onSuccess(String deviceToken) {
                //注册成功会返回device token
                mDeviceToken = deviceToken;
            }

            @Override
            public void onFailure(String s, String s1) {

            }
        });
        //调试日志默认开启,如果对外正式发布,建议设置关闭日志输出
//        mPushAgent.setDebugMode(false);

        //自定义消息
        UmengMessageHandler messageHandler = new UmengMessageHandler() {
            @Override
            public void dealWithCustomMessage(final Context context, final UMessage msg) {
                new Handler(getMainLooper()).post(new Runnable() {

                    @Override
                    public void run() {
                        // 对于自定义消息，PushSDK默认只统计送达。若开发者需要统计点击和忽略，则需手动调用统计方法。
                        boolean isClickOrDismissed = true;
                        if (isClickOrDismissed) {
                            //统计自定义消息的打开
                            Log.e("YAO", "App.java - run() ---------- 自定义消息的打开" );
                            UTrack.getInstance(getApplicationContext()).trackMsgClick(msg);
                        } else {
                            //统计自定义消息的忽略
                            Log.e("YAO", "App.java - run() ---------- 自定义消息的忽略" );
                            UTrack.getInstance(getApplicationContext()).trackMsgDismissed(msg);
                        }
                    }
                });
            }

            //自定义通知样式 在通知展示到通知栏时回调
            @Override
            public Notification getNotification(Context context, UMessage msg) {
                for (Map.Entry<String, String> entry : msg.extra.entrySet()) {
                    Log.e("YAO", "App.java - getNotification() ---------- key:value " + entry.getKey() + ":" + entry.getValue() );
                }
                switch (msg.builder_id) {
                    //自定义通知样式编号
                    case 1:
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
                        RemoteViews myNotificationView = new RemoteViews(context.getPackageName(), R.layout.view_notification);
                        myNotificationView.setTextViewText(R.id.notification_title, msg.title);
                        myNotificationView.setTextViewText(R.id.notification_text, msg.text);
                        myNotificationView.setImageViewBitmap(R.id.notification_large_icon, getLargeIcon(context, msg));
                        myNotificationView.setImageViewResource(R.id.notification_small_icon, getSmallIconId(context, msg));
                        builder.setContent(myNotificationView);
                        builder.setAutoCancel(true);
                        Notification mNotification = builder.build();
                        //由于Android v4包的bug，在2.3及以下系统，Builder创建出来的Notification，并没有设置RemoteView，故需要添加此代码
                        mNotification.contentView = myNotificationView;
                        return mNotification;
                    default:
                        //默认为0，若填写的builder_id并不存在，也使用默认。
                        return super.getNotification(context, msg);
                }
            }
        };
        mPushAgent.setMessageHandler(messageHandler);

        /**
         * 该Handler是在BroadcastReceiver中被调用，故
         * 如果需启动Activity，需添加Intent.FLAG_ACTIVITY_NEW_TASK
         * 参考集成文档的1.6.2
         * [url=http://dev.umeng.com/push/android/integration#1_6_2]http://dev.umeng.com/push/android/integration#1_6_2[/url]
         * */
        //自定义行为 只有自定义行为才能进来
        UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler() {

            //通知被点击时回调
            @Override
            public void dealWithCustomAction(Context context, UMessage msg) {
                Log.e("YAO", "App.java - dealWithCustomAction() ---------- msg.custom" + msg.custom );
                for (Map.Entry<String, String> entry : msg.extra.entrySet()) {
                    Log.e("YAO", "App.java - dealWithCustomAction() ---------- key:value " + entry.getKey() + ":" + entry.getValue() );
                }
//                Intent intent = new Intent(context, MainActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
            }
        };
        mPushAgent.setNotificationClickHandler(notificationClickHandler);

    }


}
