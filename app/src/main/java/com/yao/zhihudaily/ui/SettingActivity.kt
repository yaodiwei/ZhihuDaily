package com.yao.zhihudaily.ui

import android.os.Bundle
import android.os.Environment
import android.view.View
import com.tencent.tinker.lib.tinker.TinkerInstaller
import com.yao.zhihudaily.R
import com.yao.zhihudaily.util.FileUtil
import com.yao.zhihudaily.util.SP
import com.yao.zhihudaily.util.T
import kotlinx.android.synthetic.main.activity_setting.*
import java.io.File


/**
 *
 * @author Yao
 * @date 2016/10/1
 */
class SettingActivity : BaseActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        toolbar!!.setNavigationOnClickListener { view -> finish() }
        setting_switch_compat_splash!!.isChecked = SP.getBoolean(SP.Key.SPLASH, true)
        tv_cache_size!!.text = FileUtil.formatFileSize(FileUtil.getFileSize(File(FileUtil.STORAGE_DIR)))

        setting_switch_compat_splash.setOnClickListener(this)
        layout_clear_cache.setOnClickListener(this)
        layout_hot_fix.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.setting_switch_compat_splash -> SP.put(SP.Key.SPLASH, setting_switch_compat_splash!!.isChecked)
            R.id.layout_clear_cache -> {
                FileUtil.delete(File(FileUtil.STORAGE_DIR))
                T.s(layout_clear_cache!!, "缓存清理完成!")
                tv_cache_size!!.setText(R.string.empty_file_size)
            }
            R.id.layout_hot_fix -> TinkerInstaller.onReceiveUpgradePatch(applicationContext, Environment.getExternalStorageDirectory().absolutePath + "/patch_signed_7zip.apk")
            else -> {
            }
        }
    }
}
