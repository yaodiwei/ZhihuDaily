package com.yao.zhihudaily.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yao.zhihudaily.R;
import com.yao.zhihudaily.ui.view.SettingSwitchCompat;
import com.yao.zhihudaily.util.FileUtil;
import com.yao.zhihudaily.util.SP;
import com.yao.zhihudaily.util.T;

import java.io.File;

import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 *
 * @author Administrator
 * @date 2016/10/1
 */
public class SettingActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.settingSplash)
    SettingSwitchCompat settingSwitchCompat;
    @BindView(R.id.tvClearCache)
    TextView tvClearCache;
    @BindView(R.id.tvCacheSize)
    TextView tvCacheSize;
    @BindView(R.id.rlClearCache)
    RelativeLayout rlClearCache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);

        toolbar.setNavigationOnClickListener(view -> finish());
        settingSwitchCompat.setChecked(SP.getBoolean(SP.Key.SPLASH, true));
        tvCacheSize.setText(FileUtil.formatFileSize(FileUtil.getFileSize(new File(FileUtil.STORAGE_DIR))));
    }

    @OnClick({
            R.id.settingSplash,
            R.id.rlClearCache
    })
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.settingSplash:
                SP.put(SP.Key.SPLASH, settingSwitchCompat.isChecked());
                break;
            case R.id.rlClearCache:
                FileUtil.delete(new File(FileUtil.STORAGE_DIR));
                T.s(rlClearCache, "缓存清理完成!");
                tvCacheSize.setText("0B");
                break;
            default:
                break;
        }
    }
}
