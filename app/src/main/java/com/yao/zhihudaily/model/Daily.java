package com.yao.zhihudaily.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author Yao
 * @date 2016/7/23
 * 列表里面的Daily Item
 */
public class Daily implements Serializable {

    private int id;

    private int type;

    @SerializedName("ga_prefix")
    private String time;

    private String title;

    private ArrayList<String> images;

    private String image;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Daily{" +
                "id=" + id +
                ", type=" + type +
                ", time='" + time + '\'' +
                ", title='" + title + '\'' +
                ", images=" + images +
                ", image='" + image + '\'' +
                '}';
    }
}
