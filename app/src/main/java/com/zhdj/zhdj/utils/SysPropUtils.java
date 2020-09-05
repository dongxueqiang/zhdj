package com.zhdj.zhdj.utils;

import android.util.Log;

import java.lang.reflect.Method;

/**
 * Created by User on 2015/6/30.
 */
public class SysPropUtils {

    private static final String TAG = "MySystemProperties";
    public final static String PROP_IN_LAUNCHER = "persist.sys.BOE_IN_LAUNCHER";
    public final static String PROP_IN_AT = "ro.boot.AT_K";
    public final static String PROP_TOP_PKG = "persist.sys.current.apk";
    public final static String PROP_FTI_STEP = "persist.sys.fti_step";
    public final static String PROP_OTA_STEP = "persist.sys.ota_step";
    public final static String PROP_STOP_ANIM = "persist.sys.exit_anim";
    public final static String PROP_STR = "ro.CVTE_EN_STR";
    public final static String PROP_AGING_FLAG = "persist.sys.AgingMode_T960";
    private static boolean isSTR = false;
    static {
        isSTR = get(PROP_STR, "0").equals("1");
    }

    // String SystemProperties.get(String key){}
    public static void set(String key, String val) {
        init();

        try {
            mSetMethod.invoke(mClassType, key, val);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // String SystemProperties.get(String key){}
    public static String get(String key, String def) {
        init();

        String value = null;

        try {
            value = (String) mGetMethod.invoke(mClassType, key, def);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return value;
    }

    //int SystemProperties.get(String key, int def){}
    public static int getInt(String key, int def) {
        init();

        int value = def;
        try {
            value = (Integer) mGetIntMethod.invoke(mClassType, key, def);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    public static int getSdkVersion() {
        return getInt("ro.build.version.sdk", -1);
    }

    public static boolean isInLauncher() {
        return get(PROP_IN_LAUNCHER, "true").equals("true");
    }

    public static boolean isInAT() {
        return getInt(PROP_IN_AT, 0) == 1;
    }
    public static String getTopActivity() {
        return get(PROP_TOP_PKG, "");
    }

    public static void stopBootAnimation() {
        set(PROP_STOP_ANIM, "1");
    }

    /**
     * 0->finish, 1->in fti, 2->has bound
     */
    public static void setFTIStep(int step){
        set(PROP_FTI_STEP, "" + step);
    }

    public static int getFTIStep() {
        return getInt(PROP_FTI_STEP, 0);
    }

    /**
     *0->no upgrade, 1->wait to upgrade, 2->wait to delete
     */
    public static void setOTAStep(int step) {
        set(PROP_OTA_STEP, step + "");
    }

    public static int getOTAStep() {
        return getInt(PROP_OTA_STEP, 0);
    }

    public static boolean isSTR() {
        return isSTR;
    }

    public static boolean isAgingMode() {
        return get(PROP_AGING_FLAG, "0").equals("1");
    }
    //-------------------------------------------------------------------
    private static Class<?> mClassType = null;
    private static Method mSetMethod = null;
    private static Method mGetMethod = null;
    private static Method mGetIntMethod = null;

    private static void init() {
        try {
            if (mClassType == null) {
                mClassType = Class.forName("android.os.SystemProperties");

                mSetMethod = mClassType.getDeclaredMethod("set", String.class, String.class);
                mGetMethod = mClassType.getDeclaredMethod("get", String.class, String.class);
                mGetIntMethod = mClassType.getDeclaredMethod("getInt", String.class, int.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}