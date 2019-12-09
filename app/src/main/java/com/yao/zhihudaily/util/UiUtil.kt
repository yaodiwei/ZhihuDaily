package com.yao.zhihudaily.util


import android.annotation.SuppressLint
import android.content.Context

/**
 * @author Yao
 * @date 2016/8/16 0016
 */
object UiUtil {

    @SuppressLint("StaticFieldLeak")
    private lateinit var sContext: Context

    fun init(ctx: Context) {
        sContext = ctx
    }

    /**
     * dp转换px
     */
    fun dp2px(dp: Int): Int {
        val scale = sContext.resources.displayMetrics.density
        return (dp * scale + 0.5f).toInt()
    }

    /**
     * px转换dp
     */
    fun px2dp(px: Int): Int {
        val scale = sContext.resources.displayMetrics.density
        return (px / scale + 0.5f).toInt()
    }

}
