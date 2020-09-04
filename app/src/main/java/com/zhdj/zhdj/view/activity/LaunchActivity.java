package com.zhdj.zhdj.view.activity;

import android.app.admin.DevicePolicyManager;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.DeviceUtils;
import com.blankj.utilcode.util.NetworkUtils;
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
//        tv1.setText(DeviceUtils.getMacAddress());
//        tv2.setText(NetworkUtils.getIpAddressByWifi());
//        adminReceiver = new ComponentName(LaunchActivity.this, ScreenOffAdminReceiver.class);
//        startService(new Intent(this, GetTimeService.class));
//        startService(new Intent(this, GetSkinService.class));
        mMainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mMainViewModel.isPaibo();
        mMainViewModel.isPaiModel.observe(this, ab -> {
            if (ab) {
//                startActivity(new Intent(this, ShowMessageActivity.class));
                startActivity(new Intent(this, ShowMessageNewActivity.class));
            } else {
                startActivity(new Intent(this, MainActivity.class));
            }
            this.finish();
        });

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
