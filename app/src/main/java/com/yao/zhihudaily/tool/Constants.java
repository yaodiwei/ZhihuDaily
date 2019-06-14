package com.yao.zhihudaily.tool;

import com.yao.zhihudaily.App;

/**
 *
 * @author Yao
 * @date 2016/9/30
 */
public class Constants {

    public static String STORAGE_DIR;

    static {
        STORAGE_DIR = App.app.getExternalCacheDir().getAbsolutePath();
    }
}
