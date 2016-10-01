package com.yao.zhihudaily.ui.view;

import android.content.Context;
import android.support.v7.widget.SwitchCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yao.zhihudaily.R;

/**
 * Created by Administrator on 2016/10/1.
 */

public class SettingItemView extends RelativeLayout {

    private static final String NAMESPACE = "http://schemas.android.com/apk/res-auto";

    private TextView tvTitle;
    private TextView tvDesc;
    private SwitchCompat sc;

    private String title;
    private String descOn;
    private String descOff;

    public SettingItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }
    public SettingItemView(Context context, AttributeSet attrs) {
        super(context, attrs);

        title = attrs.getAttributeValue(NAMESPACE, "setting_title");// 根据属性名称,获取属性的值
        descOn = attrs.getAttributeValue(NAMESPACE, "desc_on");
        descOff = attrs.getAttributeValue(NAMESPACE, "desc_off");

        initView();
    }

    public SettingItemView(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        // 将自定义好的布局文件设置给当前的SettingItemView
        View.inflate(getContext(), R.layout.view_setting_item, this);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvDesc = (TextView) findViewById(R.id.tvDesc);
        sc = (SwitchCompat) findViewById(R.id.sc);
        setTitle(title);// 设置标题
    }

    public void setTitle(String title) {
        tvTitle.setText(title);
    }

    public void setDesc(String desc) {
        tvDesc.setText(desc);
    }

    /**
     * 返回勾选状态
     *
     * @return
     */
    public boolean isChecked() {
        return sc.isChecked();
    }

    public void setChecked(boolean check) {
        sc.setChecked(check);
        // 根据选择的状态,更新文本描述
        if (check) {
            setDesc(descOn);
        } else {
            setDesc(descOff);
        }
    }

    @Override
    public boolean performClick() {
        setChecked(!isChecked());
        return super.performClick();
    }

}
