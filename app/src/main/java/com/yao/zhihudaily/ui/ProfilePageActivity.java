package com.yao.zhihudaily.ui;

import android.os.Bundle;
import android.webkit.WebView;

import com.yao.zhihudaily.R;
import com.yao.zhihudaily.net.UrlConstants;
import com.yao.zhihudaily.tool.Constant;

import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 * @author Yao
 * @date 2016/9/17
 */
public class ProfilePageActivity extends BaseActivity {

    private static final String TAG = "ProfilePageActivity";

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.web_view)
    WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);
        ButterKnife.bind(this);

        int id = getIntent().getIntExtra(Constant.ID, 0);
        String name = getIntent().getStringExtra(Constant.NAME);

        mToolbar.setNavigationOnClickListener(v -> finish());

        mToolbar.setTitle(name);
        mWebView.loadUrl(String.format(UrlConstants.EDITOR, id));

    }
}
