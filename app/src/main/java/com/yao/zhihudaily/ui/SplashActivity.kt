package com.yao.zhihudaily.ui

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.orhanobut.logger.Logger
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.Permission
import com.yanzhenjie.permission.PermissionListener
import com.yao.zhihudaily.R
import com.yao.zhihudaily.net.OkHttpAsync
import com.yao.zhihudaily.net.OkHttpSync
import com.yao.zhihudaily.net.ZhihuHttp
import com.yao.zhihudaily.util.FileUtil
import com.yao.zhihudaily.util.SP
import io.reactivex.Observer
import io.reactivex.annotations.NonNull
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_splash.*
import okhttp3.Response
import java.io.File
import java.io.IOException


/**
 * @author Yao
 * @date 2016/9/29
 */

class SplashActivity : BaseActivity() {

    private var mDisposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        if (!SP.getBoolean(SP.Key.SPLASH, true)) {
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            finish()
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        //隐藏navigationBar
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION

        //        getStartImage();

        val target = object : SimpleTarget<Bitmap>() {
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                iv_splash?.setImageBitmap(resource)
                if (AndPermission.hasPermission(this@SplashActivity, *Permission.STORAGE)) {
                    animator()
                } else {
                    requestPermission()
                }
            }
        }

        val file = File(FileUtil.STORAGE_DIR, START_IMAGE_FILE)
        if (file.exists()) {
            Glide.with(this).asBitmap().load(file).into<SimpleTarget<Bitmap>>(target)
            tv_author!!.text = SP.getString(START_TEXT, "")
        } else {
            Glide.with(this).asBitmap().load(R.mipmap.miui7).into<SimpleTarget<Bitmap>>(target)
            tv_author!!.text = "永远相信美好的事情即将发生"
        }

    }

    fun requestPermission() {
        AndPermission.with(this)
                .requestCode(REQUEST_PERMISSION_STORAGE)
                .permission(*Permission.STORAGE)
                .rationale { requestCode, rationale ->
                    AlertDialog.Builder(this@SplashActivity)
                            .setTitle(R.string.tip)
                            .setMessage(R.string.permission_storage_rationale)
                            .setCancelable(false)
                            .setPositiveButton(R.string.open_permission_dialog) { dialog, which -> rationale.resume() }
                            .setNegativeButton(R.string.cancel, null)
                            .show()
                }
                .callback(object : PermissionListener {
                    override fun onSucceed(requestCode: Int, grantedPermissions: List<String>) {
                        if (requestCode == REQUEST_PERMISSION_STORAGE) {
                            animator()
                        }
                    }

                    override fun onFailed(requestCode: Int, deniedPermissions: List<String>) {
                        if (requestCode == REQUEST_PERMISSION_STORAGE) {
                            AlertDialog.Builder(this@SplashActivity)
                                    .setTitle(R.string.tip)
                                    .setMessage(R.string.permission_storage_failed)
                                    .setCancelable(false)
                                    .setPositiveButton(R.string.go_to_setting) { dialog, which -> goToSetting() }
                                    .setNegativeButton(R.string.cancel, null)
                                    .show()
                        }
                    }
                }).start()
    }

    private fun animator() {
        val drawable = iv_splash!!.drawable
        if (drawable is BitmapDrawable) {

            iv_splash!!.pivotX = drawable.bitmap.width * 0.5f
            iv_splash!!.pivotY = drawable.bitmap.height * 0.75f
            val objectAnimatorX = ObjectAnimator.ofFloat(iv_splash, View.SCALE_X, 1f, 1.25f)
            val objectAnimatorY = ObjectAnimator.ofFloat(iv_splash, View.SCALE_Y, 1f, 1.25f)
            val set = AnimatorSet()
            set.setDuration(2000).startDelay = 1000
            set.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                    finish()
                }
            })
            set.playTogether(objectAnimatorX, objectAnimatorY)
            set.start()
        }


    }


    private fun goToSetting() {
        val intent = Intent()
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.action = "android.settings.APPLICATION_DETAILS_SETTINGS"
        intent.data = Uri.fromParts("package", packageName, null)
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mDisposable != null) {
            mDisposable!!.dispose()
        }
    }

    @Deprecated("")//此api已废弃
    private fun getStartImage() {
        ZhihuHttp.zhihuHttp.startImage()
                .filter { startImageJson ->
                    //与缓存url的不相等, 才进入下一步进行新url的缓存
                    SP.getString(START_TEXT, "") != startImageJson.text
                }
                .map { startImageJson ->
                    SP.put(START_IMAGE, startImageJson.img!!)
                    SP.put(START_TEXT, startImageJson.text!!)
                    startImageJson.img
                }
                .map { s -> OkHttpSync.get(s) }
                .subscribe(object : Observer<Response> {
                    override fun onSubscribe(@NonNull d: Disposable) {
                        mDisposable = d
                    }

                    override fun onNext(@NonNull response: Response) {
                        try {
                            OkHttpAsync.saveFile(response, FileUtil.STORAGE_DIR, START_IMAGE_FILE)
                        } catch (e: IOException) {
                            onError(e)
                        }

                    }

                    override fun onError(@NonNull e: Throwable) {
                        Logger.e(e, "Subscriber onError()")
                    }

                    override fun onComplete() {

                    }
                })
    }

    companion object {

        val REQUEST_PERMISSION_STORAGE = 200

        val START_IMAGE = "startImage"
        val START_TEXT = "startText"

        val START_IMAGE_FILE = "start_image.jpg"
    }
}
