package com.yao.zhihudaily.net

import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit

/**
 * @author Yao
 * @date 2016/7/23
 */
object OkHttpSync {

    val MEDIA_TYPE_NONE = "application/x-; charset=utf-8".toMediaTypeOrNull()
    val MEDIA_TYPE_UNKNOWN = "application/octet-stream; charset=utf-8".toMediaTypeOrNull()
    private val TAG = "OkHttpSync"
    private val client: OkHttpClient

    init {
        client = OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS).build()
    }

    @Throws(IOException::class)
    operator fun get(url: String): Response {
        return get(client, url)
    }

    @Throws(IOException::class)
    fun postParams(url: String, formBody: FormBody): Response {
        return postParams(client, url, formBody)
    }

    @Throws(IOException::class)
    fun postFile(url: String, file: File): Response {
        return postFile(client, url, file)
    }

    @Throws(IOException::class)
    fun postFiles(url: String, vararg fileInputs: FileInput): Response {
        return postFiles(client, url, *fileInputs)
    }

    @Throws(IOException::class)
    operator fun get(okHttpClient: OkHttpClient, url: String): Response {
        val request = Request.Builder().url(url).build()
        return okHttpClient.newCall(request).execute()
    }

    @Throws(IOException::class)
    fun postParams(okHttpClient: OkHttpClient, url: String, formBody: FormBody): Response {
        val request = Request.Builder().url(url).post(formBody).build()
        return okHttpClient.newCall(request).execute()
    }

    @Throws(IOException::class)
    fun postFile(okHttpClient: OkHttpClient, url: String, file: File): Response {
        var requestBody: RequestBody? = null
        requestBody = RequestBody.create(MEDIA_TYPE_UNKNOWN, file)
        val request = Request.Builder().url(url).post(requestBody).build()
        return okHttpClient.newCall(request).execute()
    }

    @Throws(IOException::class)
    fun postFiles(okHttpClient: OkHttpClient, url: String, vararg fileInputs: FileInput): Response {
        val builder = MultipartBody.Builder().setType(MultipartBody.FORM)
        for (fileInput in fileInputs) {
            builder.addFormDataPart(fileInput.key, fileInput.name, RequestBody.create(MEDIA_TYPE_UNKNOWN, fileInput.file))
        }
        val requestBody = builder.build()
        val request = Request.Builder().url(url).post(requestBody).build()
        return okHttpClient.newCall(request).execute()
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
