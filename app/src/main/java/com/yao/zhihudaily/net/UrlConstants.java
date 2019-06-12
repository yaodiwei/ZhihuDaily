package com.yao.zhihudaily.net;

/**
 * @author Yao
 * @date 2016/7/24
 */
public interface UrlConstants {

    String DAILIES = "http://news-at.zhihu.com/api/4/news/latest";

    String DAILIES_BEFORE = "http://news.at.zhihu.com/api/4/news/before/%1$s";

    String NEWS = "http://news-at.zhihu.com/api/4/news/%1$s";

    String STORY_EXTRA = "http://news-at.zhihu.com/api/4/story-extra/%1$s";

    String SHORT_COMMENTS = "http://news-at.zhihu.com/api/4/story/%1$s/short-comments";

    String LONG_COMMENTS = "http://news-at.zhihu.com/api/4/story/%1$s/long-comments";

    String THEMES = "http://news-at.zhihu.com/api/4/themes";

    String THEME = "http://news-at.zhihu.com/api/4/theme/%1$s";

    String HOT = "http://news-at.zhihu.com/api/3/news/hot";

    String SECTIONS = "http://news-at.zhihu.com/api/3/sections";

    String SECTION = "http://news-at.zhihu.com/api/3/section/%1$s";

    String SECTION_BEFORE = "http://news-at.zhihu.com/api/3/section/%1$s/before/%2$s";

    String RECOMMENDERS = "http://news-at.zhihu.com/api/4/story/%1$s/recommenders";

    String EDITOR = "http://news-at.zhihu.com/api/4/editor/%1$s/profile-page/android";

    String STORY_SHARE = "http://daily.zhihu.com/story/%1$s";

}
