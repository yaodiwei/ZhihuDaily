package com.yao.zhihudaily.ui;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;

import com.yao.zhihudaily.R;
import com.yao.zhihudaily.net.UrlConstants;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/9/17.
 */
public class ProfilePageActivity extends BaseActivity {


    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.webView)
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);
        ButterKnife.bind(this);

        int id = getIntent().getIntExtra("id", 0);
        String name = getIntent().getStringExtra("name");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        toolbar.setTitle(name);
        webView.loadUrl(String.format(UrlConstants.EDITOR, id));

    }
}
