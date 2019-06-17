package com.yao.zhihudaily.ui;

import android.os.Bundle;

import com.umeng.analytics.MobclickAgent;

import androidx.appcompat.app.AppCompatActivity;


/**
 * @author Yao
 * @date 2016/10/1
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        PushAgent.getInstance(context).onAppStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
