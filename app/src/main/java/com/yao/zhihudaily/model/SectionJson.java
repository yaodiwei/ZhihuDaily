package com.yao.zhihudaily.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/9/13.
 */
public class SectionJson {

    @SerializedName("timestamp")
    private long timeStamp;

    private ArrayList<SectionStory> stories;

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public ArrayList<SectionStory> getStories() {
        return stories;
    }

    public void setStories(ArrayList<SectionStory> stories) {
        this.stories = stories;
    }
}
