package com.zhdj.zhdj.retrofit;

import android.util.Log;

import com.zhdj.zhdj.api.ApiService;
import com.zhdj.zhdj.global.UrlConstant;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author : johnny
 * @date :   2019-05-13
 * @desc :
 */
public class RetrofitUtils {

    private static Retrofit mRetrofit;
    /**
     * 超时时长
     */
    private static int DURATION = 30 * 1000;

    private RetrofitUtils() {

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(message -> Log.e("api", message));
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(DURATION, TimeUnit.MILLISECONDS)
                .readTimeout(DURATION, TimeUnit.MILLISECONDS)
                .writeTimeout(DURATION, TimeUnit.MILLISECONDS)
                .addInterceptor(new HeaderInterceptor())
                .addInterceptor(loggingInterceptor)
                .build();

        mRetrofit = new Retrofit.Builder()
                .baseUrl(UrlConstant.API_BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                //自定义的Converter，实现加密解密
//                .addConverterFactory(MyConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
    }

    public static ApiService getApiService() {
        return getService(ApiService.class);
    }

    public static <T> T getService(Class<T> clazz) {
        if (mRetrofit == null) {
            new RetrofitUtils();
        }
        return mRetrofit.create(clazz);
    }

}
