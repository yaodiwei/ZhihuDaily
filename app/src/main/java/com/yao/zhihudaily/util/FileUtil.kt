package com.yao.zhihudaily.util

import android.annotation.SuppressLint
import android.content.Context

import java.io.File
import java.io.FileInputStream
import java.text.DecimalFormat

/**
 * @author Yao
 * @date 2016/10/2
 */
object FileUtil {

    lateinit var STORAGE_DIR: String

    @SuppressLint("StaticFieldLeak")
    private lateinit var sContext: Context

    fun init(ctx: Context) {
        sContext = ctx
        STORAGE_DIR = sContext.externalCacheDir!!.absolutePath
    }

    @Throws(Exception::class)
    fun getFileSizes(f: File): Long {//取得文件大小
        var s: Long = 0
        if (f.exists()) {
            val fis: FileInputStream
            fis = FileInputStream(f)
            s = fis.available().toLong()
        } else {
            println("文件不存在")
        }
        return s
    }

    fun getFileSize(f: File): Long {
        var size: Long = 0
        val files = f.listFiles()
        if (files != null) {
            for (file in files) {
                if (file.isDirectory) {
                    size = size + getFileSize(file)
                } else {
                    size = size + file.length()
                }
            }
        }
        return size
    }

    fun formatFileSize(fileS: Long): String {//转换文件大小
        val df = DecimalFormat("#.00")
        val fileSizeString: String
        if (fileS < 1024) {
            fileSizeString = df.format(fileS.toDouble()) + "B"
        } else if (fileS < 1048576) {
            fileSizeString = df.format(fileS.toDouble() / 1024) + "KB"
        } else if (fileS < 1073741824) {
            fileSizeString = df.format(fileS.toDouble() / 1048576) + "MB"
        } else {
            fileSizeString = df.format(fileS.toDouble() / 1073741824) + "GB"
        }
        return fileSizeString
    }

    fun getFileCount(f: File): Long {//递归求取目录文件个数
        var size: Long
        val files = f.listFiles()
        size = files.size.toLong()
        for (file in files) {
            if (file.isDirectory) {
                size = size + getFileCount(file)
                size--
            }
        }
        return size
    }

    fun delete(file: File) {
        if (file.isFile) {
            file.delete()
            return
        }

        if (file.isDirectory) {
            val childFiles = file.listFiles()
            if (childFiles == null || childFiles.size == 0) {
                file.delete()
                return
            }

            for (i in childFiles.indices) {
                delete(childFiles[i])
            }
            file.delete()
        }
    }
}
