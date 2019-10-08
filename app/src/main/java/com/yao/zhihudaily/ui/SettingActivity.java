package com.yao.zhihudaily.ui;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tencent.tinker.lib.tinker.TinkerInstaller;
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
 * @author Yao
 * @date 2016/10/1
 */
public class SettingActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.setting_switch_compat_splash)
    SettingSwitchCompat mSettingSwitchCompat;
    @BindView(R.id.tv_clear_cache)
    TextView mTvClearCache;
    @BindView(R.id.tv_cache_size)
    TextView mTvCacheSize;
    @BindView(R.id.layout_clear_cache)
    RelativeLayout mLayoutClearCache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);

        mToolbar.setNavigationOnClickListener(view -> finish());
        mSettingSwitchCompat.setChecked(SP.getBoolean(SP.Key.SPLASH, true));
        mTvCacheSize.setText(FileUtil.formatFileSize(FileUtil.getFileSize(new File(FileUtil.STORAGE_DIR))));
    }

    @OnClick({
            R.id.setting_switch_compat_splash,
            R.id.layout_clear_cache,
            R.id.layout_hot_fix
    })
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.setting_switch_compat_splash:
                SP.put(SP.Key.SPLASH, mSettingSwitchCompat.isChecked());
                break;
            case R.id.layout_clear_cache:
                FileUtil.delete(new File(FileUtil.STORAGE_DIR));
                T.s(mLayoutClearCache, "缓存清理完成!");
                mTvCacheSize.setText(R.string.empty_file_size);
                break;
            case R.id.layout_hot_fix:
                T.t("已经热修复过了");
                Log.e("YAO", "已经热修复过了");
                TinkerInstaller.onReceiveUpgradePatch(getApplicationContext(), Environment.getExternalStorageDirectory().getAbsolutePath() + "/patch_signed_7zip.apk");
                break;
            default:
                break;
        }
    }
}
