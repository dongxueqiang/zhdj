package com.zhdj.zhdj.base;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

/**
 * @author : johnny
 * @date :   2019-05-13
 * @desc :
 */
public class BaseViewModel extends AndroidViewModel {
    public BaseViewModel(@NonNull Application application) {
        super(application);
    }
}
