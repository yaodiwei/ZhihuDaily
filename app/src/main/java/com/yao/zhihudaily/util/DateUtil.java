package com.yao.zhihudaily.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * @author Yao
 * @date 2016/8/18
 */
public class DateUtil {

    public static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

    /**
     * 获取currentDate前一天的日期字符串
     *
     * @param currentDate 当前日期
     */
    public static String dateMinus1(String currentDate) {
        Calendar c = Calendar.getInstance();
        int year = Integer.parseInt(currentDate.substring(0, 4));
        int month = Integer.parseInt(currentDate.substring(4, 6));
        int date = Integer.parseInt(currentDate.substring(6, 8));
        c.set(year, month - 1, date);
        c.add(Calendar.DATE, -1);
        String yyyy = String.valueOf(c.get(Calendar.YEAR));
        String MM = c.get(Calendar.MONTH) < 9 ? "0" + (c.get(Calendar.MONTH) + 1) : String.valueOf(c.get(Calendar.MONTH) + 1);
        String dd = c.get(Calendar.DAY_OF_MONTH) < 10 ? "0" + c.get(Calendar.DAY_OF_MONTH) : String.valueOf(c.get(Calendar.DAY_OF_MONTH));
        return yyyy + MM + dd;
    }

    public static String format(long time) {
        return dateFormat.format(time * 1000);
    }
}
