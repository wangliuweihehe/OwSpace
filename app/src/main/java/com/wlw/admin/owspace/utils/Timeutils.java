package com.wlw.admin.owspace.utils;

import android.annotation.SuppressLint;

public class Timeutils {
    @SuppressLint("DefaultLocale")
    public static String formatDuration(int duration) {
        duration /= 1000;
        int minute = duration / 60;
        int hour = minute / 60;
        int second = duration % 60;
        minute %= 60;
        if (hour != 0) {
            return String.format("%2d:%02d:%02d", hour, minute, second);
        } else {
            return String.format("%02d:%02d", minute, second);
        }
    }
}
