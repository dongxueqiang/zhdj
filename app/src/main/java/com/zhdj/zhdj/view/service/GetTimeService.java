package com.zhdj.zhdj.view.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;

import com.zhdj.zhdj.global.MyRequestCode;
import com.zhdj.zhdj.model.BaseModel;
import com.zhdj.zhdj.model.TimeModel;
import com.zhdj.zhdj.retrofit.RetrofitUtils;
import com.zhdj.zhdj.rxjava.BaseObserver;
import com.zhdj.zhdj.rxjava.CommonSchedulers;
import com.zhdj.zhdj.utils.DateUtils;
import com.talents.igallery.platform.GalleryNative;
import com.zhdj.zhdj.view.receiver.AlarmReceiver;

import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;


/**
 * @ClassName GetTimeService
 * @Author dongxueqiang
 * @Date 2020/7/16 20:09
 * @Title //获取开关机的服务
 */
public class GetTimeService extends Service {
    private Calendar mCalendar;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //执行获取开关机的接口
                RetrofitUtils.getApiService()
                        .getTime()
                        .compose(CommonSchedulers.observableIo2Main())
                        .subscribe(new BaseObserver<List<TimeModel>>() {
                            @Override
                            protected void onSuccess(List<TimeModel> data) {
                                if (data.size() == 2) {
                                    for (int i = 0; i < data.size(); i++) {
                                        TimeModel model = data.get(i);
                                        Log.i("www", "date type = " + model.getFunction_type());
                                        if (model.getFunction_type() == 0) {
                                            //关机
                                            setScreenState(MyRequestCode.INTENT_ALARM_COLSE, MyRequestCode.CLOSE, model);
                                        } else if (model.getFunction_type() == 1) {
                                            //开机
                                            setScreenState(MyRequestCode.INTENT_ALARM_OPEN, MyRequestCode.OPEN, model);
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.i("www", "error");
                            }

                            @Override
                            protected void onFailure(BaseModel<List<TimeModel>> model) {
                                Log.i("www", "onFailure");

                            }

                            @Override
                            protected boolean showExceptionMsg() {
                                return false;
                            }

                            @Override
                            protected boolean showErrorMsg() {
                                return false;
                            }
                        });

            }
        }).start();
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int anHour = 1 * 30 * 1000;  // 这是一小时的毫秒数
        long triggerAtTime = SystemClock.elapsedRealtime() + anHour;
        Intent i = new Intent(this, AlarmReceiver.class);
        i.setAction(MyRequestCode.INTENT_ALARM_TIME);
        PendingIntent pi = PendingIntent.getBroadcast(this, MyRequestCode.GET_TIME, i, 0);
        manager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
        return super.onStartCommand(intent, flags, startId);
    }

    private void setScreenState(String intentAlarmLog, int i, TimeModel timeModel) {
        //得到日历实例，主要是为了下面的获取时间
        mCalendar = Calendar.getInstance();
        mCalendar.setTimeInMillis(System.currentTimeMillis());
        mCalendar.set(Calendar.DAY_OF_MONTH, 8);
        //获取当前毫秒值
        long systemTime = System.currentTimeMillis();

        //是设置日历的时间，主要是让日历的年月日和当前同步
        mCalendar.setTimeInMillis(System.currentTimeMillis());
//        // 这里时区需要设置一下，不然可能个别手机会有8个小时的时间差
        mCalendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        if (timeModel.getType() == 1) {

        } else if (timeModel.getType() == 2) {
            //每星期
            mCalendar.set(Calendar.DAY_OF_WEEK, DateUtils.getDayByWeek(timeModel.getDay()));
        } else if (timeModel.getType() == 3) {
            //每月
            mCalendar.set(Calendar.DAY_OF_MONTH, DateUtils.getDayByString(timeModel.getDay()));
        }

        //设置在几点提醒  设置的为13点
        mCalendar.set(Calendar.HOUR_OF_DAY, DateUtils.getHour(timeModel.getFunction_time(), DateUtils.FORMAT_FULL));
        //设置在几分提醒  设置的为25分
        mCalendar.set(Calendar.MINUTE, DateUtils.getMinute(timeModel.getFunction_time(), DateUtils.FORMAT_FULL));
        //下面这两个看字面意思也知道
        mCalendar.set(Calendar.SECOND, 0);
        mCalendar.set(Calendar.MILLISECOND, 0);

        long selectTime = mCalendar.getTimeInMillis();
        // 如果当前时间大于设置的时间，那么就从第二天的设定时间开始
        if (systemTime > selectTime) {
            if (timeModel.getType() == 1) {
                mCalendar.add(Calendar.DAY_OF_MONTH, 1);
            } else if (timeModel.getType() == 2) {
                //每星期
                mCalendar.add(Calendar.DAY_OF_MONTH, 7);
            } else if (timeModel.getType() == 3) {
                //每月
                mCalendar.add(Calendar.MONTH, 1);
            }
        }
        selectTime = mCalendar.getTimeInMillis();
        if (i == MyRequestCode.CLOSE) {
            //AlarmReceiver.class为广播接受者
            Intent intent = new Intent(this, AlarmReceiver.class);
            intent.setAction(intentAlarmLog);
            PendingIntent pi = PendingIntent.getBroadcast(this, i, intent, 0);
            //得到AlarmManager实例
            AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
            /**
             * 重复提醒
             * 第一个参数是警报类型；下面有介绍
             * 第二个参数网上说法不一，很多都是说的是延迟多少毫秒执行这个闹钟，
             * 第三个参数是重复周期，也就是下次提醒的间隔 毫秒值 我这里是一天后提醒
             */
            am.setExact(AlarmManager.RTC_WAKEUP, selectTime, pi);
//            Log.i("www", "关机时间 = " + TimeUtils.millis2String(selectTime, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")));
        } else {
//            Log.i("www", "开机时间 = " + TimeUtils.millis2String(selectTime, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")));
            GalleryNative.setOnTime_RTC(selectTime);
        }
//        am.setRepeating(AlarmManager.RTC_WAKEUP, mCalendar.getTimeInMillis(), (1000 * 60 * 60 * 24), pi);
    }

}
