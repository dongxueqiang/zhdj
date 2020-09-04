package com.zhdj.zhdj.view.receiver;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;


/**
 * @ClassName ScreenOffAdminReceiver
 * @Author dongxueqiang
 * @Date 2020/7/13 20:59
 * @Title
 */
public class ScreenOffAdminReceiver extends DeviceAdminReceiver {
    private void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEnabled(Context context, Intent intent) {
        showToast(context,
                "设备管理器使用");
    }

    @Override
    public void onDisabled(Context context, Intent intent) {
        showToast(context,
                "设备管理器没有使用");
    }
}
