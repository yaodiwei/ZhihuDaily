package com.yao.zhihudaily.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.orhanobut.logger.Logger;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.PermissionListener;
import com.yao.zhihudaily.R;
import com.yao.zhihudaily.net.OkHttpAsync;
import com.yao.zhihudaily.net.OkHttpSync;
import com.yao.zhihudaily.net.ZhihuHttp;
import com.yao.zhihudaily.util.FileUtil;
import com.yao.zhihudaily.util.SP;

import java.io.File;
import java.io.IOException;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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

    public static final int REQUEST_PERMISSION_STORAGE = 200;

    public static final String START_IMAGE = "startImage";
    public static final String START_TEXT = "startText";

    public static final String START_IMAGE_FILE = "start_image.jpg";
    @BindView(R.id.iv_splash)
    ImageView mImageView;
    @BindView(R.id.tv_author)
    TextView mTvAuthor;

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
                mImageView.setImageBitmap(resource);
                if (AndPermission.hasPermission(SplashActivity.this, Permission.STORAGE)) {
                    animator();
                } else {
                    requestPermission();
                }
            }
        };

        File file = new File(FileUtil.STORAGE_DIR, START_IMAGE_FILE);
        if (file.exists()) {
            Glide.with(this).asBitmap().load(file).into(target);
            mTvAuthor.setText(SP.getString(START_TEXT, ""));
        } else {
            Glide.with(this).asBitmap().load(R.mipmap.miui7).into(target);
            mTvAuthor.setText("永远相信美好的事情即将发生");
        }

    }

    public void requestPermission() {
        AndPermission.with(this)
                .requestCode(REQUEST_PERMISSION_STORAGE)
                .permission(Permission.STORAGE)
                .rationale((requestCode, rationale) ->
                        new AlertDialog.Builder(SplashActivity.this)
                                .setTitle(R.string.tip)
                                .setMessage(R.string.permission_storage_rationale)
                                .setCancelable(false)
                                .setPositiveButton(R.string.open_permission_dialog, (dialog, which) -> rationale.resume())
                                .setNegativeButton(R.string.cancel, null)
                                .show())
                .callback(new PermissionListener() {
                    @Override
                    public void onSucceed(int requestCode, @androidx.annotation.NonNull List<String> grantedPermissions) {
                        if (requestCode == REQUEST_PERMISSION_STORAGE) {
                            animator();
                        }
                    }

                    @Override
                    public void onFailed(int requestCode, @androidx.annotation.NonNull List<String> deniedPermissions) {
                        if (requestCode == REQUEST_PERMISSION_STORAGE) {
                            new AlertDialog.Builder(SplashActivity.this)
                                    .setTitle(R.string.tip)
                                    .setMessage(R.string.permission_storage_failed)
                                    .setCancelable(false)
                                    .setPositiveButton(R.string.go_to_setting, (dialog, which) -> goToSetting())
                                    .setNegativeButton(R.string.cancel, null)
                                    .show();
                        }
                    }
                }).start();
    }

    private void animator() {
        Drawable drawable = mImageView.getDrawable();
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;

            mImageView.setPivotX(bitmapDrawable.getBitmap().getWidth() * 0.5f);
            mImageView.setPivotY(bitmapDrawable.getBitmap().getHeight() * 0.75f);
            ObjectAnimator objectAnimatorX = ObjectAnimator.ofFloat(mImageView, "scaleX", 1, 1.25f);
            ObjectAnimator objectAnimatorY = ObjectAnimator.ofFloat(mImageView, "scaleY", 1, 1.25f);
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


    }


    private void goToSetting() {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.setData(Uri.fromParts("package", getPackageName(), null));
        startActivity(intent);
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
