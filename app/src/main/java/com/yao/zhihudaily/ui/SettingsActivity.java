package com.yao.zhihudaily.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yao.zhihudaily.R;
import com.yao.zhihudaily.tool.Constants;
import com.yao.zhihudaily.ui.view.SettingItemView;
import com.yao.zhihudaily.util.FileUtil;
import com.yao.zhihudaily.util.SP;
import com.yao.zhihudaily.util.T;

import java.io.File;

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
    @BindView(R.id.tvClearCache)
    TextView tvClearCache;
    @BindView(R.id.tvCacheSize)
    TextView tvCacheSize;
    @BindView(R.id.rlClearCache)
    RelativeLayout rlClearCache;

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
        tvCacheSize.setText(FileUtil.formetFileSize(FileUtil.getFileSize(new File(Constants.STORAGE_DIR))));
        rlClearCache.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.settingSplash:
                SP.put(SP.SPLASH, settingSplash.isChecked());
                break;
            case R.id.rlClearCache:
                FileUtil.delete(new File(Constants.STORAGE_DIR));
                T.s(rlClearCache, "缓存清理完成!");
                tvCacheSize.setText("0B");
                break;
            default:
                break;
        }
    }
}
