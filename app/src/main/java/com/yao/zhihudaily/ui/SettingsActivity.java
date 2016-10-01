package com.yao.zhihudaily.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.yao.zhihudaily.R;
import com.yao.zhihudaily.ui.view.SettingItemView;
import com.yao.zhihudaily.util.SP;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/10/1.
 */

public class SettingsActivity extends Activity implements View.OnClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.settingSplash)
    SettingItemView settingSplash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        settingSplash.setChecked(SP.getBoolean(SP.SPLASH, true));
        settingSplash.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.settingSplash:
                SP.put(SP.SPLASH, !settingSplash.isChecked());
                break;
            default:
                break;
        }
    }
}
