package com.yao.zhihudaily.util;

import android.app.Activity;
import android.widget.Toast;

import com.yao.zhihudaily.App;

/**
 * Created by Administrator on 2016/8/17.
 */
public class T {

    public static void t(String text) {
        Toast.makeText(App.app, text, Toast.LENGTH_SHORT).show();
    }

    public static void t(Activity aty, final String text) {
        aty.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(App.app, text, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void t(int resId) {
        Toast.makeText(App.app, resId, Toast.LENGTH_SHORT).show();
    }

    public static void t(Activity aty, final int resId) {
        aty.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(App.app, resId, Toast.LENGTH_SHORT).show();
            }
        });
    }
}

