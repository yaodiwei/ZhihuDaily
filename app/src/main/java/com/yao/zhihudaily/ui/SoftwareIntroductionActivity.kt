package com.yao.zhihudaily.ui

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import butterknife.BindView
import butterknife.ButterKnife
import com.yao.zhihudaily.R

/**
 *
 * @author Yao
 * @date 2016/9/26
 */

class SoftwareIntroductionActivity : BaseActivity() {

    @JvmField
    @BindView(R.id.toolbar)
    internal var mToolbar: Toolbar? = null
    @JvmField
    @BindView(R.id.iv1)
    internal var mIv1: ImageView? = null
    @JvmField
    @BindView(R.id.iv2)
    internal var mIv2: ImageView? = null
    @JvmField
    @BindView(R.id.iv3)
    internal var mIv3: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_software_introduction)
        ButterKnife.bind(this)

        mToolbar!!.setNavigationOnClickListener { view -> finish() }
        //        Glide.with(this).load(Uri.parse("file:///android_asset/PullDownRefresh.gif")).asGif().into(mIv1);
        //        Glide.with(this).load(Uri.parse("file:///android_asset/PullDownRefresh.gif")).asGif().into(mIv2);

    }
}
