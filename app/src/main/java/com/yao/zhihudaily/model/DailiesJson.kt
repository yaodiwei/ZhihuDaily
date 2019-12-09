package com.yao.zhihudaily.model

import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * @author Yao
 * @date 2016/7/24
 */
class DailiesJson {

    var date: String? = null
    var stories: ArrayList<Daily>? = null
    @SerializedName("top_stories")
    var topStories: ArrayList<Daily>? = null

    /**
     * 获取 stories 和 top_stories 集合
     * @return 返回一个ArrayList
     */
    val allStories: ArrayList<Daily>
        get() {
            val allDailyList = ArrayList(stories!!)
            if (topStories != null) {
                allDailyList.addAll(topStories!!)
            }
            return allDailyList
        }
}
