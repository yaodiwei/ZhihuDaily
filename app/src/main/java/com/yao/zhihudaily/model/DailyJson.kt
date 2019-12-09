package com.yao.zhihudaily.model

import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * @author Yao
 * @date 2016/7/26
 * Daily详情
 */
class DailyJson {

    var body: String? = null

    @SerializedName("image_source")
    var imageSource: String? = null

    var title: String? = null

    var image: String? = null

    @SerializedName("share_url")
    var shareUrl: String? = null

    @SerializedName("ga_prefix")
    var time: String? = null

    var type: Int = 0

    var id: Int = 0

    var css: ArrayList<String>? = null

    var js: ArrayList<String>? = null

    var section: Section? = null

    var recommenders: ArrayList<Recommender>? = null

    override fun toString(): String {
        return "DailyJson{" +
                "body='" + body + '\''.toString() +
                ", imageSource='" + imageSource + '\''.toString() +
                ", title='" + title + '\''.toString() +
                ", image='" + image + '\''.toString() +
                ", shareUrl='" + shareUrl + '\''.toString() +
                ", time='" + time + '\''.toString() +
                ", type=" + type +
                ", id=" + id +
                ", css=" + css +
                ", js=" + js +
                ", section=" + section +
                ", recommenders=" + recommenders +
                '}'.toString()
    }
}
