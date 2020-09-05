package com.talents.igallery.platform;

/**
 * Created by user on 2018/10/18.
 */
public class GalleryNative {
    static {
        System.loadLibrary("gallery_native");
    }
    /**
     * RTC module------------------------------------------
     */
    public static native int setOnTime_RTC(long onTime_rtc);
}
