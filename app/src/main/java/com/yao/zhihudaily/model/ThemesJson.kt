package com.yao.zhihudaily.model

import java.util.*

/**
 * @author Yao
 * @date 2016/9/7
 */
class ThemesJson {

    var limit: Int = 0
    var subscribed: ArrayList<String>? = null
    var others: ArrayList<Theme>? = null

    override fun toString(): String {
        return "ThemesJson{" +
                "limit=" + limit +
                ", subscribed='" + subscribed + '\''.toString() +
                ", others=" + others +
                '}'.toString()
    }
}
