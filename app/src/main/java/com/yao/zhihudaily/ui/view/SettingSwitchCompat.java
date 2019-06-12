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
 * Created by Administrator on 2016/10/1.
 */

public class SettingSwitchCompat extends RelativeLayout {

    private static final String NAMESPACE = "http://schemas.android.com/apk/res-auto";

    private TextView tvTitle;
    private TextView tvDesc;
    private SwitchCompat sc;

    private CharSequence title;
    private CharSequence descOn;
    private CharSequence descOff;

    public SettingSwitchCompat(Context context) {
        super(context);
        initView();
    }

    public SettingSwitchCompat(Context context, AttributeSet attrs) {
        super(context, attrs);

        //获取自定义控件中xml中的属性
        title = attrs.getAttributeValue(NAMESPACE, "setting_title");
        descOn = attrs.getAttributeValue(NAMESPACE, "desc_on");
        descOff = attrs.getAttributeValue(NAMESPACE, "desc_off");

        initView();
    }

    public SettingSwitchCompat(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    private void initView() {
        //inflate出自定义的布局
        View.inflate(getContext(), R.layout.view_setting_switchcompat, this);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvDesc = (TextView) findViewById(R.id.tvDesc);
        sc = (SwitchCompat) findViewById(R.id.sc);

        setTitle(title);
        setChecked(false);
    }

    /**
     * 设置控件标题
     *
     * @param title 标题字符串
     */
    public void setTitle(CharSequence title) {
        tvTitle.setText(title);
    }

    /**
     * 设置控件标题
     *
     * @param resid 字符资源id
     */
    public void setTitle(@StringRes int resid) {
        tvTitle.setText(title);
    }

    /**
     * 设置控件开关时候的描述
     *
     * @param descOn 开启时的描述
     * @param descOff 关闭时的描述
     */
    public void setDesc(CharSequence descOn, CharSequence descOff) {
        this.descOn = descOn;
        this.descOff = descOff;
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
     * @return
     */
    public boolean isChecked() {
        return sc.isChecked();
    }

    /**
     * 设置当前开关状态
     *
     * @param check
     */
    public void setChecked(boolean check) {
        sc.setChecked(check);
        if (check) {
            tvDesc.setText(descOn);
        } else {
            tvDesc.setText(descOff);
        }
    }

    @Override
    public boolean performClick() {
        setChecked(!isChecked());
        return super.performClick();
    }

}
