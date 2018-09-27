package com.wlw.admin.owspace.utils;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * @author admin
 */
public class TimeUtil {
    public static long getCurrentSeconds() {
        return System.currentTimeMillis() / 1000;
    }

    public static String getDate(String format) {
        return new SimpleDateFormat(format, Locale.ENGLISH).format(new Date());
    }

    public static String[] getCalendarShowTime(String paramString) {
        Long l = Long.valueOf(paramString);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(l * 1000L);
        return getCalendarShowTime(calendar.getTimeInMillis());
    }

    public static String[] getCalendarShowTime(long paramLong) {
        String[] localObject;
        String str = new SimpleDateFormat("yyyy:MMM:d", Locale.ENGLISH).format(new Date(paramLong));
        localObject = str.split(":");
        if (localObject.length == 3) {
            return localObject;
        }
        return null;
    }
}
