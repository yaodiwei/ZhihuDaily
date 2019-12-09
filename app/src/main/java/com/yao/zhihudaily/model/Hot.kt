package com.yao.zhihudaily.model

import com.google.gson.annotations.SerializedName

/**
 * @author Yao
 * @date 2016/9/12
 */
class Hot {

    @SerializedName("news_id")
    var newsId: Int = 0

    var url: String? = null

    var thumbnail: String? = null

    var title: String? = null
}
