package com.yao.zhihudaily.ui

import android.os.Bundle
import com.yao.zhihudaily.R
import com.yao.zhihudaily.net.UrlConstants
import com.yao.zhihudaily.tool.Constant
import kotlinx.android.synthetic.main.activity_profile_page.*

/**
 *
 * @author Yao
 * @date 2016/9/17
 */
class ProfilePageActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_page)

        val id = intent.getIntExtra(Constant.ID, 0)
        val name = intent.getStringExtra(Constant.NAME)

        toolbar!!.setNavigationOnClickListener { v -> finish() }

        toolbar!!.title = name
        web_view!!.loadUrl(String.format(UrlConstants.EDITOR, id))

    }

    companion object {

        private val TAG = "ProfilePageActivity"
    }
}
