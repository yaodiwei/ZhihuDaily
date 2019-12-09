package com.yao.zhihudaily.util

import android.annotation.SuppressLint
import android.content.Context

/**
 * @author Yao
 * @date 2016/9/30
 */
object SP {

    val PREFERENCES_FILE = "share_data"

    @SuppressLint("StaticFieldLeak")
    private lateinit var sContext: Context

    interface Key {
        companion object {
            val SPLASH = "splash"
        }
    }

    fun init(ctx: Context) {
        sContext = ctx
    }

    fun put(key: String, value: String) {
        sContext.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE).edit().putString(key, value).apply()
    }

    fun put(key: String, value: Boolean) {
        sContext.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE).edit().putBoolean(key, value).apply()
    }

    fun put(key: String, value: Float) {
        sContext.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE).edit().putFloat(key, value).apply()
    }

    fun put(key: String, value: Int) {
        sContext.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE).edit().putInt(key, value).apply()
    }

    fun put(key: String, value: Long) {
        sContext.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE).edit().putLong(key, value).apply()
    }


    fun getString(key: String, defValue: String): String? {
        return sContext.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE).getString(key, defValue)
    }

    fun getBoolean(key: String, defValue: Boolean): Boolean {
        return sContext.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE).getBoolean(key, defValue)
    }

    fun getFloat(key: String, defValue: Float): Float {
        return sContext.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE).getFloat(key, defValue)
    }

    fun getInt(key: String, defValue: Int): Int {
        return sContext.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE).getInt(key, defValue)
    }

    fun getLong(key: String, defValue: Long): Long {
        return sContext.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE).getLong(key, defValue)
    }

    operator fun contains(key: String): Boolean {
        return sContext.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE).contains(key)
    }

    fun remove(key: String): Boolean {
        return sContext.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE).edit().remove(key).commit()
    }

    fun clear(): Boolean {
        return sContext.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE).edit().clear().commit()
    }
}
