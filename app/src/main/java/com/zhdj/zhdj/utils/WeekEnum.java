package com.zhdj.zhdj.utils;

/**
 * @ClassName WeekEnum
 * @Description TODO
 * @Author dongxueqiang
 * @Date 2020/9/5 7:55 PM
 */
public enum WeekEnum {
    SUN("星期日", 1),

    MON("星期一", 2),

    TUE("星期二", 3),

    WED("星期三", 4),

    THU("星期四", 5),

    FRI("星期五", 6),

    SAT("星期六", 7);

    private String week;
    private int day;

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    WeekEnum(String week, int day) {
        this.week = week;
        this.day = day;
    }


    public static WeekEnum getEnum(String week) {
        for (WeekEnum bt : values()) {
            if (bt.week.equals(week)) {
                return bt;
            }
        }
        return null;
    }
}
