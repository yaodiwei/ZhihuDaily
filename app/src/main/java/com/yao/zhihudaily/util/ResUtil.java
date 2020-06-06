package com.yao.zhihudaily.util;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;

/**
 * @author Yao
 * @date 2016/8/17 0012
 */
public class ResUtil {

    @SuppressLint("StaticFieldLeak")
    private static Context sContext;

    public static void init(Context ctx) {
        sContext = ctx;
    }

    public static Resources getResources() {
        return sContext.getResources();
    }

    public static String getString(int resId) {
        return sContext.getResources().getString(resId);
    }

    public static String getString(int format, int resId) {
        return String.format(getString(format), getString(resId));
    }

    public static String getString(int format, String... str) {
        return String.format(getString(format), (Object[]) str);
    }

    public static String getString(String format, String... str) {
        return String.format(format, (Object[]) str);
    }

    public static String getString(String format, int resId) {
        return String.format(format, getString(resId));
    }

    public static int getColor(int resId) {
        return sContext.getResources().getColor(resId);
    }
}
