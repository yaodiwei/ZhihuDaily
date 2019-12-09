package com.yao.zhihudaily.ui

import android.os.Bundle

import com.umeng.analytics.MobclickAgent
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
        MobclickAgent.onResume(this)
    }

    public override fun onPause() {
        super.onPause()
        MobclickAgent.onPause(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventCenter.unregister(this)
    }
}
