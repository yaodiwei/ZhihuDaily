package com.yao.zhihudaily.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * @author Yao
 * @date 2019/6/14
 */
public class AppUtil {

    @SuppressLint("StaticFieldLeak")
    public static Context sContext;

    private static int APP_VERSION_CODE = 0;
    private static String APP_VERSION_NAME = null;

    public static void init(Context context) {
        sContext = context;
        T.init(context);
        SP.init(context);
        DeviceUtil.init(context);
        ResUtil.init(context);
        UiUtil.init(context);
        FileUtil.init(context);
    }

    public static int getAppVersionCode() {
        if (APP_VERSION_CODE <= 0) {
            try {
                PackageManager pm = sContext.getPackageManager();
                PackageInfo pi = pm.getPackageInfo(sContext.getPackageName(), 0);
                APP_VERSION_CODE = pi.versionCode;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return APP_VERSION_CODE;
    }

    public static String getAppVersionName() {
        if (APP_VERSION_NAME == null) {
            try {
                PackageManager pm = sContext.getPackageManager();
                PackageInfo pi = pm.getPackageInfo(sContext.getPackageName(), 0);
                APP_VERSION_NAME = pi.versionName;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return APP_VERSION_NAME;
    }

    public static void hideSoftInput(Activity aty) {
        View currentFocus = aty.getCurrentFocus();
        if (currentFocus != null) {
            InputMethodManager imm = (InputMethodManager) sContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
            }
        }
    }

    public static void hideSoftInput(View view) {
        InputMethodManager imm = (InputMethodManager) sContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
        }
    }

    public static void showSoftInput() {
        InputMethodManager imm = (InputMethodManager) sContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
        }
    }

}
