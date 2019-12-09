package com.yao.zhihudaily.tool

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Context
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.yao.zhihudaily.R


/**
 *
 * @author Yao
 * @date 2016/9/28
 */
class StateTool {

    private var sViewGroupRoot: ViewGroup? = null
    private var mContext: Context? = null
    private var mContentView: View
    private lateinit var mEmptyView: RelativeLayout
    private lateinit var mErrorView: RelativeLayout
    private lateinit var mProgressView: RelativeLayout
    private lateinit var mCurrentView: View

    private var mParamsChildrenWrapContent: LinearLayout.LayoutParams? = null
    private var mParamsChildrenImage: LinearLayout.LayoutParams? = null
    private var mParamsChildrenMarginBottom50: LinearLayout.LayoutParams? = null

    /**
     * 如果有多个孩子,调用此方法
     * @param root ViewGroup sViewGroupRoot
     */
    constructor(root: ViewGroup) {
        this.sViewGroupRoot = root
        if (root.childCount > 1) {
            throw RuntimeException("sViewGroupRoot view's children more than 1")
        }
        mContentView = root.getChildAt(0)

        init()
    }

    /**
     * 如果有多个孩子,调用此方法
     * @param root ViewGroup sViewGroupRoot
     * @param index 传孩子的位置
     */
    constructor(root: ViewGroup, index: Int) {
        this.sViewGroupRoot = root
        if (root.childCount < index + 1) {
            throw RuntimeException("Invalid index " + index + ", size is " + root.childCount)
        }
        mContentView = root.getChildAt(index)

        init()
    }

    private fun init() {
        mContext = sViewGroupRoot!!.context
        mCurrentView = mContentView

        mParamsChildrenWrapContent = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        mParamsChildrenImage = LinearLayout.LayoutParams(sImageSidesLength, sImageSidesLength)
        mParamsChildrenMarginBottom50 = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        mParamsChildrenMarginBottom50!!.setMargins(0, 0, 0, sOffset)//不margin不居中

        initEmptyView()
        initErrorView()
        initProgressView()
    }

    fun setEmptyAndErrorImageResId(emptyImageResId: Int, errorImageResId: Int) {
        sEmptyImageResId = emptyImageResId
        sErrorImageResId = errorImageResId
    }

    fun setEmptyAndErrorTextResId(emptyText: String, errorText: String, reloadText: String, loadingText: String) {
        sEmptyText = emptyText
        sErrorText = errorText
        sReloadText = reloadText
        sLoadingText = loadingText
    }

    fun showEmptyView() {
        if (sUseAlphaAnimator) {
            alphaHide(mCurrentView)
            alphaShow(mEmptyView)
        } else {
            mCurrentView.visibility = View.GONE
            mEmptyView.visibility = View.VISIBLE
        }
        mCurrentView = mEmptyView
    }

    fun showErrorView() {
        mCurrentView.visibility = View.GONE
        mErrorView.visibility = View.VISIBLE
        mCurrentView = mErrorView
    }

    fun showProgressView() {
        mCurrentView.visibility = View.GONE
        mProgressView.visibility = View.VISIBLE
        mCurrentView = mProgressView
    }

    fun showContentView() {
        mCurrentView.visibility = View.GONE
        mContentView.visibility = View.VISIBLE
        mCurrentView = mContentView
    }

    fun setOnClickListener(onClickListener: View.OnClickListener) {
        mEmptyView.setOnClickListener(onClickListener)
        mErrorView.setOnClickListener(onClickListener)
    }

