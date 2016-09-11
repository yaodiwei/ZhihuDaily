package com.yao.zhihudaily.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2016/9/12.
 */
public class Hot {

    @SerializedName("news_id")
    private int newsId;

    private String url;

    private String thumbnail;

    private String title;

    public int getNewsId() {
        return newsId;
    }

    public void setNewsId(int newsId) {
        this.newsId = newsId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
