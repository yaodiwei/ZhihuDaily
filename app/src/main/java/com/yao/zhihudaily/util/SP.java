package com.yao.zhihudaily.util;

import android.content.Context;

import com.yao.zhihudaily.App;

/**
 * @author Yao
 * @date 2016/9/30
 */
public class SP {

    public static final String PREFERENCES_FILE = "share_data";

    public static final String SPLASH = "splash";


    public static void put(String key, String value) {
        App.app.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE).edit().putString(key, value).apply();
    }

    public static void put(String key, boolean value) {
        App.app.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE).edit().putBoolean(key, value).apply();
    }

    public static void put(String key, float value) {
        App.app.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE).edit().putFloat(key, value).apply();
    }

    public static void put(String key, int value) {
        App.app.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE).edit().putInt(key, value).apply();
    }

    public static void put(String key, long value) {
        App.app.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE).edit().putLong(key, value).apply();
    }


    public static String getString(String key, String defValue) {
        return App.app.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE).getString(key, defValue);
    }

    public static boolean getBoolean(String key, boolean defValue) {
        return App.app.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE).getBoolean(key, defValue);
    }

    public static float getFloat(String key, float defValue) {
        return App.app.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE).getFloat(key, defValue);
    }

    public static int getInt(String key, int defValue) {
        return App.app.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE).getInt(key, defValue);
    }

    public static long getLong(String key, long defValue) {
        return App.app.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE).getLong(key, defValue);
    }

    public static boolean contains(String key) {
        return App.app.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE).contains(key);
    }

    public static boolean remove(String key) {
        return App.app.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE).edit().remove(key).commit();
    }

    public static boolean clear() {
        return App.app.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE).edit().clear().commit();
    }
}
