package com.yao.zhihudaily.model

import java.util.*

/**
 * @author Yao
 * @date 2016/9/10
 */
class ThemeStory {

    var images: ArrayList<String>? = null
    var type: Int = 0
    var id: Int = 0
    var title: String? = null

    override fun toString(): String {
        return "ThemeStory{" +
                "title='" + title + '\''.toString() +
                ", id=" + id +
                ", type=" + type +
                ", images=" + images +
                '}'.toString()
    }
}
