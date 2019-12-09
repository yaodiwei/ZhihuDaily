package com.yao.zhihudaily.model

/**
 * @author Yao
 * @date 2016/9/13
 */
class Section {

    var id: Int = 0
    var name: String? = null
    var description: String? = null
    var thumbnail: String? = null


    override fun toString(): String {
        return "Section{" +
                "id=" + id +
                ", name='" + name + '\''.toString() +
                ", description='" + description + '\''.toString() +
                ", thumbnail='" + thumbnail + '\''.toString() +
                '}'.toString()
    }
}
