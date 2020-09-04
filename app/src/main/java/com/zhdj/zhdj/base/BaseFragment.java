package com.zhdj.zhdj.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * @author : johnny
 * @date :   2019-05-13
 * @desc :   基类Fragment
 */
public abstract class BaseFragment extends Fragment {

    protected CompositeDisposable mCompositeDisposable;
    private Unbinder unbinder;
    protected Context mContext;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = null;
        if (getLayoutId() != 0) {
            rootView = inflater.inflate(getLayoutId(), container, false);
            unbinder = ButterKnife.bind(this, rootView);
        }
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = getActivity();
        initData();
        initView();
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
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onDestroy() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable.clear();
        }
        super.onDestroy();
    }

}
