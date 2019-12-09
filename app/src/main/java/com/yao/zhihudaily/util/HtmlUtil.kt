package com.yao.zhihudaily.util


import com.yao.zhihudaily.model.DailyJson
import java.util.*

/**
 * @author Yao
 * @date 2016/8/1
 */
object HtmlUtil {

    /**
     * css样式,隐藏header
     */
    private val HIDE_HEADER_STYLE = "<style>div.headline{display:none;}</style>"

    /**
     * css style tag,需要格式化
     */
    private val NEEDED_FORMAT_CSS_TAG = "<link rel=\"stylesheet\" type=\"text/css\" href=\"%s\"/>"

    /**
     * js script tag,需要格式化
     */
    private val NEEDED_FORMAT_JS_TAG = "<script src=\"%s\"></script>"

    val MIME_TYPE = "text/html; charset=utf-8"

    val ENCODING = "utf-8"

    /**
     * 根据css链接生成Link标签
     *
     * @param url String
     * @return String
     */
    fun createCssTag(url: String): String {

        return String.format(NEEDED_FORMAT_CSS_TAG, url)
    }

    /**
     * 根据多个css链接生成Link标签
     *
     * @param urls ArrayList<String>
     * @return String
    </String> */
    fun createCssTag(urls: ArrayList<String>): String {

        val sb = StringBuilder()
        for (url in urls) {
            sb.append(createCssTag(url))
        }
        return sb.toString()
    }

    /**
     * 根据js链接生成Script标签
     *
     * @param url String
     * @return String
     */
    fun createJsTag(url: String): String {

        return String.format(NEEDED_FORMAT_JS_TAG, url)
    }

    /**
     * 根据多个js链接生成Script标签
     *
     * @param urls ArrayList<String>
     * @return String
    </String> */
    fun createJsTag(urls: ArrayList<String>): String {

        val sb = StringBuilder()
        for (url in urls) {
            sb.append(createJsTag(url))
        }
        return sb.toString()
    }

    /**
     * 根据样式标签,html字符串,js标签
     * 生成完整的HTML文档
     *
     * @param html string
     * @param css  string
     * @param js   string
     * @return string
     */
    private fun createHtmlData(html: String, css: String, js: String): String {

        return css + HIDE_HEADER_STYLE + html + js
    }

    /**
     * 生成完整的HTML文档
     *
     * @return String
     */
    fun createHtmlData(detail: DailyJson): String {

        val css = HtmlUtil.createCssTag(detail.css!!)
        val js = HtmlUtil.createJsTag(detail.js!!)
        return createHtmlData(detail.body!!, css, js)
    }
}
