package com.yao.zhihudaily.ui;

import android.os.Bundle;
import android.widget.ImageView;

import com.yao.zhihudaily.R;

import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 * @author Yao
 * @date 2016/9/26
 */

public class SoftwareIntroductionActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.iv1)
    ImageView mIv1;
    @BindView(R.id.iv2)
    ImageView mIv2;
    @BindView(R.id.iv3)
    ImageView mIv3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_software_introduction);
        ButterKnife.bind(this);

        mToolbar.setNavigationOnClickListener(view -> finish());
//        Glide.with(this).load(Uri.parse("file:///android_asset/PullDownRefresh.gif")).asGif().into(mIv1);
//        Glide.with(this).load(Uri.parse("file:///android_asset/PullDownRefresh.gif")).asGif().into(mIv2);

    }
}
