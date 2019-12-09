package com.yao.zhihudaily.model

import com.google.gson.annotations.SerializedName

import java.io.Serializable

/**
 *
 * @author Yao
 * @date 2016/8/30
 */
class StoryExtra : Serializable {

    var popularity: Int = 0

    @SerializedName("long_comments")
    var longComments: Int = 0

    @SerializedName("short_comments")
    var shortComments: Int = 0

    var comments: Int = 0

    override fun toString(): String {
        return "StoryExtra{" +
                "popularity=" + popularity +
                ", longComments=" + longComments +
                ", shortComments=" + shortComments +
                ", comments=" + comments +
                '}'.toString()
    }
}
