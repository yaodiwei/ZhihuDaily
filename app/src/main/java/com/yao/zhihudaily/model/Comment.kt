package com.yao.zhihudaily.model

import com.google.gson.annotations.SerializedName
import com.yao.zhihudaily.util.DateUtil

/**
 * @author yao
 * @date 2016/8/30
 */
class Comment {

    var id: Int = 0
    var author: String? = null
    var content: String? = null
    var avatar: String? = null
    var time: Long = 0
    @SerializedName("reply_to")
    var replyTo: ReplyTo? = null
    var likes: Int = 0

    val timeStr: String
        get() = DateUtil.format(time)

    override fun toString(): String {
        return "Comment{" +
                "id=" + id +
                ", author='" + author + '\''.toString() +
                ", content='" + content + '\''.toString() +
                ", avatar='" + avatar + '\''.toString() +
                ", time='" + time + '\''.toString() +
                ", replyTo=" + replyTo +
                ", likes=" + likes +
                '}'.toString()
    }

    inner class ReplyTo(var id: Int, var author: String?, var content: String?, var status: Int) {

        override fun toString(): String {
            return "ReplyTo{" +
                    "id=" + id +
                    ", author='" + author + '\''.toString() +
                    ", content='" + content + '\''.toString() +
                    ", status=" + status +
                    '}'.toString()
        }
    }

}
