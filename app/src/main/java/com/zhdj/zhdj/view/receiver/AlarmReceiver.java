package com.zhdj.zhdj.view.receiver;

import android.annotation.SuppressLint;
import android.app.admin.DevicePolicyManager;
import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.util.Log;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.DeviceUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.zhdj.zhdj.global.MyRequestCode;
import com.zhdj.zhdj.model.MessageModel;
import com.zhdj.zhdj.retrofit.RetrofitUtils;
import com.zhdj.zhdj.rxjava.BaseObserver;
import com.zhdj.zhdj.rxjava.CommonSchedulers;
import com.zhdj.zhdj.view.activity.MainActivity;
import com.zhdj.zhdj.view.activity.SleepActivity;
import com.zhdj.zhdj.view.service.GetMessageService;
import com.zhdj.zhdj.view.service.GetSkinService;
import com.zhdj.zhdj.view.service.GetTimeService;
import com.zhdj.zhdj.viewmodel.MainViewModel;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName AlarmReceiver
 * @Author dongxueqiang
 * @Date 2020/7/13 19:52
 * @Title
 */
public class AlarmReceiver extends BroadcastReceiver {
    private DevicePolicyManager policyManager;
    private ComponentName adminReceiver;

    private PowerManager mPowerManager;
    private PowerManager.WakeLock mWakeLock;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
//        Log.i("www", "action = " + action);
        if (action == MyRequestCode.INTENT_ALARM_COLSE) {
//            Log.i("www", "我熄屏了");
            //设备使用状态上传
            shutdown(context);
            setTerminalStatus(0);
        } else if (action == MyRequestCode.INTENT_ALARM_OPEN) {

            //设备使用状态上传
            setTerminalStatus(1);
        } else if (action == MyRequestCode.INTENT_ALARM_TIME) {
            context.startService(new Intent(context, GetTimeService.class));
        } else if (action == MyRequestCode.INTENT_ALARM_MESSAGE) {
            context.startService(new Intent(context, GetMessageService.class));
        } else if (action == MyRequestCode.INTENT_ALARM_SKIN) {
            context.startService(new Intent(context, GetSkinService.class));
        }
    }

    /**
     * @param function_type 1=开机，0=关机
     */
    public void setTerminalStatus(int function_type) {
        Map<String, Object> map = new HashMap<>();
        map.put("s", "App.PublishFunction.FunctionNotice");
        map.put("function_type", function_type);
        RetrofitUtils.getApiService()
                .setTerminalStatus(map)
                .compose(CommonSchedulers.observableIo2Main())
                .subscribe(new BaseObserver<Object>() {
                    @Override
                    protected void onSuccess(Object data) {

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


    public void shutdown(Context context) {
        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        Class<PowerManager> clz = PowerManager.class;
        try {
            @SuppressLint("SoonBlockedPrivateApi") Method method = clz.getDeclaredMethod("shutdown", boolean.class, String.class, boolean.class);
            method.invoke(powerManager, false, null, false);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
