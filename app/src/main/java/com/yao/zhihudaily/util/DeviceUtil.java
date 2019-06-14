package com.yao.zhihudaily.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import java.lang.reflect.Method;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * @author Yao
 * @date 2016/10/3
 */
public class DeviceUtil {

    private static final String TAG = "DeviceUtil";

    private static String MAC_ADDRESS = null;
    private static String DEVICE_ID = null;

    public static int SCREEN_WIDTH = 0;
    public static int SCREEN_HEIGHT = 0;
    public static float DENSITY = 0;
    public static int STATUS_BAR_HEIGHT = 0;

    @SuppressLint("StaticFieldLeak")
    private static Context sContext;

    public static void init(Context ctx) {
        sContext = ctx;
        initScreenSize(ctx);
    }

    public static void initScreenSize(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);

        int screenWidth = dm.widthPixels;
        int screenHeight = dm.heightPixels;
        DENSITY = dm.density;
        if (screenWidth > 0) {
            if (screenWidth > screenHeight) {
                SCREEN_WIDTH = screenWidth;
                SCREEN_HEIGHT = screenHeight;
            } else {
                SCREEN_WIDTH = screenHeight;
                SCREEN_HEIGHT = screenWidth;
            }
        }

        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            STATUS_BAR_HEIGHT = context.getResources().getDimensionPixelSize(resourceId);
            //如果取不到就取个保底值25dp
            if (STATUS_BAR_HEIGHT == 0) {
                STATUS_BAR_HEIGHT = (int) (DENSITY * 25);
            }
        }
    }

    public static String getAllDeviceInfo() {
        String deviceInfo = "Brand:" + android.os.Build.BRAND
                + " MODEL:" + android.os.Build.MODEL
                + " SDK:" + android.os.Build.VERSION.RELEASE
                + "(" + android.os.Build.VERSION.SDK_INT + ")";
        Log.i(TAG, "DeviceUtil.java - getAllDeviceInfo() ---------- " + deviceInfo);
        return deviceInfo;
    }

    @SuppressLint("HardwareIds")
    public static String getMacAddress() {
        if (!TextUtils.isEmpty(MAC_ADDRESS)) {
            return MAC_ADDRESS;
        }
        WifiManager wifi = (WifiManager) sContext.getSystemService(Context.WIFI_SERVICE);
        if (wifi == null) {
            return MAC_ADDRESS;
        }
        WifiInfo info = wifi.getConnectionInfo();
        if (info == null) {
            return MAC_ADDRESS;
        }
        MAC_ADDRESS = info.getMacAddress();
        if ("02:00:00:00:00:00".equals(MAC_ADDRESS)) {
            try {
                Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
                while (interfaces.hasMoreElements()) {
                    NetworkInterface iter = interfaces.nextElement();
                    if ("wlan0".equals(iter.getName())) {
                        byte[] addr = iter.getHardwareAddress();
                        if (addr == null || addr.length == 0) {
                            continue;
                        }
                        StringBuilder buf = new StringBuilder();
                        for (byte b : addr) {
                            buf.append(String.format("%02X:", b));
                        }
                        if (buf.length() > 0) {
                            buf.deleteCharAt(buf.length() - 1);
                        }
                        MAC_ADDRESS = buf.toString();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return MAC_ADDRESS;
    }

    public static String getDeviceId() {
        if (DEVICE_ID == null) {
            DEVICE_ID = getMacAddress().replace(":", "");
        }
        return DEVICE_ID;
    }

    @SuppressLint("HardwareIds")
    public static String getSerialNumber() {
        String serialNumber = android.os.Build.SERIAL;
        if (!TextUtils.isEmpty(serialNumber)) {
            return serialNumber;
        }
        try {
            @SuppressLint("PrivateApi")
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class);
            serialNumber = (String) get.invoke(c, "ro.serialno");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return serialNumber;

    }

}
