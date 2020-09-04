package com.zhdj.zhdj.view.activity;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.zhdj.zhdj.R;
import com.zhdj.zhdj.view.receiver.ScreenOffAdminReceiver;


/**
 * @ClassName SleepActivity
 * @Author dongxueqiang
 * @Date 2020/7/13 21:03
 * @Title
 */
public class SleepActivity extends AppCompatActivity {
    private String tag = "MainActivity";
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    checkScreenOn(null);
                    break;
                case 2:

                    break;
            }
        }
    };
    private DevicePolicyManager policyManager;
    private ComponentName adminReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep);
        adminReceiver = new ComponentName(SleepActivity.this, ScreenOffAdminReceiver.class);
        mPowerManager = (PowerManager) getSystemService(POWER_SERVICE);
        policyManager = (DevicePolicyManager) SleepActivity.this.getSystemService(Context.DEVICE_POLICY_SERVICE);
        checkAndTurnOnDeviceManager(null);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        isOpen();
    }


    private void isOpen() {
        if (policyManager.isAdminActive(adminReceiver)) {//判断超级管理员是否激活
            showToast("设备已被激活");
        } else {
            showToast("设备没有被激活");

        }
    }
    private PowerManager mPowerManager;
    private PowerManager.WakeLock mWakeLock;


    /**
     * @param view 检测屏幕状态
     */
    public void checkScreen(View view) {
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        boolean screenOn = pm.isScreenOn();
        if (!screenOn) {//如果灭屏
            //相关操作
            showToast("屏幕是息屏");
        } else {
            showToast("屏幕是亮屏");

        }
    }


    /**
     * @param view 亮屏
     */
    public void checkScreenOn(View view) {
        mWakeLock = mPowerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, this.getClass().getName());
        mWakeLock.acquire();
        mWakeLock.release();
    }

    /**
     * @param view 熄屏
     */
    public void checkScreenOff(View view) {
        boolean admin = policyManager.isAdminActive(adminReceiver);
        if (admin) {
            policyManager.lockNow();
        } else {
            showToast("没有设备管理权限");
        }
    }

    /**
     * @param view 熄屏并延时亮屏
     */
    public void checkScreenOffAndDelayOn(View view) {
        boolean admin = policyManager.isAdminActive(adminReceiver);
        if (admin) {
            policyManager.lockNow();
            handler.sendEmptyMessageDelayed(1, 3000);
        } else {
            showToast("没有设备管理权限");
        }
    }

    /**
     * @param view 检测并去激活设备管理器权限
     */
    public void checkAndTurnOnDeviceManager(View view) {
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, adminReceiver);
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "开启后就可以使用锁屏功能了...");//显示位置见图二
        startActivityForResult(intent, 0);

    }


    private void showToast(String Str) {
        Toast.makeText(this, Str, Toast.LENGTH_SHORT).show();
    }
}
