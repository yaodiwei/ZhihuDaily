package com.yao.zhihudaily.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * @author Yao
 * @date 2016/9/22
 */
public class SectionsJson {

    @SerializedName("data")
    private ArrayList<Section> sections;

    public ArrayList<Section> getSections() {
        return sections;
    }

    public void setSections(ArrayList<Section> sections) {
        this.sections = sections;
    }
}
