package com.yao.zhihudaily.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

/**
 * @author Yao
 * @date 2016/8/17
 */
public class T {

    @SuppressLint("StaticFieldLeak")
    private static Context sContext;

    public static void init(Context ctx){
        sContext = ctx;
    }

    public static void t(String text) {
        Toast.makeText(sContext, text, Toast.LENGTH_SHORT).show();
    }

    public static void t(Activity aty, final String text) {
        aty.runOnUiThread(() -> Toast.makeText(sContext, text, Toast.LENGTH_SHORT).show());
    }

    public static void t(int resId) {
        Toast.makeText(sContext, resId, Toast.LENGTH_SHORT).show();
    }

    public static void t(Activity aty, final int resId) {
        aty.runOnUiThread(() -> Toast.makeText(sContext, resId, Toast.LENGTH_SHORT).show());
    }

    public static void s(View v, String text) {
        final Snackbar snackbar = Snackbar.make(v, text, Snackbar.LENGTH_SHORT);
        snackbar.setAction("关闭", view -> snackbar.dismiss());
        snackbar.show();
    }

    public static void s(final View v, Activity aty, final String text) {
        aty.runOnUiThread(() -> {
            final Snackbar snackbar = Snackbar.make(v, text, Snackbar.LENGTH_SHORT);
            snackbar.setAction("关闭", view -> snackbar.dismiss());
            snackbar.show();
        });
    }

    public static void s(View v, int resId) {
        final Snackbar snackbar = Snackbar.make(v, resId, Snackbar.LENGTH_SHORT);
        snackbar.setAction("关闭", view -> snackbar.dismiss());
        snackbar.show();
    }

    public static void s(final View v, Activity aty, final int resId) {
        aty.runOnUiThread(() -> {
            final Snackbar snackbar = Snackbar.make(v, resId, Snackbar.LENGTH_SHORT);
            snackbar.setAction("关闭", view -> snackbar.dismiss());
            snackbar.show();
        });
    }
}

