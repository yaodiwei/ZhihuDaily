package com.yao.zhihudaily.util;

import java.util.Calendar;

/**
 * Created by Administrator on 2016/8/18.
 */
public class DateUtil {

    /**
     * 获取currentDate前一天的日期字符串
     * @param currentDate
     */
    public static String dateMinus1(String currentDate) {
        Calendar c = Calendar.getInstance();
        int year = Integer.parseInt(currentDate.substring(0, 4));
        int month = Integer.parseInt(currentDate.substring(4, 6));
        int date = Integer.parseInt(currentDate.substring(6, 8));
        c.set(year, month-1, date);
        c.add(Calendar.DATE, -1);
        String yyyy = String.valueOf(c.get(Calendar.YEAR));
        String MM = c.get(Calendar.MONTH)<9 ? "0"+String.valueOf(c.get(Calendar.MONTH) + 1) : String.valueOf(c.get(Calendar.MONTH) + 1);
        String dd = c.get(Calendar.DAY_OF_MONTH)<10 ? "0"+String.valueOf(c.get(Calendar.DAY_OF_MONTH)) : String.valueOf(c.get(Calendar.DAY_OF_MONTH));
        return yyyy + MM + dd;
    }
}
