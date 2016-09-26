package com.yao.zhihudaily.ui;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.yao.zhihudaily.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/9/26.
 */

public class SoftwareIntroductionActivity extends Activity {

    @BindView(R.id.iv1)
    ImageView iv1;
    @BindView(R.id.iv2)
    ImageView iv2;
    @BindView(R.id.iv3)
    ImageView iv3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_software_introduction);
        ButterKnife.bind(this);

        Glide.with(this).load(Uri.parse("file:///android_asset/PullDownRefresh.gif")).asGif().into(iv1);
        Glide.with(this).load(Uri.parse("file:///android_asset/PullDownRefresh.gif")).asGif().into(iv2);

    }
}
