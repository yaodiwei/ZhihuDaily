package com.yao.zhihudaily.model

import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * @author Yao
 * @date 2016/9/10
 * Daily详情
 */
class ThemeJson {

    var stories: ArrayList<ThemeStory>? = null
    var description: String? = null
    var background: String? = null
    var color: Int = 0
    var name: String? = null
    var image: String? = null
    var editors: ArrayList<Editor>? = null
    @SerializedName("image_source")
    var imageSource: String? = null

    override fun toString(): String {
        return "ThemeJson{" +
                "stories=" + stories +
                ", description='" + description + '\''.toString() +
                ", background='" + background + '\''.toString() +
                ", color=" + color +
                ", name='" + name + '\''.toString() +
                ", image='" + image + '\''.toString() +
                ", editors=" + editors +
                ", imageSource='" + imageSource + '\''.toString() +
                '}'.toString()
    }
}
