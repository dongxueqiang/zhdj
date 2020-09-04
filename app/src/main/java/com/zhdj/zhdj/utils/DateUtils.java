package com.zhdj.zhdj.utils;

import com.blankj.utilcode.util.TimeUtils;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

/**
 * @ClassName DateUtils
 * @Author dongxueqiang
 * @Date 2020/7/14 20:04
 * @Title
 */
public class DateUtils {
    /**
     * 精确到秒的完整时间    HH:mm:ss
     */
    public static String FORMAT_FULL = "HH:mm:ss";

    /*获取星期几*/
    public static String getWeek() {
        Calendar cal = Calendar.getInstance();
        int i = cal.get(Calendar.DAY_OF_WEEK);
        switch (i) {
            case 1:
                return "星期日";
            case 2:
                return "星期一";
            case 3:
                return "星期二";
            case 4:
                return "星期三";
            case 5:
                return "星期四";
            case 6:
                return "星期五";
            case 7:
                return "星期六";
            default:
                return "";
        }
    }

    public static int getHour(String time, String dateForm) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(TimeUtils.string2Date(time, new SimpleDateFormat(dateForm)));
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    public static int getMinute(String time, String dateForm) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(TimeUtils.string2Date(time, new SimpleDateFormat(dateForm)));
        return calendar.get(Calendar.MINUTE);
    }

}
