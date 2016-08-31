package com.yao.zhihudaily.util;


import com.yao.zhihudaily.App;

/**
 * Created by Administrator on 2016/8/17 0012.
 */
public class ResUtil {

    public static String getString (int resId) {
        return App.app.getResources().getString(resId);
    }

    public static String getString(int format, int resId)
    {
        return String.format(App.app.getResources().getString(format), App.app.getResources().getString(resId));
    }

    public static String getString(int format, String content)
    {
        return String.format(App.app.getResources().getString(format), content);
    }
}
