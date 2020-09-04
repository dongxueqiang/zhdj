package com.zhdj.zhdj.global;

/**
 * @ClassName MyRequestCode
 * @Author dongxueqiang
 * @Date 2020/7/16 19:10
 * @Title
 */
public interface MyRequestCode {
    int GET_SKIN = 1;
    int xiping = 2;
    int liangping = 3;
    int GET_TIME = 4;
    int OPEN = 5;
    int CLOSE = 6;
    int MESSAGE = 7;

    String INTENT_ALARM_COLSE = "INTENT_ALARM_COLSE";
    String INTENT_ALARM_OPEN = "INTENT_ALARM_OPEN";
    //获取手机皮肤
    String INTENT_ALARM_SKIN = "INTENT_ALARM_SKIN";
    //继续获取手机排播信息
    String INTENT_ALARM_MESSAGE = "INTENT_ALARM_MESSAGE";
    //获取开关机时间
    String INTENT_ALARM_TIME = "INTENT_ALARM_TIME";
}
