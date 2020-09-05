package com.zhdj.zhdj.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.LogUtils;
import com.talents.igallery.platform.SysPropUtils;

import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * @author : johnny
 * @date :   2019-05-13
 * @desc :   Activity基类
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected CompositeDisposable mCompositeDisposable;
    private TextView tvTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getLayoutId() != 0) {
            setContentView(getLayoutId());
            ButterKnife.bind(this);
        }
        SysPropUtils.set(SysPropUtils.PROP_STOP_ANIM, "1");
        initData();
        initView();
        KeyboardUtils.hideSoftInput(this);
        LogUtils.i("xxx:" + getClass().getName());
    }


    protected void onBackButtonClick() {
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    protected abstract int getLayoutId();

    protected abstract void initData();

    protected abstract void initView();

    protected void addDisposable(Disposable disposable) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(disposable);
    }

    @Override
    protected void onDestroy() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable.clear();
        }
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

}
