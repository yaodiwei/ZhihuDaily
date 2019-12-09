package com.yao.zhihudaily.util

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.view.View
import android.view.inputmethod.InputMethodManager

/**
 * @author Yao
 * @date 2019/6/14
 */
object AppUtil {

    @SuppressLint("StaticFieldLeak")
    lateinit var sContext: Context

    private var APP_VERSION_CODE = 0
    private lateinit var APP_VERSION_NAME: String

    val appVersionCode: Int
        get() {
            if (APP_VERSION_CODE <= 0) {
                try {
                    val pm = sContext.packageManager
                    val pi = pm.getPackageInfo(sContext.packageName, 0)
                    APP_VERSION_CODE = pi.versionCode
                } catch (e: PackageManager.NameNotFoundException) {
                    e.printStackTrace()
                }

            }
            return APP_VERSION_CODE
        }

    val appVersionName: String?
        get() {
            if (APP_VERSION_NAME == null) {
                try {
                    val pm = sContext.packageManager
                    val pi = pm.getPackageInfo(sContext.packageName, 0)
                    APP_VERSION_NAME = pi.versionName
                } catch (e: PackageManager.NameNotFoundException) {
                    e.printStackTrace()
                }

            }
            return APP_VERSION_NAME
        }

    fun init(context: Context) {
        sContext = context
        T.init(context)
        SP.init(context)
        DeviceUtil.init(context)
        ResUtil.init(context)
        UiUtil.init(context)
        FileUtil.init(context)
    }

    fun hideSoftInput(aty: Activity) {
        val currentFocus = aty.currentFocus
        if (currentFocus != null) {
            val imm = sContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus.windowToken, 0)
        }
    }

    fun hideSoftInput(view: View) {
        val imm = sContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.applicationWindowToken, 0)
    }

    fun showSoftInput() {
        val imm = sContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED)
    }

}
