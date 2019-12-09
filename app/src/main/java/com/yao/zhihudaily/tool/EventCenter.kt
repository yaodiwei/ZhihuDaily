package com.yao.zhihudaily.tool

import android.util.Log

import org.greenrobot.eventbus.EventBus

/**
 * @author Yao
 * @date 2019/9/14
 */
object EventCenter {

    private val TAG = "EventCenter"

    fun register(subscriber: Any) {
        if (!EventBus.getDefault().isRegistered(subscriber)) {
            try {
                EventBus.getDefault().register(subscriber)

            } catch (e: Exception) {
                Log.d(TAG, "Current page has not event: [" + subscriber.javaClass.name + "]")
            }

        }
    }

    fun unregister(subscriber: Any) {
        if (EventBus.getDefault().isRegistered(subscriber)) {
            try {
                EventBus.getDefault().unregister(subscriber)
            } catch (e: Exception) {
                Log.d(TAG, "Current page has not event: [" + subscriber.javaClass.name + "]")
            }

        }
    }

    fun post(event: Any) {
        EventBus.getDefault().post(event)
    }
}
