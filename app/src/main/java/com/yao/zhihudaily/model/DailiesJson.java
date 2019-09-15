package com.yao.zhihudaily.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * @author Yao
 * @date 2016/7/24
 */
public class DailiesJson {

    private String date;
    private ArrayList<Daily> stories;
    @SerializedName("top_stories")
    private ArrayList<Daily> topStories;

    /**
     * 获取 stories 和 top_stories 集合
     * @return 返回一个ArrayList
     */
    public ArrayList<Daily> getAllStories(){
        ArrayList<Daily> allDailyList = new ArrayList<>(stories);
        if (topStories != null) {
            allDailyList.addAll(topStories);
        }
        return allDailyList;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<Daily> getStories() {
        return stories;
    }

    public void setStories(ArrayList<Daily> stories) {
        this.stories = stories;
    }

    public ArrayList<Daily> getTopStories() {
        return topStories;
    }

    public void setTopStories(ArrayList<Daily> topStories) {
        this.topStories = topStories;
    }
}
