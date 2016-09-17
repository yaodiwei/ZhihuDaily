package com.yao.zhihudaily.model;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/9/13.
 */
public class SectionJson {

    private long timestamp;

    private ArrayList<SectionStory> stories;

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public ArrayList<SectionStory> getStories() {
        return stories;
    }

    public void setStories(ArrayList<SectionStory> stories) {
        this.stories = stories;
    }
}
