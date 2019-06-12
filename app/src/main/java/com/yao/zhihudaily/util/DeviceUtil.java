package com.yao.zhihudaily.util;

import android.util.Log;

/**
 * @author Yao
 * @date 2016/10/3
 */
public class DeviceUtil {

    private static final String TAG = "DeviceUtil";

    public static String getAllDeviceInfo() {
        String deviceInfo = "Brand:" + android.os.Build.BRAND
                + " MODEL:" + android.os.Build.MODEL
                + " SDK:" + android.os.Build.VERSION.RELEASE
                + "(" + android.os.Build.VERSION.SDK_INT + ")";
        Log.i(TAG, "DeviceUtil.java - getAllDeviceInfo() ---------- " + deviceInfo);
        return deviceInfo;
    }
}
