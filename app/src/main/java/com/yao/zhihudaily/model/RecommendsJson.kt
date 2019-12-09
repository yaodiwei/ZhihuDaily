package com.yao.zhihudaily.model

import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * @author Yao
 * @date 2016/9/16
 */
class RecommendsJson {

    var items: ArrayList<Item>? = null
    @SerializedName("item_count")
    var itemCount: Int = 0


    inner class Item {
        var index: Int = 0
        lateinit var recommenders: ArrayList<Recommender>
        lateinit var author: Author

        override fun toString(): String {
            return "Item{" +
                    "index=" + index +
                    ", recommenders=" + recommenders +
                    ", author=" + author +
                    '}'.toString()
        }
    }

    inner class Author {
        lateinit var name: String

        override fun toString(): String {
            return "Author{" +
                    "name='" + name + '\''.toString() +
                    '}'.toString()
        }
    }


    override fun toString(): String {
        return "RecommendsJson{" +
                "items=" + items +
                ", itemCount=" + itemCount +
                '}'.toString()
    }
}
