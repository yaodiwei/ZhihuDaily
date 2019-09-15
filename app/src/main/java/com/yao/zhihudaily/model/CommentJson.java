package com.yao.zhihudaily.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * @author Yao
 * @date 2016/8/30
 */
public class CommentJson {

    @SerializedName("comments")
    private ArrayList<Comment> comments;

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }
}
