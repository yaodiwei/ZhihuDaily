package com.yao.zhihudaily.model

import java.io.Serializable

/**
 * @author Yao
 * @date 2016/9/7
 * 列表里面的Theme Item
 */
class Theme : Serializable {

    var id: Int = 0
    var color: Int = 0
    var name: String? = null
    var description: String? = null
    var thumbnail: String? = null

    override fun toString(): String {
        return "Theme{" +
                "id=" + id +
                ", color=" + color +
                ", name='" + name + '\''.toString() +
                ", description='" + description + '\''.toString() +
                ", thumbnail='" + thumbnail + '\''.toString() +
                '}'.toString()
    }
}
