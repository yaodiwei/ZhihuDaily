package com.yao.zhihudaily.util

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author Yao
 * @date 2016/8/18
 */
object DateUtil {

    var dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

    /**
     * 获取currentDate前一天的日期字符串
     *
     * @param currentDate 当前日期
     */
    fun dateMinus1(currentDate: String): String {
        val c = Calendar.getInstance()
        val year = Integer.parseInt(currentDate.substring(0, 4))
        val month = Integer.parseInt(currentDate.substring(4, 6))
        val date = Integer.parseInt(currentDate.substring(6, 8))
        c.set(year, month - 1, date)
        c.add(Calendar.DATE, -1)
        val yyyy = c.get(Calendar.YEAR).toString()
        val MM = if (c.get(Calendar.MONTH) < 9) "0" + (c.get(Calendar.MONTH) + 1) else (c.get(Calendar.MONTH) + 1).toString()
        val dd = if (c.get(Calendar.DAY_OF_MONTH) < 10) "0" + c.get(Calendar.DAY_OF_MONTH) else c.get(Calendar.DAY_OF_MONTH).toString()
        return yyyy + MM + dd
    }

    fun format(time: Long): String {
        return dateFormat.format(time * 1000)
    }
}
