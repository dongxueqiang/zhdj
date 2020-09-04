package com.zhdj.zhdj.view.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.jeremyliao.liveeventbus.LiveEventBus;
import com.zhdj.zhdj.event.LiveEvent;
import com.zhdj.zhdj.global.MyRequestCode;
import com.zhdj.zhdj.model.LiveMessageModel;
import com.zhdj.zhdj.model.MessageModel;
import com.zhdj.zhdj.model.TimeModel;
import com.zhdj.zhdj.retrofit.RetrofitUtils;
import com.zhdj.zhdj.rxjava.BaseObserver;
import com.zhdj.zhdj.rxjava.CommonSchedulers;
import com.zhdj.zhdj.view.receiver.AlarmReceiver;

import org.json.JSONArray;

import java.util.List;


/**
 * @ClassName GetMessageService
 * @Author dongxueqiang
 * @Date 2020/7/16 19:15
 * @Title
 */
public class GetMessageService extends Service {

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
//                Log.e("api", "getMessage");
                //执行获取排播信息的接口
                RetrofitUtils.getApiService()
                        .getPaibo()
                        .compose(CommonSchedulers.observableIo2Main())
                        .subscribe(new BaseObserver<LiveMessageModel>() {
                            @Override
                            protected void onSuccess(LiveMessageModel data) {
                                if (data != null && data.getList().size()!=0) {
//                                    if (data.get)
                                    LiveEventBus.get(LiveEvent.REFRESH_MESSAGE).post(data);
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
        int anHour = 1 * 3000 * 1000;  //
        long triggerAtTime = SystemClock.elapsedRealtime() + anHour;
        Intent i = new Intent(this, AlarmReceiver.class);
        i.setAction(MyRequestCode.INTENT_ALARM_MESSAGE);
        PendingIntent pi = PendingIntent.getBroadcast(this, MyRequestCode.MESSAGE, i, 0);
        manager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
        return super.onStartCommand(intent, flags, startId);
    }
}