package com.zhdj.zhdj.rxjava;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.zhdj.zhdj.global.StatusCode;
import com.zhdj.zhdj.model.BaseModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import retrofit2.HttpException;

/**
 * @author : johnny
 * @date :   2019-05-16
 * @desc :
 */
public abstract class BaseObserver<T> implements Observer<BaseModel<T>> {

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(BaseModel<T> model) {
        if (model.getRet() == StatusCode.SUCCESS) {
            onSuccess(model.getData());
        } else {
            if (showErrorMsg()) {
                if (!TextUtils.isEmpty(model.getMsg())) {
                    ToastUtils.showLong(model.getMsg());
                }
            }
            onFailure(model);
        }
    }

    @Override
    public void onError(Throwable e) {
        LogUtils.e(e);
        if (e instanceof ConnectException) {
            NetworkUtils.isAvailableAsync(available -> {
                if (!available) {
                    showNetworkExceptionDialog("网络异常，请检查手机网络设置");
                } else {
                    ToastUtils.showLong("连接服务器异常，请稍后再试");
                }
            });
        } else if (e instanceof SocketTimeoutException) {
            ToastUtils.showLong("连接服务器超时，请稍后再试");
        } else if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            String error = null;
            try {
                error = httpException.response().errorBody().string();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            try {
                JSONObject jsonObject = new JSONObject(error);
                if (jsonObject.has("msg")) {
                    String msg = jsonObject.getString("msg");
                    if (!TextUtils.isEmpty(msg)) {
                        ToastUtils.showLong(msg);
                    }
                } else if (jsonObject.has("message")) {
                    ToastUtils.showLong("系统正在维护，请稍后再试");
                }
            } catch (JSONException e1) {
                e1.printStackTrace();
            }

        } else if (showExceptionMsg()) {
//            if (!TextUtils.isEmpty(e.getMessage())) {
//                ToastUtils.showLong(e.getMessage());
//            }
        }
        onException(e);
    }

    private void showNetworkExceptionDialog(String msg) {
        new AlertDialog.Builder(ActivityUtils.getTopActivity())
                .setTitle("提示")
                .setMessage(msg)
                .setPositiveButton("确定", null)
                .show();
    }

    @Override
    public void onComplete() {

    }


    /**
     * 进入点播
     */
    protected void openDianbo() {
    }

    /**
     * 进入排播
     */
    protected void openPaibo() {
    }

    /**
     * 接口调用成功
     *
     * @param data
     */
    protected abstract void onSuccess(T data);

    /**
     *
     */
    /**
     * 接口失败回调，通常不需要有逻辑
     */
    protected void onFailure(BaseModel<T> model) {
    }

    /**
     * 异常回调，通常不需要有逻辑
     *
     * @param t
     */
    protected void onException(Throwable t) {
    }

    /**
     * 开关方法，用于子类控制是否展示错误消息，默认展示
     */
    protected boolean showErrorMsg() {
        return true;
    }

    /**
     * 开关方法，用于子类控制是否展示异常消息，默认展示
     */
    protected boolean showExceptionMsg() {
        return true;
    }
}
