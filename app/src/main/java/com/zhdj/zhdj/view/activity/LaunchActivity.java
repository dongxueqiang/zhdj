package com.zhdj.zhdj.view.activity;

import android.app.admin.DevicePolicyManager;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.DeviceUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.zhdj.zhdj.R;
import com.zhdj.zhdj.base.BaseActivity;
import com.zhdj.zhdj.view.receiver.ScreenOffAdminReceiver;
import com.zhdj.zhdj.view.service.GetSkinService;
import com.zhdj.zhdj.view.service.GetTimeService;
import com.zhdj.zhdj.viewmodel.MainViewModel;

import butterknife.BindView;

/**
 * @author : johnny
 * @date :   2019-05-17
 * @desc :   启动页
 */
public class LaunchActivity extends BaseActivity {

    private MainViewModel mMainViewModel;
    private ComponentName adminReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected void initData() {
        mMainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mMainViewModel.isPaiModel.observe(this, ab -> {
            if (ab == 1) {
//                startActivity(new Intent(this, ShowMessageActivity.class));
                startActivity(new Intent(this, ShowMessageNewActivity.class));
            } else if (ab == 0) {
                startActivity(new Intent(this, MainActivity.class));
            } else {
                startActivity(new Intent(this, ShowMessageNewActivity.class));
            }
            this.finish();
        });
//        tv1.setText(DeviceUtils.getMacAddress());
//        tv2.setText(NetworkUtils.getIpAddressByWifi());
//        adminReceiver = new ComponentName(LaunchActivity.this, ScreenOffAdminReceiver.class);
        startService(new Intent(this, GetTimeService.class));
        startService(new Intent(this, GetSkinService.class));
        PermissionUtils
                .permission(PermissionConstants.STORAGE)
                .callback(new PermissionUtils.SimpleCallback() {
                    @Override
                    public void onGranted() {
//                        getLatestVersion();
                        mMainViewModel.isPaibo();
                    }

                    @Override
                    public void onDenied() {
                        new AlertDialog.Builder(LaunchActivity.this)
                                .setTitle("提示")
                                .setMessage("您拒绝了某些权限请求，部分功能将无法使用，前往设置中开启？")
                                .setPositiveButton("确定", (dialog, which) -> AppUtils.launchAppDetailsSettings())
                                .show();
                    }
                })
                .request();
    }

    @Override
    protected void initView() {

    }

    /**
     * @param view 检测并去激活设备管理器权限
     */
    public void checkAndTurnOnDeviceManager(View view) {
        DevicePolicyManager mDPM = (DevicePolicyManager) this.getSystemService(Context.DEVICE_POLICY_SERVICE);
        if (!mDPM.isAdminActive(adminReceiver)) {
            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, adminReceiver);
            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "开启后就可以使用锁屏功能了...");//显示位置见图二
            startActivityForResult(intent, 0);
        }
    }
}
