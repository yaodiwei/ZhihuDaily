package com.yao.zhihudaily.model

import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * @author Yao
 * @date 2016/9/13
 */
class SectionStory {

    var id: Int = 0
    var images: ArrayList<String>? = null
    var title: String? = null
    var date: String? = null
    @SerializedName("display_date")
    var displayDate: String? = null

    override fun toString(): String {
        return "SectionStory{" +
                "id=" + id +
                ", images=" + images +
                ", title='" + title + '\''.toString() +
                ", date='" + date + '\''.toString() +
                ", displayDate='" + displayDate + '\''.toString() +
                '}'.toString()
    }
}
