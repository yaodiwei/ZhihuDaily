package com.yao.zhihudaily.tool;

import android.os.Environment;

import com.yao.zhihudaily.App;

import java.io.File;

/**
 * Created by Administrator on 2016/9/30.
 */

public class Constants {

    public static String STORAGE_DIR = Environment.getExternalStorageDirectory() + File.separator + App.app.getPackageName() + File.separator;

    static {
        STORAGE_DIR = App.app.getExternalCacheDir().getAbsolutePath();
    }
}
