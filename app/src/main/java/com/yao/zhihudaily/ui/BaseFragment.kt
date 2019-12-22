package com.yao.zhihudaily.ui

import android.util.Log
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.umeng.analytics.MobclickAgent
import com.yao.zhihudaily.R
import java.util.*

/**
 * @author Yao
 * @date 2016/9/4
 */
abstract class BaseFragment : Fragment() {

    private val mFragmentContainer: FrameLayout? = null

    val fragmentActivity: FragmentActivity
        get() = Objects.requireNonNull<FragmentActivity>(activity)

    override fun onPause() {
        super.onPause()
        MobclickAgent.onPageEnd(this.javaClass.simpleName)
        Log.i(TAG, "BaseFragment.java - onPause() ---------- Fragment:" + this.javaClass.name)
    }

    override fun onResume() {
        super.onResume()
        MobclickAgent.onPageStart(this.javaClass.simpleName)
        Log.i(TAG, "BaseFragment.java - onResume() ---------- Fragment:" + this.javaClass.name)
    }

    /**
     * Refresh
     * 如果是当前页面=点击页面, 则平滑移动到头部
     */
    open fun refresh() {
    }

    /**
     * Called when a fragment will be displayed
     * 将会显示的淡入动画
     */
    fun willBeDisplayed() {
        // Do what you want here, for example animate the content
        if (mFragmentContainer != null) {
            val fadeIn = AnimationUtils.loadAnimation(activity, R.anim.fade_in)
            mFragmentContainer.startAnimation(fadeIn)
        }
    }

    /**
     * Called when a fragment will be hidden
     * 将会隐藏的淡出动画
     */
    fun willBeHidden() {
        if (mFragmentContainer != null) {
            val fadeOut = AnimationUtils.loadAnimation(activity, R.anim.fade_out)
            mFragmentContainer.startAnimation(fadeOut)
        }
    }

    companion object {

        private val TAG = "BaseFragment"
    }

}
