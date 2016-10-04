package com.yao.zhihudaily.util;

import android.util.Log;

/**
 * Created by Administrator on 2016/10/3.
 */

public class DeviceUtil {
    
    public static String getAllDeviceInfo() {
        String deviceInfo = "Brand:" + android.os.Build.BRAND + "    MODEL:" + android.os.Build.MODEL + "    SDK:" + android.os.Build.VERSION.RELEASE + "(" + android.os.Build.VERSION.SDK_INT + ")";
        Log.i("YAO", "DeviceUtil.java - getAllDeviceInfo() ---------- " + deviceInfo );
        return deviceInfo;
    }
}
