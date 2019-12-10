package com.yao.zhihudaily.ui

import android.os.Bundle
import com.yao.zhihudaily.R
import kotlinx.android.synthetic.main.activity_main.*

/**
 *
 * @author Yao
 * @date 2016/9/26
 */

class SoftwareIntroductionActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_software_introduction)

        toolbar!!.setNavigationOnClickListener { view -> finish() }
        //        Glide.with(this).load(Uri.parse("file:///android_asset/PullDownRefresh.gif")).asGif().into(iv1);
        //        Glide.with(this).load(Uri.parse("file:///android_asset/PullDownRefresh.gif")).asGif().into(iv2);

    }
}
