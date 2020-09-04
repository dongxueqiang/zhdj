package com.zhdj.zhdj.base;

import android.support.v7.app.AlertDialog;

/**
 * @author : johnny
 * @date :   2019-05-13
 * @desc :   带有返回弹框提示的Activity基类
 */
public abstract class BaseReturnHintActivity extends BaseActivity {

    private AlertDialog.Builder mDialog;

    @Override
    protected void initView() {
        mDialog = new AlertDialog.Builder(this);
        mDialog.setTitle("提示")
                .setMessage("确定退出吗？")
                .setPositiveButton("确定", (dialog, which) -> onConfirmClicked())
                .setNegativeButton("取消", null);
    }

    @Override
    public void onBackPressed() {
        showHintDialog();
    }

    private void showHintDialog() {
        if (mDialog != null) {
            mDialog.show();
        }
    }

    public void onConfirmClicked() {
        finish();
    }

    @Override
    protected void onBackButtonClick() {
        showHintDialog();
    }
}
