package com.yao.zhihudaily.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.orhanobut.logger.Logger;
import com.yao.zhihudaily.R;
import com.yao.zhihudaily.net.OkHttpAsync;
import com.yao.zhihudaily.net.OkHttpSync;
import com.yao.zhihudaily.net.ZhihuHttp;
import com.yao.zhihudaily.util.FileUtil;
import com.yao.zhihudaily.util.SP;

import java.io.File;
import java.io.IOException;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import okhttp3.Response;


/**
 * @author Yao
 * @date 2016/9/29
 */

public class SplashActivity extends BaseActivity {

    public static final String START_IMAGE = "startImage";
    public static final String START_TEXT = "startText";

    public static final String START_IMAGE_FILE = "start_image.jpg";
    @BindView(R.id.iv)
    ImageView iv;
    @BindView(R.id.tvAuthor)
    TextView tvAuthor;

    private Disposable mDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (!SP.getBoolean(SP.Key.SPLASH, true)) {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        //隐藏navigationBar
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

//        getStartImage();

        SimpleTarget<Bitmap> target = new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(@androidx.annotation.NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                iv.setImageBitmap(resource);
                iv.setPivotX(resource.getWidth() * 0.5f);
                iv.setPivotY(resource.getHeight() * 0.75f);
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

        File file = new File(FileUtil.STORAGE_DIR, START_IMAGE_FILE);
        if (file.exists()) {
            Glide.with(this).asBitmap().load(file).into(target);
            tvAuthor.setText(SP.getString(START_TEXT, ""));
        } else {
            Glide.with(this).asBitmap().load(R.mipmap.miui7).into(target);
            tvAuthor.setText("永远相信美好的事情即将发生");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDisposable != null) {
            mDisposable.dispose();
        }
    }

    @Deprecated//此api已废弃
    private void getStartImage() {
        ZhihuHttp.getZhihuHttp().getStartImage()
                .filter(startImageJson -> {
                    //与缓存url的不相等, 才进入下一步进行新url的缓存
                    return !SP.getString(START_TEXT, "").equals(startImageJson.getText());
                })
                .map(startImageJson -> {
                    SP.put(START_IMAGE, startImageJson.getImg());
                    SP.put(START_TEXT, startImageJson.getText());
                    return startImageJson.getImg();
                })
                .map(s -> OkHttpSync.get(s))
                .subscribe(new Observer<Response>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mDisposable = d;
                    }

                    @Override
                    public void onNext(@NonNull Response response) {
                        try {
                            OkHttpAsync.saveFile(response, FileUtil.STORAGE_DIR, START_IMAGE_FILE);
                        } catch (IOException e) {
                            onError(e);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Logger.e(e, "Subscriber onError()");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
