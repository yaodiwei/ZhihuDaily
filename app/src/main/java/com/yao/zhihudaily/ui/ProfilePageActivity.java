package com.yao.zhihudaily.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;

import com.yao.zhihudaily.R;
import com.yao.zhihudaily.net.UrlConstants;

/**
 * Created by Administrator on 2016/9/17.
 */
public class ProfilePageActivity extends Activity {

    private WebView webView;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        int id = getIntent().getIntExtra("id", 0);
        String name = getIntent().getStringExtra("name");

        webView = (WebView) findViewById(R.id.webView);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });

        toolbar.setTitle(name);
        Log.e("YAO", "ProfilePageActivity.java - onCreate() ---------- id" + id );
        webView.loadUrl(String.format(UrlConstants.EDITORS, id));

    }
}
