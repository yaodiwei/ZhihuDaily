package com.yao.zhihudaily.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.yao.zhihudaily.R;

import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/9/26.
 */

public class SoftwareIntroductionActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
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

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
//        Glide.with(this).load(Uri.parse("file:///android_asset/PullDownRefresh.gif")).asGif().into(iv1);
//        Glide.with(this).load(Uri.parse("file:///android_asset/PullDownRefresh.gif")).asGif().into(iv2);

    }
}
