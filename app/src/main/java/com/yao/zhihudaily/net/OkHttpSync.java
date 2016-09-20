package com.yao.zhihudaily.net;

import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/7/23.
 */
public class OkHttpSync {

    private static final String TAG = "OkHttpSync";

    public static final MediaType MEDIA_TYPE_NONE = MediaType.parse("application/x-; charset=utf-8");
    public static final MediaType MEDIA_TYPE_UNKNOWN = MediaType.parse("application/octet-stream; charset=utf-8");

    private static final OkHttpClient client;

    static
    {
        Log.e(TAG, "static initializer: OkHttpSync");
        client = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS).build();
    }

    public static Response get(String url) throws IOException {
        return get(client, url);
    }

    public static Response postParams(String url, FormBody formBody) throws IOException {
        return postParams(client, url, formBody);
    }

    public static Response postFile(String url, File file) throws IOException {
        return postFile(client, url, file);
    }

    public static Response postFiles(String url, FileInput ...fileInputs) throws IOException {
        return postFiles(client, url, fileInputs);
    }

    public static Response get(OkHttpClient okHttpClient, String url) throws IOException {
        Request request = new Request.Builder().url(url).build();
        return okHttpClient.newCall(request).execute();
    }

    public static Response postParams(OkHttpClient okHttpClient, String url, FormBody formBody) throws IOException {
        Request request = new Request.Builder().url(url).post(formBody).build();
        return okHttpClient.newCall(request).execute();
    }

    public static Response postFile(OkHttpClient okHttpClient, String url, File file) throws IOException {
        RequestBody requestBody = null;
        requestBody = RequestBody.create(MEDIA_TYPE_UNKNOWN, file);
        Request request = new Request.Builder().url(url).post(requestBody).build();
        return okHttpClient.newCall(request).execute();
    }

    public static Response postFiles(OkHttpClient okHttpClient, String url, FileInput ...fileInputs) throws IOException {
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        for (FileInput fileInput : fileInputs) {
            builder.addFormDataPart(fileInput.key, fileInput.name, RequestBody.create(MEDIA_TYPE_UNKNOWN, fileInput.file));
        }
        RequestBody requestBody = builder.build();
        Request request = new Request.Builder().url(url).post(requestBody).build();
        return okHttpClient.newCall(request).execute();
    }



    public static class FileInput
    {
        public String key;
        public String name;
        public File file;

        public FileInput(String key, String name, File file)
        {
            this.key = key;
            this.name = name;
            this.file = file;
        }

        @Override
        public String toString()
        {
            return "FileInput{" +
                    "key='" + key + '\'' +
                    ", name='" + name + '\'' +
                    ", file=" + file +
                    '}';
        }
    }
}
