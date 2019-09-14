package com.yao.zhihudaily.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yao.zhihudaily.R;

import androidx.annotation.StringRes;
import androidx.appcompat.widget.SwitchCompat;


/**
 *
 * @author Administrator
 * @date 2016/10/1
 */

public class SettingSwitchCompat extends RelativeLayout {

    private static final String NAMESPACE = "http://schemas.android.com/apk/res-auto";

    private TextView mTvTitle;
    private TextView mTvDesc;
    private SwitchCompat mSwitchCompat;

    private CharSequence mTitle;
    private CharSequence mDescOn;
    private CharSequence mDescOff;

    public SettingSwitchCompat(Context context) {
        super(context);
        initView();
    }

    public SettingSwitchCompat(Context context, AttributeSet attrs) {
        super(context, attrs);

        //获取自定义控件中xml中的属性
        mTitle = attrs.getAttributeValue(NAMESPACE, "setting_title");
        mDescOn = attrs.getAttributeValue(NAMESPACE, "desc_on");
        mDescOff = attrs.getAttributeValue(NAMESPACE, "desc_off");

        initView();
    }

    public SettingSwitchCompat(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    private void initView() {
        //inflate出自定义的布局
        View.inflate(getContext(), R.layout.view_setting_switchcompat, this);
        mTvTitle = findViewById(R.id.tv_title);
        mTvDesc = findViewById(R.id.tv_desc);
        mSwitchCompat = findViewById(R.id.switch_compat);

        setTitle(mTitle);
        setChecked(false);
    }

    /**
     * 设置控件标题
     *
     * @param title 标题字符串
     */
    public void setTitle(CharSequence title) {
        mTvTitle.setText(title);
    }

    /**
     * 设置控件标题
     *
     * @param resid 字符资源id
     */
    public void setTitle(@StringRes int resid) {
        mTvTitle.setText(mTitle);
    }

    /**
     * 设置控件开关时候的描述
     *
     * @param descOn 开启时的描述
     * @param descOff 关闭时的描述
     */
    public void setDesc(CharSequence descOn, CharSequence descOff) {
        this.mDescOn = descOn;
        this.mDescOff = descOff;
    }

    /**
     * 设置控件开关时候的描述
     *
     * @param descOnResId 开启时的描述
     * @param descOffResId 关闭时的描述
     */
    public void setDesc(@StringRes int descOnResId, @StringRes int descOffResId) {
        setDesc(getContext().getResources().getText(descOnResId), getContext().getResources().getText(descOffResId));
    }

    /**
     * 获取当前开关状态
     *
     * @return 返回开关的值
     */
    public boolean isChecked() {
        return mSwitchCompat.isChecked();
    }

    /**
     * 设置当前开关状态
     *
     * @param check true表示开，false表示关
     */
    public void setChecked(boolean check) {
        mSwitchCompat.setChecked(check);
        if (check) {
            mTvDesc.setText(mDescOn);
        } else {
            mTvDesc.setText(mDescOff);
        }
    }

    @Override
    public boolean performClick() {
        setChecked(!isChecked());
        return super.performClick();
    }

}
