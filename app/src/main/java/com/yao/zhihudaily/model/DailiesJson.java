package com.yao.zhihudaily.model;

import java.util.List;

/**
 * Created by Administrator on 2016/7/24.
 */
public class DailiesJson {

    private String date;
    private List<Daily> stories;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<Daily> getStories() {
        return stories;
    }

    public void setStories(List<Daily> stories) {
        this.stories = stories;
    }
}
