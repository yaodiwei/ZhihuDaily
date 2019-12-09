package com.yao.zhihudaily.ui

import android.os.Bundle
import android.webkit.WebView
import androidx.appcompat.widget.Toolbar
import butterknife.BindView
import butterknife.ButterKnife
import com.yao.zhihudaily.R
import com.yao.zhihudaily.net.UrlConstants
import com.yao.zhihudaily.tool.Constant

/**
 *
 * @author Yao
 * @date 2016/9/17
 */
class ProfilePageActivity : BaseActivity() {

    @JvmField
    @BindView(R.id.toolbar)
    internal var mToolbar: Toolbar? = null
    @JvmField
    @BindView(R.id.web_view)
    internal var mWebView: WebView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_page)
        ButterKnife.bind(this)

        val id = intent.getIntExtra(Constant.ID, 0)
        val name = intent.getStringExtra(Constant.NAME)

        mToolbar!!.setNavigationOnClickListener { v -> finish() }

        mToolbar!!.title = name
        mWebView!!.loadUrl(String.format(UrlConstants.EDITOR, id))

    }

    companion object {

        private val TAG = "ProfilePageActivity"
    }
}
