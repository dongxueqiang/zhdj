package com.zhdj.zhdj.base;

import android.app.Application;
import android.content.Context;

/**
 * @author : johnny
 * @date :   2019-05-13
 * @desc :
 */
public class BaseApplication extends Application {

    private static Application mApplication;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
    }

    public static Context getAppContext() {
        return mApplication.getApplicationContext();
    }
}
