package com.yao.zhihudaily.tool;

import android.util.Log;

import org.greenrobot.eventbus.EventBus;

public class EventCenter {

    private static final String TAG = "EventCenter";

    public static void register(Object subscriber){
        if (!EventBus.getDefault().isRegistered(subscriber)) {
            try {
                EventBus.getDefault().register(subscriber);

            } catch (Exception e) {
                Log.d(TAG, "Current page has not event: [" + subscriber.getClass().getName() + "]");
            }
        }
    }

    public static void unregister(Object subscriber){
        if (EventBus.getDefault().isRegistered(subscriber)) {
            try {
                EventBus.getDefault().unregister(subscriber);
            } catch (Exception e) {
                Log.d(TAG, "Current page has not event: [" + subscriber.getClass().getName() + "]");
            }
        }
    }

    public static void post(Object event){
        EventBus.getDefault().post(event);
    }
}
