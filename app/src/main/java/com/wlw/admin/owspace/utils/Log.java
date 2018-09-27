package com.wlw.admin.owspace.utils;


import com.wlw.admin.owspace.BuildConfig;

public class Log {
    public static void e(String tag, String msg) {
        if (isDebug()) {
            android.util.Log.e(tag, msg);
        }
    }
    public static void d(String tag, String msg) {
        if (isDebug()) {
            android.util.Log.d(tag, msg);
        }
    }
    private static boolean isDebug() {
        return BuildConfig.DEBUG;
    }
}
