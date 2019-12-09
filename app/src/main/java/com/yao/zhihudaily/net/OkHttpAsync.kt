package com.yao.zhihudaily.net

import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.util.concurrent.TimeUnit

/**
 * @author Yao
 * @date 2016/7/23
 */
object OkHttpAsync {

    private val TAG = "OkHttpSync"

    val MEDIA_TYPE_NONE = "application/x-; charset=utf-8".toMediaTypeOrNull()
    val MEDIA_TYPE_UNKNOWN = "application/octet-stream; charset=utf-8".toMediaTypeOrNull()

    private val client: OkHttpClient

    init {
        client = OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS).build()
    }

    operator fun get(url: String, callback: Callback) {
        get(client, url, callback)
    }

    fun postParams(url: String, formBody: FormBody, callback: Callback) {
        postParams(client, url, formBody, callback)
    }

    fun postFile(url: String, callback: Callback, file: File) {
        postFile(client, url, callback, file)
    }

    fun postFiles(url: String, callback: Callback, vararg fileInputs: FileInput) {
        postFiles(client, url, callback, *fileInputs)
    }

    operator fun get(okHttpClient: OkHttpClient, url: String, callback: Callback) {
        val request = Request.Builder().url(url).build()
        okHttpClient.newCall(request).enqueue(callback)
    }

    fun postParams(okHttpClient: OkHttpClient, url: String, formBody: FormBody, callback: Callback) {
        val request = Request.Builder().url(url).post(formBody).build()
        okHttpClient.newCall(request).enqueue(callback)
    }

    fun postFile(okHttpClient: OkHttpClient, url: String, callback: Callback, file: File) {
        var requestBody: RequestBody? = null
        requestBody = RequestBody.create(MEDIA_TYPE_UNKNOWN, file)
        val request = Request.Builder().url(url).post(requestBody).build()
        okHttpClient.newCall(request).enqueue(callback)
    }

    fun postFiles(okHttpClient: OkHttpClient, url: String, callback: Callback, vararg fileInputs: FileInput) {
        val builder = MultipartBody.Builder().setType(MultipartBody.FORM)
        for (fileInput in fileInputs) {
            builder.addFormDataPart(fileInput.key, fileInput.name, RequestBody.create(MEDIA_TYPE_UNKNOWN, fileInput.file))
        }
        val requestBody = builder.build()
        val request = Request.Builder().url(url).post(requestBody).build()
        okHttpClient.newCall(request).enqueue(callback)
    }

    @Throws(IOException::class)
    fun saveFile(response: Response, destFileDir: String, destFileName: String): File {
        var inputStream: InputStream? = null
        val buf = ByteArray(2048)
        var len = 0
        var fos: FileOutputStream? = null
        try {
            inputStream = response.body!!.byteStream()
            //total:文件总长度  sum:当前文件已下载长度
            val total = response.body!!.contentLength()
            var sum: Long = 0

            val dir = File(destFileDir)
            if (!dir.exists()) {
                dir.mkdirs()
            }
            val file = File(dir, destFileName)
            fos = FileOutputStream(file)
            len = inputStream.read(buf)
            while (len != -1) {
                sum += len.toLong()
                fos.write(buf, 0, len)
            }
            fos.flush()

            return file

        } finally {
            try {
                response.body!!.close()
                inputStream?.close()
            } catch (e: IOException) {
            }

            try {
                fos?.close()
            } catch (e: IOException) {
            }

        }
    }

    class FileInput(var key: String, var name: String, var file: File) {

        override fun toString(): String {
            return "FileInput{" +
                    "key='" + key + '\''.toString() +
                    ", name='" + name + '\''.toString() +
                    ", file=" + file +
                    '}'.toString()
        }
    }
}
