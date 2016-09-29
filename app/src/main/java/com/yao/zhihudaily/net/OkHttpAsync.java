package com.yao.zhihudaily.net;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.Callback;
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
public class OkHttpAsync {

    private static final String TAG = "OkHttpSync";

    public static final MediaType MEDIA_TYPE_NONE = MediaType.parse("application/x-; charset=utf-8");
    public static final MediaType MEDIA_TYPE_UNKNOWN = MediaType.parse("application/octet-stream; charset=utf-8");

    private static final OkHttpClient client;

    static
    {
        client = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS).build();
    }

    public static void get(String url, Callback callback)
    {
        get(client, url, callback);
    }

    public static void postParams(String url, FormBody formBody, Callback callback)
    {
        postParams(client, url, formBody, callback);
    }

    public static void postFile(String url, Callback callback, File file) {
        postFile(client, url, callback, file);
    }

    public static void postFiles(String url, Callback callback, FileInput ...fileInputs) {
        postFiles(client, url, callback, fileInputs);
    }

    public static void get(OkHttpClient okHttpClient, String url, Callback callback)
    {
        Request request = new Request.Builder().url(url).build();
        okHttpClient.newCall(request).enqueue(callback);
    }

    public static void postParams(OkHttpClient okHttpClient, String url, FormBody formBody, Callback callback)
    {
        Request request = new Request.Builder().url(url).post(formBody).build();
        okHttpClient.newCall(request).enqueue(callback);
    }

    public static void postFile(OkHttpClient okHttpClient, String url, Callback callback, File file) {
        RequestBody requestBody = null;
        requestBody = RequestBody.create(MEDIA_TYPE_UNKNOWN, file);
        Request request = new Request.Builder().url(url).post(requestBody).build();
        okHttpClient.newCall(request).enqueue(callback);
    }

    public static void postFiles(OkHttpClient okHttpClient, String url, Callback callback, FileInput ...fileInputs) {
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        for (FileInput fileInput : fileInputs) {
            builder.addFormDataPart(fileInput.key, fileInput.name, RequestBody.create(MEDIA_TYPE_UNKNOWN, fileInput.file));
        }
        RequestBody requestBody = builder.build();
        Request request = new Request.Builder().url(url).post(requestBody).build();
        okHttpClient.newCall(request).enqueue(callback);
    }

    public static File saveFile(Response response, String destFileDir, String destFileName) throws IOException {
        InputStream is = null;
        byte[] buf = new byte[2048];
        int len = 0;
        FileOutputStream fos = null;
        try {
            is = response.body().byteStream();
            //total:文件总长度  sum:当前文件已下载长度
            final long total = response.body().contentLength();
            long sum = 0;

            File dir = new File(destFileDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(dir, destFileName);
            fos = new FileOutputStream(file);
            while ((len = is.read(buf)) != -1) {
                sum += len;
                fos.write(buf, 0, len);
            }
            fos.flush();

            return file;

        } finally {
            try {
                response.body().close();
                if (is != null) is.close();
            } catch (IOException e) {
            }
            try {
                if (fos != null) fos.close();
            } catch (IOException e) {
            }

        }
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
