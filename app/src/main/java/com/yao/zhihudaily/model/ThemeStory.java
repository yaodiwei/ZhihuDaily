package com.yao.zhihudaily.model;

import java.util.ArrayList;

import androidx.annotation.NonNull;

/**
 * @author Yao
 * @date 2016/9/10
 */
public class ThemeStory {

    private ArrayList<String> images;
    private int type;
    private int id;
    private String title;

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @NonNull
    @Override
    public String toString() {
        return "ThemeStory{" +
                "title='" + title + '\'' +
                ", id=" + id +
                ", type=" + type +
                ", images=" + images +
                '}';
    }
}
