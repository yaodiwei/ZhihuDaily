package com.yao.zhihudaily.util;

import android.annotation.SuppressLint;
import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.text.DecimalFormat;

/**
 * @author Yao
 * @date 2016/10/2
 */
public class FileUtil {

    public static String STORAGE_DIR;

    @SuppressLint("StaticFieldLeak")
    private static Context sContext;

    public static void init(Context ctx){
        sContext = ctx;
        STORAGE_DIR = sContext.getExternalCacheDir().getAbsolutePath();
    }

    public static long getFileSizes(File f) throws Exception {//取得文件大小
        long s = 0;
        if (f.exists()) {
            FileInputStream fis;
            fis = new FileInputStream(f);
            s = fis.available();
        } else {
            System.out.println("文件不存在");
        }
        return s;
    }

    public static long getFileSize(File f) {
        long size = 0;
        File[] files = f.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    size = size + getFileSize(file);
                } else {
                    size = size + file.length();
                }
            }
        }
        return size;
    }

    public static String formatFileSize(long fileS) {//转换文件大小
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString;
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "GB";
        }
        return fileSizeString;
    }

    public static long getFileCount(File f) {//递归求取目录文件个数
        long size;
        File[] files = f.listFiles();
        size = files.length;
        for (File file : files) {
            if (file.isDirectory()) {
                size = size + getFileCount(file);
                size--;
            }
        }
        return size;
    }

    public static void delete(File file) {
        if (file.isFile()) {
            file.delete();
            return;
        }

        if (file.isDirectory()) {
            File[] childFiles = file.listFiles();
            if (childFiles == null || childFiles.length == 0) {
                file.delete();
                return;
            }

            for (int i = 0; i < childFiles.length; i++) {
                delete(childFiles[i]);
            }
            file.delete();
        }
    }
}
