package com.wlw.admin.owspace.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


public class NetUtil {
    /**
     * 判断网络是否连接
     */
    public static boolean isConnected(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (null != manager) {
            NetworkInfo info = manager.getActiveNetworkInfo();
            if (null != info && info.isConnected()) {
                if (NetworkInfo.DetailedState.CONNECTED == info.getDetailedState()) {
                    return true;
                }
            }
        }
        return false;
    }
    /**
     * 判断是否是WiFi连接
     */
    public static boolean isWiFi(Context context){
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(manager==null)
            return false;
        return manager.getActiveNetworkInfo().getType()==ConnectivityManager.TYPE_WIFI;
    }
}
