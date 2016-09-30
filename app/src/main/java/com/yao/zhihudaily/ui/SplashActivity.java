package com.yao.zhihudaily.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

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

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import rx.Subscriber;

/**
 * Created by Administrator on 2016/9/29.
 */

public class SplashActivity extends Activity {

    public static final String START_IMAGE = "startImage";
    public static final String START_TEXT = "startText";

    public static final String START_IMAGE_FILE = "start_image.jpg";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ImageView iv = new ImageView(this);
        iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
        if (Build.VERSION.SDK_INT >= 14) {
            //隐藏navigationBar
            iv.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
        setContentView(iv);

        getStartImage();

        SimpleTarget target = new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {
                iv.setImageBitmap(bitmap);
                iv.setPivotX(bitmap.getWidth()*0.3f);
                iv.setPivotY(bitmap.getHeight()*0.25f);
                ObjectAnimator objectAnimatorX = ObjectAnimator.ofFloat(iv, "scaleX", 1, 1.25f);
                ObjectAnimator objectAnimatorY = ObjectAnimator.ofFloat(iv, "scaleY", 1, 1.25f);
                AnimatorSet set = new AnimatorSet();
                set.setDuration(2000).setStartDelay(1000);
                set.addListener(new AnimatorListenerAdapter(){
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
        } else {
            Glide.with(this).load(R.mipmap.miui7).asBitmap().into(target);
        }
    }

    private void getStartImage() {
        Subscriber subscriber = new Subscriber<StartImageJson>() {

            @Override
            public void onCompleted() {}

            @Override
            public void onError(Throwable e) {
                Logger.e(e, "Subscriber onError()");
            }

            @Override
            public void onNext(StartImageJson startImageJson) {
                SP.putString(START_IMAGE, startImageJson.getImg());
                SP.putString(START_TEXT, startImageJson.getImg());
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
        };

        ZhihuHttp.getZhihuHttp().getStartImage(subscriber);
    }
}
