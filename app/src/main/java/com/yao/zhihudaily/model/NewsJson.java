package com.yao.zhihudaily.model;

import java.util.List;

/**
 * Created by Administrator on 2016/7/24.
 */
public class NewsJson {

    private String date;
    private List<Story> stories;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<Story> getStories() {
        return stories;
    }

    public void setStories(List<Story> stories) {
        this.stories = stories;
    }
}
