package com.yao.zhihudaily.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.orhanobut.logger.Logger;
import com.yao.zhihudaily.R;
import com.yao.zhihudaily.model.StartImageJson;
import com.yao.zhihudaily.net.OkHttpAsync;
import com.yao.zhihudaily.net.ZhihuHttp;
import com.yao.zhihudaily.tool.Constants;
import com.yao.zhihudaily.util.SP;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import rx.Subscriber;

/**
 * Created by Administrator on 2016/9/29.
 */

public class SplashActivity extends BaseActivity {

    public static final String START_IMAGE = "startImage";
    public static final String START_TEXT = "startText";

    public static final String START_IMAGE_FILE = "start_image.jpg";
    @BindView(R.id.iv)
    ImageView iv;
    @BindView(R.id.tvAuthor)
    TextView tvAuthor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (!SP.getBoolean(SP.SPLASH, true)) {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        if (Build.VERSION.SDK_INT >= 14) {
            //隐藏navigationBar
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }

//        getStartImage();


        SimpleTarget target = new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {
                iv.setImageBitmap(bitmap);
                iv.setPivotX(bitmap.getWidth() * 0.3f);
                iv.setPivotY(bitmap.getHeight() * 0.25f);
                ObjectAnimator objectAnimatorX = ObjectAnimator.ofFloat(iv, "scaleX", 1, 1.25f);
                ObjectAnimator objectAnimatorY = ObjectAnimator.ofFloat(iv, "scaleY", 1, 1.25f);
                AnimatorSet set = new AnimatorSet();
                set.setDuration(2000).setStartDelay(1000);
                set.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        finish();
                    }
                });
                set.playTogether(objectAnimatorX, objectAnimatorY);
                set.start();
            }
        };

        File file = new File(Constants.STORAGE_DIR, START_IMAGE_FILE);
        if (file.exists()) {
            Glide.with(this).load(file).asBitmap().into(target);
            tvAuthor.setText(SP.getString(START_TEXT, ""));
        } else {
            Glide.with(this).load(R.mipmap.miui7).asBitmap().into(target);
            tvAuthor.setText("永远相信美好的事情即将发生");
        }
    }

    @Deprecated//此api已废弃
    private void getStartImage() {
        Subscriber subscriber = new Subscriber<StartImageJson>() {

            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                Logger.e(e, "Subscriber onError()");
                Log.e("YAO", "SplashActivity.java - onError() ----- e" + e);
            }

            @Override
            public void onNext(StartImageJson startImageJson) {
                //如果本地存的图片就是最新的图片,那么不用下载更新
                if (SP.getString(START_TEXT, "").equals(startImageJson.getText())) {
                    return;
                } else {
                    SP.put(START_IMAGE, startImageJson.getImg());
                    SP.put(START_TEXT, startImageJson.getText());
                    //TODO  改成Rx风格
                    OkHttpAsync.get(startImageJson.getImg(), new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            OkHttpAsync.saveFile(response, Constants.STORAGE_DIR, START_IMAGE_FILE);
                        }
                    });
                }
            }
        };

        //这个的返回不能在UI线程中执行,应该是IO线程
        ZhihuHttp.getZhihuHttp().getStartImage(subscriber);
    }
}
