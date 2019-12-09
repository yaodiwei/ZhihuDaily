package com.yao.zhihudaily.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.appcompat.widget.SwitchCompat
import com.yao.zhihudaily.R


/**
 *
 * @author Yao
 * @date 2016/10/1
 */

class SettingSwitchCompat : RelativeLayout {

    private var mTvTitle: TextView? = null
    private var mTvDesc: TextView? = null
    private var mSwitchCompat: SwitchCompat? = null

    private lateinit var mTitle: CharSequence
    private var mDescOn: CharSequence? = null
    private var mDescOff: CharSequence? = null

    /**
     * 获取当前开关状态
     *
     * @return 返回开关的值
     */
    /**
     * 设置当前开关状态
     *
     * @param check true表示开，false表示关
     */
    var isChecked: Boolean
        get() = mSwitchCompat!!.isChecked
        set(check) {
            mSwitchCompat!!.isChecked = check
            if (check) {
                mTvDesc!!.text = mDescOn
            } else {
                mTvDesc!!.text = mDescOff
            }
        }

    constructor(context: Context) : super(context) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {

        //获取自定义控件中xml中的属性
        mTitle = attrs.getAttributeValue(NAMESPACE, "setting_title")
        mDescOn = attrs.getAttributeValue(NAMESPACE, "desc_on")
        mDescOff = attrs.getAttributeValue(NAMESPACE, "desc_off")

        initView()
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        initView()
    }

    private fun initView() {
        //inflate出自定义的布局
        View.inflate(context, R.layout.view_setting_switchcompat, this)
        mTvTitle = findViewById(R.id.tv_title)
        mTvDesc = findViewById(R.id.tv_desc)
        mSwitchCompat = findViewById(R.id.switch_compat)

        setTitle(mTitle)
        isChecked = false
    }

    /**
     * 设置控件标题
     *
     * @param title 标题字符串
     */
    fun setTitle(title: CharSequence) {
        mTvTitle!!.text = title
    }

    /**
     * 设置控件标题
     *
     * @param resid 字符资源id
     */
    fun setTitle(@StringRes resid: Int) {
        mTvTitle!!.text = mTitle
    }

    /**
     * 设置控件开关时候的描述
     *
     * @param descOn 开启时的描述
     * @param descOff 关闭时的描述
     */
    fun setDesc(descOn: CharSequence, descOff: CharSequence) {
        this.mDescOn = descOn
        this.mDescOff = descOff
    }

    /**
     * 设置控件开关时候的描述
     *
     * @param descOnResId 开启时的描述
     * @param descOffResId 关闭时的描述
     */
    fun setDesc(@StringRes descOnResId: Int, @StringRes descOffResId: Int) {
        setDesc(context.resources.getText(descOnResId), context.resources.getText(descOffResId))
    }

    override fun performClick(): Boolean {
        isChecked = !isChecked
        return super.performClick()
    }

    companion object {

        private val NAMESPACE = "http://schemas.android.com/apk/res-auto"
    }

}
