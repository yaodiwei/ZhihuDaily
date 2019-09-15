package com.yao.zhihudaily.model;

import androidx.annotation.NonNull;

/**
 * @author Yao
 * @date 2016/9/16
 */
public class Recommender {

    private int id;
    private String bio;
    private String zhihu_url_token;
    private String avatar;
    private String name;

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getZhihu_url_token() {
        return zhihu_url_token;
    }

    public void setZhihu_url_token(String zhihu_url_token) {
        this.zhihu_url_token = zhihu_url_token;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NonNull
    @Override
    public String toString() {
        return "Recommender{" +
                "bio='" + bio + '\'' +
                ", zhihu_url_token='" + zhihu_url_token + '\'' +
                ", id=" + id +
                ", avatar='" + avatar + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
