package com.yao.zhihudaily.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
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
import com.yao.zhihudaily.net.OkHttpSync;
import com.yao.zhihudaily.net.ZhihuHttp;
import com.yao.zhihudaily.tool.Constants;
import com.yao.zhihudaily.util.SP;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import okhttp3.Response;


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

    private Disposable mDisposable;

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
                .filter(new Predicate<StartImageJson>() {
                    @Override
                    public boolean test(@NonNull StartImageJson startImageJson) throws Exception {
                        //与缓存url的不相等, 才进入下一步进行新url的缓存
                        return !SP.getString(START_TEXT, "").equals(startImageJson.getText());
                    }
                })
                .map(new Function<StartImageJson, String>() {
                    @Override
                    public String apply(@NonNull StartImageJson startImageJson) throws Exception {
                        SP.put(START_IMAGE, startImageJson.getImg());
                        SP.put(START_TEXT, startImageJson.getText());
                        return startImageJson.getImg();
                    }
                })
                .map(new Function<String, Response>() {
                    @Override
                    public Response apply(@NonNull String s) throws Exception {
                        return OkHttpSync.get(s);
                    }
                })
                .subscribe(new Observer<Response>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mDisposable = d;
                    }

                    @Override
                    public void onNext(@NonNull Response response) {
                        try {
                            OkHttpAsync.saveFile(response, Constants.STORAGE_DIR, START_IMAGE_FILE);
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
