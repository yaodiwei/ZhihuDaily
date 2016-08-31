package com.yao.zhihudaily.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2016/8/30.
 */
public class ShortCommentJson {

    @SerializedName("comments")
    private List<ShortComment> shortComments;

    public List<ShortComment> getShortComments() {
        return shortComments;
    }

    public void setShortComments(List<ShortComment> shortComments) {
        this.shortComments = shortComments;
    }
}
