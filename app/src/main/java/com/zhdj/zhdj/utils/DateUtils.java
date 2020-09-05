package com.zhdj.zhdj.utils;

import android.text.TextUtils;

import com.blankj.utilcode.util.TimeUtils;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    /*获取星期几*/
    public static int getDayByWeek(String week) {
        WeekEnum weekEnum = WeekEnum.getEnum(week);
        return weekEnum.getDay();
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

    private static int string2Int(String s) {
        if (TextUtils.isEmpty(s)) {
            return 0;
        } else {
            return Integer.parseInt(s);
        }
    }

    public static int getDayByString(String str) {
        //用正则表达式
        String reg = "[^0-9]";
        //Pattern类的作用在于编译正则表达式后创建一个匹配模式.
        Pattern p = Pattern.compile(reg);
        //Matcher类使用Pattern实例提供的模式信息对正则表达式进行匹配
        Matcher m = p.matcher(str);
        String string = m.replaceAll("").trim();
        return string2Int(string);
    }

}
