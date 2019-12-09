package com.yao.zhihudaily.model

import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * @author Yao
 * @date 2016/8/30
 */
class CommentJson {

    @SerializedName("comments")
    var comments: ArrayList<Comment>? = null
}
