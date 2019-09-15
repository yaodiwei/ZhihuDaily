package com.yao.zhihudaily.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import androidx.annotation.NonNull;

/**
 * @author Yao
 * @date 2016/7/26
 * Daily详情
 */
public class DailyJson {

    private String body;

    @SerializedName("image_source")
    private String imageSource;

    private String title;

    private String image;

    @SerializedName("share_url")
    private String shareUrl;

    @SerializedName("ga_prefix")
    private String time;

    private int type;

    private int id;

    private ArrayList<String> css;

    private ArrayList<String> js;

    private Section section;

    private ArrayList<Recommender> recommenders;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getImageSource() {
        return imageSource;
    }

    public void setImageSource(String imageSource) {
        this.imageSource = imageSource;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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

    public ArrayList<String> getCss() {
        return css;
    }

    public void setCss(ArrayList<String> css) {
        this.css = css;
    }

    public ArrayList<String> getJs() {
        return js;
    }

    public void setJs(ArrayList<String> js) {
        this.js = js;
    }

    public Section getSection() {
        return section;
    }

    public void setSection(Section section) {
        this.section = section;
    }

    public ArrayList<Recommender> getRecommenders() {
        return recommenders;
    }

    public void setRecommenders(ArrayList<Recommender> recommenders) {
        this.recommenders = recommenders;
    }

    @NonNull
    @Override
    public String toString() {
        return "DailyJson{" +
                "body='" + body + '\'' +
                ", imageSource='" + imageSource + '\'' +
                ", title='" + title + '\'' +
                ", image='" + image + '\'' +
                ", shareUrl='" + shareUrl + '\'' +
                ", time='" + time + '\'' +
                ", type=" + type +
                ", id=" + id +
                ", css=" + css +
                ", js=" + js +
                ", section=" + section +
                ", recommenders=" + recommenders +
                '}';
    }
}
