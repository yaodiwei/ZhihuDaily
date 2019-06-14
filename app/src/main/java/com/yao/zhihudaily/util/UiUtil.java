package com.yao.zhihudaily.util;


import android.annotation.SuppressLint;
import android.content.Context;

/**
 * @author Yao
 * @date 2016/8/16 0016
 */
public class UiUtil {

    @SuppressLint("StaticFieldLeak")
    private static Context sContext;

    public static void init(Context ctx){
        sContext = ctx;
    }

    /**
     * dp转换px
     */
    public static int dp2px(int dp) {
        final float scale = sContext.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    /**
     * px转换dp
     */
    public static int px2dp(int px) {
        final float scale = sContext.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

}
