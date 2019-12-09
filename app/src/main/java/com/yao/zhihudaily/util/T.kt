package com.yao.zhihudaily.util

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.Toast

import com.google.android.material.snackbar.Snackbar

/**
 * @author Yao
 * @date 2016/8/17
 */
object T {

    @SuppressLint("StaticFieldLeak")
    private lateinit var sContext: Context

    fun init(ctx: Context) {
        sContext = ctx
    }

    fun t(text: String) {
        Toast.makeText(sContext, text, Toast.LENGTH_SHORT).show()
    }

    fun t(aty: Activity, text: String) {
        aty.runOnUiThread { Toast.makeText(sContext, text, Toast.LENGTH_SHORT).show() }
    }

    fun t(resId: Int) {
        Toast.makeText(sContext, resId, Toast.LENGTH_SHORT).show()
    }

    fun t(aty: Activity, resId: Int) {
        aty.runOnUiThread { Toast.makeText(sContext, resId, Toast.LENGTH_SHORT).show() }
    }

    fun s(v: View, text: String) {
        val snackbar = Snackbar.make(v, text, Snackbar.LENGTH_SHORT)
        snackbar.setAction("关闭") { view -> snackbar.dismiss() }
        snackbar.show()
    }

    fun s(v: View, aty: Activity, text: String) {
        aty.runOnUiThread {
            val snackbar = Snackbar.make(v, text, Snackbar.LENGTH_SHORT)
            snackbar.setAction("关闭") { view -> snackbar.dismiss() }
            snackbar.show()
        }
    }

    fun s(v: View, resId: Int) {
        val snackbar = Snackbar.make(v, resId, Snackbar.LENGTH_SHORT)
        snackbar.setAction("关闭") { view -> snackbar.dismiss() }
        snackbar.show()
    }

    fun s(v: View, aty: Activity, resId: Int) {
        aty.runOnUiThread {
            val snackbar = Snackbar.make(v, resId, Snackbar.LENGTH_SHORT)
            snackbar.setAction("关闭") { view -> snackbar.dismiss() }
            snackbar.show()
        }
    }
}

