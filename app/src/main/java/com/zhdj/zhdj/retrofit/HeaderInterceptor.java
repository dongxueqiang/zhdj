package com.zhdj.zhdj.retrofit;

import com.blankj.utilcode.util.DeviceUtils;
import com.blankj.utilcode.util.NetworkUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author : johnny
 * @date :   2019-05-17
 * @desc :   添加统一的token请求头
 */
public class HeaderInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalReq = chain.request();

        Request newReq = originalReq.newBuilder()
//                .addHeader("Authorization", "123.110.112.11")
                .addHeader("Authorization", DeviceUtils.getMacAddress())
                .build();
        return chain.proceed(newReq);

    }
}
