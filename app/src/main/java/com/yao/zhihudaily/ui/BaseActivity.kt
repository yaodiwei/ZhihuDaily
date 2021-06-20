package com.yao.zhihudaily.ui

import android.os.Bundle

import com.yao.zhihudaily.tool.EventCenter

import androidx.appcompat.app.AppCompatActivity


/**
 * @author Yao
 * @date 2016/10/1
 */
abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventCenter.register(this)
    }

    public override fun onResume() {
        super.onResume()
    }

    public override fun onPause() {
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        EventCenter.unregister(this)
    }
}
