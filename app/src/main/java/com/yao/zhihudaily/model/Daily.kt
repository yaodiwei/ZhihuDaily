package com.yao.zhihudaily.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*

/**
 * @author Yao
 * @date 2016/7/23
 * 列表里面的Daily Item
 */
class Daily : Serializable {

    var id: Int = 0

    var type: Int = 0

    @SerializedName("ga_prefix")
    var time: String? = null

    var title: String? = null

    var images: ArrayList<String>? = null

    var image: String? = null

    override fun toString(): String {
        return "Daily{" +
                "id=" + id +
                ", type=" + type +
                ", time='" + time + '\''.toString() +
                ", title='" + title + '\''.toString() +
                ", images=" + images +
                ", image='" + image + '\''.toString() +
                '}'.toString()
    }
}
