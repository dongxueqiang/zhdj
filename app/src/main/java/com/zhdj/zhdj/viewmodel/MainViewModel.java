package com.zhdj.zhdj.viewmodel;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.view.View;

import com.blankj.utilcode.util.SPUtils;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.zhdj.zhdj.base.BaseViewModel;
import com.zhdj.zhdj.model.BaseModel;
import com.zhdj.zhdj.model.MenuModel;
import com.zhdj.zhdj.model.MessageModel;
import com.zhdj.zhdj.model.SkinModel;
import com.zhdj.zhdj.retrofit.RetrofitUtils;
import com.zhdj.zhdj.rxjava.BaseObserver;
import com.zhdj.zhdj.rxjava.CommonSchedulers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @ClassName MainViewModel
 * @Author dongxueqiang
 * @Date 2020/7/14 19:04
 * @Title
 */
public class MainViewModel extends BaseViewModel {
    public MutableLiveData<SkinModel> skinModel = new MutableLiveData<>();
    public MutableLiveData<List<MenuModel>> menuList = new MutableLiveData<>();
    public MutableLiveData<List<MessageModel>> msgList = new MutableLiveData<>();
    public MutableLiveData<MessageModel> msgDetail = new MutableLiveData<>();
    public MutableLiveData<Integer> isPaiModel = new MutableLiveData<>();

    public MainViewModel(@NonNull Application application) {
        super(application);
    }

    public void isPaibo() {
        RetrofitUtils.getApiService()
                .isPaibo()
                .compose(CommonSchedulers.observableIo2Main())
                .subscribe(new BaseObserver<Integer>() {
                    @Override
                    protected void onSuccess(Integer data) {
                        isPaiModel.setValue(data);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        isPaiModel.setValue(-1);
                    }

                    @Override
                    protected void onFailure(BaseModel<Integer> model) {
                        super.onFailure(model);
                        isPaiModel.setValue(-1);
                    }
                });
    }

    public void getSkin() {
        RetrofitUtils.getApiService()
                .getSkin()
                .compose(CommonSchedulers.observableIo2Main())
                .subscribe(new BaseObserver<SkinModel>() {
                    @Override
                    protected void onSuccess(SkinModel data) {
                        skinModel.setValue(data);
                    }
                });
    }

    public void getMainMenu() {
        RetrofitUtils.getApiService()
                .getMainMenu()
                .compose(CommonSchedulers.observableIo2Main())
                .subscribe(new BaseObserver<List<MenuModel>>() {
                    @Override
                    protected void onSuccess(List<MenuModel> data) {
                        menuList.setValue(data);
                    }
                });
    }

    public void getMessage(int module_id, int page, int limit) {
        Map<String, Object> map = new HashMap<>();
        map.put("s", "App.Module.ModuleResourcesList");
        map.put("module_id", module_id);
        map.put("page", page);
        map.put("limit", limit);
        RetrofitUtils.getApiService()
                .getMessage(map)
                .compose(CommonSchedulers.observableIo2Main())
                .subscribe(new BaseObserver<List<MessageModel>>() {
                    @Override
                    protected void onSuccess(List<MessageModel> data) {
                        msgList.setValue(data);
                    }
                });
    }

    public void getMessageDetail(long id) {
        Map<String, Object> map = new HashMap<>();
        map.put("s", "App.Module.DetailResources");
        map.put("id", id);
        RetrofitUtils.getApiService()
                .getMessageDetail(map)
                .compose(CommonSchedulers.observableIo2Main())
                .subscribe(new BaseObserver<MessageModel>() {
                    @Override
                    protected void onSuccess(MessageModel data) {
                        msgDetail.setValue(data);
                    }
                });
    }

    public void setPlayTerminal(long id) {
        Map<String, Object> map = new HashMap<>();
        map.put("s", "App.Terminal.PlayTerminal");
        map.put("id", id);
        RetrofitUtils.getApiService()
                .setPlayTerminal(map)
                .compose(CommonSchedulers.observableIo2Main())
                .subscribe(new BaseObserver<Object>() {
                    @Override
                    protected void onSuccess(Object data) {

                    }
                });
    }

}
