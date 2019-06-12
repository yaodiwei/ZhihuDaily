package com.yao.zhihudaily.util;

import android.app.Activity;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
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

    public static void s(View v, String text) {
        final Snackbar snackbar = Snackbar.make(v, text, Snackbar.LENGTH_SHORT);
        snackbar.setAction("关闭", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snackbar.dismiss();
            }
        });
        snackbar.show();
    }

    public static void s(final View v, Activity aty, final String text) {
        aty.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final Snackbar snackbar = Snackbar.make(v, text, Snackbar.LENGTH_SHORT);
                snackbar.setAction("关闭", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        snackbar.dismiss();
                    }
                });
                snackbar.show();
            }
        });
    }

    public static void s(View v, int resId) {
        final Snackbar snackbar = Snackbar.make(v, resId, Snackbar.LENGTH_SHORT);
        snackbar.setAction("关闭", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snackbar.dismiss();
            }
        });
        snackbar.show();
    }

    public static void s(final View v, Activity aty, final int resId) {
        aty.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final Snackbar snackbar = Snackbar.make(v, resId, Snackbar.LENGTH_SHORT);
                snackbar.setAction("关闭", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        snackbar.dismiss();
                    }
                });
                snackbar.show();
            }
        });
    }
}

