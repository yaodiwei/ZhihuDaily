package com.yao.zhihudaily.model

/**
 * @author Yao
 * @date 2016/9/16
 */
class Recommender {

    var id: Int = 0
    var bio: String? = null
    var zhihu_url_token: String? = null
    var avatar: String? = null
    var name: String? = null

    override fun toString(): String {
        return "Recommender{" +
                "bio='" + bio + '\''.toString() +
                ", zhihu_url_token='" + zhihu_url_token + '\''.toString() +
                ", id=" + id +
                ", avatar='" + avatar + '\''.toString() +
                ", name='" + name + '\''.toString() +
                '}'.toString()
    }
}
