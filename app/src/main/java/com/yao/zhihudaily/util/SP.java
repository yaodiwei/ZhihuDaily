package com.yao.zhihudaily.util;

import android.annotation.SuppressLint;
import android.content.Context;

/**
 * @author Yao
 * @date 2016/9/30
 */
public class SP {

    public static final String PREFERENCES_FILE = "share_data";

    @SuppressLint("StaticFieldLeak")
    private static Context sContext;

    public interface Key {
        String SPLASH = "splash";
    }

    public static void init(Context ctx) {
        sContext = ctx;
    }

    public static void put(String key, String value) {
        sContext.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE).edit().putString(key, value).apply();
    }

    public static void put(String key, boolean value) {
        sContext.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE).edit().putBoolean(key, value).apply();
    }

    public static void put(String key, float value) {
        sContext.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE).edit().putFloat(key, value).apply();
    }

    public static void put(String key, int value) {
        sContext.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE).edit().putInt(key, value).apply();
    }

    public static void put(String key, long value) {
        sContext.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE).edit().putLong(key, value).apply();
    }


    public static String getString(String key, String defValue) {
        return sContext.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE).getString(key, defValue);
    }

    public static boolean getBoolean(String key, boolean defValue) {
        return sContext.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE).getBoolean(key, defValue);
    }

    public static float getFloat(String key, float defValue) {
        return sContext.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE).getFloat(key, defValue);
    }

    public static int getInt(String key, int defValue) {
        return sContext.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE).getInt(key, defValue);
    }

    public static long getLong(String key, long defValue) {
        return sContext.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE).getLong(key, defValue);
    }

    public static boolean contains(String key) {
        return sContext.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE).contains(key);
    }

    public static boolean remove(String key) {
        return sContext.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE).edit().remove(key).commit();
    }

    public static boolean clear() {
        return sContext.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE).edit().clear().commit();
    }
}
