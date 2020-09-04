package com.zhdj.zhdj.view.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.jeremyliao.liveeventbus.LiveEventBus;
import com.zhdj.zhdj.event.LiveEvent;
import com.zhdj.zhdj.global.MyRequestCode;
import com.zhdj.zhdj.model.LiveMessageModel;
import com.zhdj.zhdj.retrofit.RetrofitUtils;
import com.zhdj.zhdj.rxjava.BaseObserver;
import com.zhdj.zhdj.rxjava.CommonSchedulers;
import com.zhdj.zhdj.utils.MessageUtils;
import com.zhdj.zhdj.view.receiver.AlarmReceiver;

import java.util.Calendar;


/**
 * @ClassName GetMessageService
 * @Author dongxueqiang
 * @Date 2020/7/16 19:15
 * @Title
 */
public class GetMessageOldService extends Service {
    private Calendar mCalendar;
    private int second = 0;
    private int nowSecond;
    private int millieSecond = 0;
    private int nowMillieSecond;

    private boolean needSend;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("www", "get message onStartCommand");
        LiveMessageModel model = MessageUtils.getMessage();
        if (model != null) {
            LiveEventBus.get(LiveEvent.REFRESH_MESSAGE).post(model);
            needSend = false;
        } else {
            needSend = true;
        }
        mCalendar = Calendar.getInstance();
//        Log.i("www", "未设置 = " + TimeUtils.date2String(mCalendar.getTime(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.ss")));
        nowSecond = mCalendar.get(Calendar.SECOND);
        nowMillieSecond = mCalendar.get(Calendar.MILLISECOND);
//        Log.i("www", "now = " + nowSecond);
        new Thread(new Runnable() {
            @Override
            public void run() {

                //执行获取排播信息的接口
                RetrofitUtils.getApiService()
                        .getPaibo()
                        .compose(CommonSchedulers.observableIo2Main())
                        .subscribe(new BaseObserver<LiveMessageModel>() {
                            @Override
                            protected void onSuccess(LiveMessageModel data) {
//                                Log.i("www", "上次 = " + second);
                                if (data != null) {
                                    if (0 != second && 0 != millieSecond) {
                                        data.setIs_change(1);
                                    }
                                    second = nowSecond;
                                    millieSecond = nowMillieSecond;
//                                    Log.i("www", "这次 = " + second);
                                    if (needSend) {
                                        LiveEventBus.get(LiveEvent.REFRESH_MESSAGE).post(model);
                                    } else {
                                        MessageUtils.saveMessage(data);
                                    }
//                                    Log.i("www", "is change = " + data.getIs_change());
                                }
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

        mCalendar.set(Calendar.MINUTE, mCalendar.get(Calendar.MINUTE) + 1);
        mCalendar.set(Calendar.SECOND, 0);
        mCalendar.set(Calendar.MILLISECOND, 0);
//        mCalendar.set(mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH)
//                , mCalendar.get(Calendar.HOUR_OF_DAY), mCalendar.get(Calendar.MINUTE) + 1, 1);
        Intent i = new Intent(this, AlarmReceiver.class);
        i.setAction(MyRequestCode.INTENT_ALARM_MESSAGE);
        PendingIntent pi = PendingIntent.getBroadcast(this, MyRequestCode.MESSAGE, i, 0);
//        manager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
//                Log.i("www", "设置后 = " + TimeUtils.date2String(mCalendar.getTime(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.ss")));

        manager.setExact(AlarmManager.RTC_WAKEUP, mCalendar.getTimeInMillis(), pi);
        return super.onStartCommand(intent, flags, startId);
    }

}
