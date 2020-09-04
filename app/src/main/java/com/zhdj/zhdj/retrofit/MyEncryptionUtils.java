package com.zhdj.zhdj.retrofit;

/**
 * @author : johnny
 * @date :   2019-05-14
 * @desc :
 */
public class MyEncryptionUtils {

    /**
     * 原生方法，获取AES加密密钥
     *
     * @return
     */
    public static native String getAesKey(Object context);

}
