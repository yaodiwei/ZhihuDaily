package com.yao.zhihudaily.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/9/22.
 */

public class HotJson {

    @SerializedName("recent")
    private ArrayList<Hot> hots;

    public ArrayList<Hot> getHots() {
        return hots;
    }

    public void setHots(ArrayList<Hot> hots) {
        this.hots = hots;
    }
}
