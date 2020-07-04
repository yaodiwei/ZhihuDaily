package com.yao.zhihudaily.model;

import com.google.gson.annotations.SerializedName;

public class UploadImageItem {

    @SerializedName("url_path")
    public String urlPath;//网络图片路径

    @SerializedName("timestamp")
    public long timestamp;

    @SerializedName("location")
    public String location;

    public transient String filePath;//本地图片路径

    public UploadImageItem() {
    }

    public UploadImageItem(String filePath, long timestamp, String location) {
        this.timestamp = timestamp;
        this.location = location;
        this.filePath = filePath;
    }

    @Override
    public String toString() {
        return "UploadImageItem{" +
                "urlPath='" + urlPath + '\'' +
                ", timestamp=" + timestamp +
                ", location='" + location + '\'' +
                ", filePath='" + filePath + '\'' +
                '}';
    }
}
