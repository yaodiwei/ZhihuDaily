package com.yao.zhihudaily.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

import androidx.annotation.NonNull;

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

    @SerializedName("mutlipic")
    private boolean multiPic;

    private ArrayList<String> images;

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

    public boolean isMultiPic() {
        return multiPic;
    }

    public void setMultiPic(boolean multiPic) {
        this.multiPic = multiPic;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    @NonNull
    @Override
    public String toString() {
        return "Daily{" +
                "id=" + id +
                ", type=" + type +
                ", time='" + time + '\'' +
                ", title='" + title + '\'' +
                ", multiPic=" + multiPic +
                ", images=" + images +
                '}';
    }
}
