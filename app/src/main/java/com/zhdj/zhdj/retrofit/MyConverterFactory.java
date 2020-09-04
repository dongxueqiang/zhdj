package com.zhdj.zhdj.retrofit;

import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.EncryptUtils;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.zhdj.zhdj.base.BaseApplication;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;


/**
 * @author : johnny
 * @date :   2019-05-14
 * @desc :
 */
public class MyConverterFactory extends Converter.Factory {

    private static byte[] key = MyEncryptionUtils.getAesKey(BaseApplication.getAppContext()).getBytes();
//    private static byte[] key = MyEncryptionUtils.getAesKey().getBytes();
    private static String transformation = "AES/ECB/PKCS5Padding";

    public static MyConverterFactory create() {
        return create(new Gson());
    }

    private static MyConverterFactory create(Gson gson) {
        return new MyConverterFactory(gson);
    }

    private final Gson gson;

    private MyConverterFactory(Gson gson) {
        if (gson == null) {
            throw new NullPointerException("gson == null");
        }
        this.gson = gson;
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
        return new DecodeResponseBodyConverter<>(adapter);
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
        return new EncodeRequestBodyConverter(gson, adapter);
    }


    static class EncodeRequestBodyConverter<T> implements Converter<T, RequestBody> {
        private Gson gson;
        private TypeAdapter<T> adapter;

        public EncodeRequestBodyConverter(TypeAdapter<T> adapter) {
            this.adapter = adapter;
        }

        public EncodeRequestBodyConverter(Gson gson, TypeAdapter<T> adapter) {
            this.gson = gson;
            this.adapter = adapter;
        }

        @Override
        public RequestBody convert(T o) throws IOException {
            byte[] data = adapter.toJson(o).getBytes();
            byte[] content = EncryptUtils.encryptAES(data, key, transformation, null);
            return RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), content);
        }
    }

    static class DecodeResponseBodyConverter<T> implements Converter<ResponseBody, T> {
        private TypeAdapter<T> adapter;
        private Gson gson;

        public DecodeResponseBodyConverter(TypeAdapter<T> adapter) {
            this.adapter = adapter;
        }

        public DecodeResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
            this.gson = gson;
            this.adapter = adapter;
        }

        @Override
        public T convert(ResponseBody responseBody) throws IOException {
            byte[] content = EncryptUtils.decryptAES(responseBody.bytes(), key, transformation, null);
            return adapter.fromJson(ConvertUtils.bytes2HexString(content));
        }
    }
}
