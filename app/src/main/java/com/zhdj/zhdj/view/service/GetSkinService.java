package com.zhdj.zhdj.view.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import com.jeremyliao.liveeventbus.LiveEventBus;
import com.zhdj.zhdj.event.LiveEvent;
import com.zhdj.zhdj.global.MyRequestCode;
import com.zhdj.zhdj.model.SkinModel;
import com.zhdj.zhdj.retrofit.RetrofitUtils;
import com.zhdj.zhdj.rxjava.BaseObserver;
import com.zhdj.zhdj.rxjava.CommonSchedulers;
import com.zhdj.zhdj.view.receiver.AlarmReceiver;


/**
 * @ClassName GetSkinService
 * @Author dongxueqiang
 * @Date 2020/7/16 19:04
 * @Title
 */
public class GetSkinService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        new Thread(new Runnable() {
            @Override
            public void run() {
//                Log.i("www", "get skin");
                RetrofitUtils.getApiService()
                        .getSkin()
                        .compose(CommonSchedulers.observableIo2Main())
                        .subscribe(new BaseObserver<SkinModel>() {
                            @Override
                            protected void onSuccess(SkinModel data) {
//                                Log.i("www","a="+data.getBack_imgs_url());
//                                Log.i("www","b="+data.getLogo_url());
                                LiveEventBus.get(LiveEvent.REFRESH_SKIN).post(data);
                            }
                        });
            }
        }).start();
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int anHour = 1 * 60 * 1000; // 这是十分钟的毫秒数
        long triggerAtTime = SystemClock.elapsedRealtime() + anHour;
        Intent i = new Intent(this, AlarmReceiver.class);
        i.setAction(MyRequestCode.INTENT_ALARM_SKIN);
        PendingIntent pi = PendingIntent.getBroadcast(this, MyRequestCode.GET_SKIN, i, 0);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
        return super.onStartCommand(intent, flags, startId);
    }
}
