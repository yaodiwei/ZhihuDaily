package com.yao.zhihudaily.ui

import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.tencent.tinker.lib.tinker.TinkerInstaller
import com.yao.zhihudaily.R
import com.yao.zhihudaily.ui.view.SettingSwitchCompat
import com.yao.zhihudaily.util.FileUtil
import com.yao.zhihudaily.util.SP
import com.yao.zhihudaily.util.T
import java.io.File


/**
 *
 * @author Yao
 * @date 2016/10/1
 */
class SettingActivity : BaseActivity() {

    @JvmField
    @BindView(R.id.toolbar)
    internal var mToolbar: Toolbar? = null
    @JvmField
    @BindView(R.id.setting_switch_compat_splash)
    internal var mSettingSwitchCompat: SettingSwitchCompat? = null
    @JvmField
    @BindView(R.id.tv_clear_cache)
    internal var mTvClearCache: TextView? = null
    @JvmField
    @BindView(R.id.tv_cache_size)
    internal var mTvCacheSize: TextView? = null
    @JvmField
    @BindView(R.id.layout_clear_cache)
    internal var mLayoutClearCache: RelativeLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        ButterKnife.bind(this)

        mToolbar!!.setNavigationOnClickListener { view -> finish() }
        mSettingSwitchCompat!!.isChecked = SP.getBoolean(SP.Key.SPLASH, true)
        mTvCacheSize!!.text = FileUtil.formatFileSize(FileUtil.getFileSize(File(FileUtil.STORAGE_DIR)))
    }

    @OnClick(R.id.setting_switch_compat_splash, R.id.layout_clear_cache, R.id.layout_hot_fix)
    fun onClick(v: View) {
        when (v.id) {
            R.id.setting_switch_compat_splash -> SP.put(SP.Key.SPLASH, mSettingSwitchCompat!!.isChecked)
            R.id.layout_clear_cache -> {
                FileUtil.delete(File(FileUtil.STORAGE_DIR))
                T.s(mLayoutClearCache!!, "缓存清理完成!")
                mTvCacheSize!!.setText(R.string.empty_file_size)
            }
            R.id.layout_hot_fix -> TinkerInstaller.onReceiveUpgradePatch(applicationContext, Environment.getExternalStorageDirectory().absolutePath + "/patch_signed_7zip.apk")
            else -> {
            }
        }
    }
}
