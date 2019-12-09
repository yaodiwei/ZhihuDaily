package com.yao.zhihudaily.util


import android.annotation.SuppressLint
import android.content.Context

/**
 * @author Yao
 * @date 2016/8/17 0012
 */
object ResUtil {

    @SuppressLint("StaticFieldLeak")
    private lateinit var sContext: Context

    fun init(ctx: Context) {
        sContext = ctx
    }

    fun getString(resId: Int): String {
        return sContext.resources.getString(resId)
    }

    fun getString(format: Int, resId: Int): String {
        return String.format(getString(format), getString(resId))
    }

    fun getString(format: Int, vararg str: String): String {
        return String.format(getString(format), *str as Array<Any>)
    }

    fun getString(format: String, vararg str: String): String {
        return String.format(format, *str as Array<Any>)
    }

    fun getString(format: String, resId: Int): String {
        return String.format(format, getString(resId))
    }

    fun getColor(resId: Int): Int {
        return sContext.resources.getColor(resId)
    }
}
