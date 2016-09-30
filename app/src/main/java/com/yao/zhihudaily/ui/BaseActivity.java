package com.yao.zhihudaily.ui;

import android.app.Activity;
import android.os.Bundle;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/10/1.
 * 暂时没什么业务好写在这里
 */

public abstract class BaseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResID());
        ButterKnife.bind(this);
    }

    public abstract int getLayoutResID();
}
