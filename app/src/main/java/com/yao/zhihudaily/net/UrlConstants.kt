package com.yao.zhihudaily.net

/**
 * @author Yao
 * @date 2016/7/24
 */
interface UrlConstants {
    companion object {

        val DAILIES = "http://news-at.zhihu.com/api/4/news/latest"

        val DAILIES_BEFORE = "http://news.at.zhihu.com/api/4/news/before/%1\$s"

        val NEWS = "http://news-at.zhihu.com/api/4/news/%1\$s"

        val STORY_EXTRA = "http://news-at.zhihu.com/api/4/story-extra/%1\$s"

        val SHORT_COMMENTS = "http://news-at.zhihu.com/api/4/story/%1\$s/short-comments"

        val LONG_COMMENTS = "http://news-at.zhihu.com/api/4/story/%1\$s/long-comments"

        val THEMES = "http://news-at.zhihu.com/api/4/themes"

        val THEME = "http://news-at.zhihu.com/api/4/theme/%1\$s"

        val HOT = "http://news-at.zhihu.com/api/3/news/hot"

        val SECTIONS = "http://news-at.zhihu.com/api/3/sections"

        val SECTION = "http://news-at.zhihu.com/api/3/section/%1\$s"

        val SECTION_BEFORE = "http://news-at.zhihu.com/api/3/section/%1\$s/before/%2\$s"

        val RECOMMENDERS = "http://news-at.zhihu.com/api/4/story/%1\$s/recommenders"

        val EDITOR = "http://news-at.zhihu.com/api/4/editor/%1\$s/profile-page/android"

        val STORY_SHARE = "http://daily.zhihu.com/story/%1\$s"
    }

}