    private fun alphaShow(v: View) {
        val oa = ObjectAnimator.ofFloat(v, View.ALPHA, 0f, 1f)
        oa.setDuration(500)
        oa.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animator: Animator) {
                v.visibility = View.VISIBLE
            }
        })
        oa.start()
    }

    private fun alphaHide(v: View) {
        val oa = ObjectAnimator.ofFloat(v, View.ALPHA, 1f, 0f)
        oa.setDuration(500)
        oa.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animator: Animator) {
                v.visibility = View.GONE
            }
        })
        oa.start()
    }

    private fun initEmptyView() {
        mEmptyView = RelativeLayout(mContext)

        val linearLayout = LinearLayout(mContext)
        linearLayout.orientation = LinearLayout.VERTICAL

        val iv = ImageView(mContext)
        iv.setImageResource(sEmptyImageResId)
        linearLayout.addView(iv, mParamsChildrenImage)

        val tvContent = TextView(mContext)
        tvContent.setTextSize(TypedValue.COMPLEX_UNIT_SP, CONTENT_TEXT_SIZE.toFloat())
        tvContent.text = sEmptyText
        linearLayout.addView(tvContent, mParamsChildrenWrapContent)

        val tvTip = TextView(mContext)
        tvTip.setTextSize(TypedValue.COMPLEX_UNIT_SP, TIP_TEXT_SIZE.toFloat())
        tvTip.text = sReloadText
        linearLayout.addView(tvTip, mParamsChildrenMarginBottom50)

        val paramsLinearLayout = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        paramsLinearLayout.addRule(RelativeLayout.CENTER_IN_PARENT)//这个是RelativeLayout的layout_centerInParent属性
        linearLayout.gravity = Gravity.CENTER//这个是LinearLayout的gravity属性
        mEmptyView.addView(linearLayout, paramsLinearLayout)

        sViewGroupRoot!!.addView(mEmptyView, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))

        mEmptyView.visibility = View.GONE
    }

    private fun initErrorView() {
        mErrorView = RelativeLayout(mContext)

        val linearLayout = LinearLayout(mContext)
        linearLayout.orientation = LinearLayout.VERTICAL

        val iv = ImageView(mContext)
        iv.setImageResource(sErrorImageResId)
        linearLayout.addView(iv, mParamsChildrenImage)

        val tvContent = TextView(mContext)
        tvContent.setTextSize(TypedValue.COMPLEX_UNIT_SP, CONTENT_TEXT_SIZE.toFloat())
        tvContent.text = sErrorText
        linearLayout.addView(tvContent, mParamsChildrenWrapContent)

        val tvTip = TextView(mContext)
        tvTip.setTextSize(TypedValue.COMPLEX_UNIT_SP, TIP_TEXT_SIZE.toFloat())
        tvTip.text = sReloadText
        linearLayout.addView(tvTip, mParamsChildrenMarginBottom50)

        val paramsLinearLayout = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        paramsLinearLayout.addRule(RelativeLayout.CENTER_IN_PARENT)//这个是RelativeLayout的layout_centerInParent属性
        linearLayout.gravity = Gravity.CENTER//这个是LinearLayout的gravity属性
        mErrorView.addView(linearLayout, paramsLinearLayout)

        sViewGroupRoot!!.addView(mErrorView, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))

        mErrorView.visibility = View.GONE
    }

    private fun initProgressView() {
        mProgressView = RelativeLayout(mContext)

        val linearLayout = LinearLayout(mContext)
        linearLayout.orientation = LinearLayout.VERTICAL

        val pb = ProgressBar(mContext)
        linearLayout.addView(pb, mParamsChildrenWrapContent)

        val tvContent = TextView(mContext)
        tvContent.setTextSize(TypedValue.COMPLEX_UNIT_SP, CONTENT_TEXT_SIZE.toFloat())
        tvContent.text = sLoadingText
        linearLayout.addView(tvContent, mParamsChildrenMarginBottom50)

        val paramsLinearLayout = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        paramsLinearLayout.addRule(RelativeLayout.CENTER_IN_PARENT)//这个是RelativeLayout的layout_centerInParent属性
        linearLayout.gravity = Gravity.CENTER//这个是LinearLayout的gravity属性
        mProgressView.addView(linearLayout, paramsLinearLayout)

        sViewGroupRoot!!.addView(mProgressView, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
    }

    companion object {

        /**
         * 一大堆静态变量,根据项目需要可以随意修改.比如项目全部用同一张错误页面的图片.
         * 在一个项目中如果用好几张不同的错误页面图片,可以考虑改成成员变量.
         */
        //内容字体的大小,单位SP
        private val CONTENT_TEXT_SIZE = 20
        //提示字体的大小,单位SP
        private val TIP_TEXT_SIZE = 12

        //空页面图片,默认用的安卓sdk里面的图片,严重建议替换成一个256px左右的图片 默认使用android.R.drawable.ic_menu_close_clear_cancel
        private var sEmptyImageResId = R.mipmap.empty
        //错误页面图片,默认用的安卓sdk里面的图片,严重建议替换成一个256px左右的图片 默认使用android.R.drawable.ic_menu_search
        private var sErrorImageResId = R.mipmap.error

        //空页面文字
        private var sEmptyText = "空页面"
        //错误页面文字
        private var sErrorText = "错误页面"
        //加载页面文字
        private var sLoadingText = "加载中..."
        //重载动作的文字提示
        private var sReloadText = "点击重载"

        //图片的宽和高 可以用ViewGroup.LayoutParams.WRAP_CONTENT
        private val sImageSidesLength = 128
        //等待,错误,空页面提示的向上偏移
        private val sOffset = 0

        //使用淡入淡出动画
        private val sUseAlphaAnimator = false
    }

}