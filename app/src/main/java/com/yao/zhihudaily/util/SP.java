package com.yao.zhihudaily.util;

import android.content.Context;

import com.yao.zhihudaily.App;

/**
 * Created by Administrator on 2016/9/30.
 */

public class SP {

    public static final String PREFERENCES_FILE = "share_data";


    public static boolean putString(String key, String value) {
        return App.app.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE).edit().putString(key, value).commit();
    }

    public boolean putBoolean(String key, boolean value) {
        return App.app.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE).edit().putBoolean(key, value).commit();
    }

    public boolean putFloat(String key, float value) {
        return App.app.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE).edit().putFloat(key, value).commit();
    }

    public boolean putInt(String key, int value) {
        return App.app.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE).edit().putInt(key, value).commit();
    }

    public boolean putLong(String key, long value) {
        return App.app.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE).edit().putLong(key, value).commit();
    }


    public String getString(String key, String defValue) {
        return App.app.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE).getString(key, defValue);
    }

    public boolean getBoolean(String key, boolean defValue) {
        return App.app.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE).getBoolean(key, defValue);
    }

    public float getFloat(String key, float defValue) {
        return App.app.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE).getFloat(key, defValue);
    }

    public int getInt(String key, int defValue) {
        return App.app.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE).getInt(key, defValue);
    }

    public long getLong(String key, long defValue) {
        return App.app.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE).getLong(key, defValue);
    }

    /**
     * 判断SP是否包含特定key的数据
     *
     * @param key
     * @return
     */
    public boolean contains(String key) {
        return App.app.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE).contains(key);
    }

    /**
     * 清空SP里所有数据
     */
    public boolean clear() {
        return App.app.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE).edit().clear().commit();
    }

    /**
     * 删除SP里指定key对应的数据项
     *
     * @param key
     */
    public boolean remove(String key) {
        return App.app.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE).edit().remove(key).commit();
    }
}
