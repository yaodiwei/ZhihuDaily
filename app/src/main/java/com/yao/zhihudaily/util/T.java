package com.yao.zhihudaily.util;

import android.widget.Toast;

import com.yao.zhihudaily.App;

/**
 * Created by Administrator on 2016/8/17.
 */
public class T {

    public static void t(String text) {
        Toast.makeText(App.app, text, Toast.LENGTH_SHORT).show();
    }
}
