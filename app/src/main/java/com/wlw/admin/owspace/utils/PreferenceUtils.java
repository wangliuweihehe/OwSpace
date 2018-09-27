package com.wlw.admin.owspace.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Set;

public class PreferenceUtils {

    public static String getPrefString(Context context, String key, String defaultValue) {
        return getSharedPreference(context).getString(key, defaultValue);
    }

    public static void setPrefString(Context context, String key, String value) {
        getSharedPreference(context).edit().putString(key, value).apply();
    }

    public static boolean getPrefBoolean(Context context, String key, boolean defaultValue) {
        return getSharedPreference(context).getBoolean(key, defaultValue);
    }

    public static void setPrefBoolean(Context context, String key, boolean value) {
        getSharedPreference(context).edit().putBoolean(key, value).apply();
    }


    public static int getPrefInt(Context context, String key, int defaultValue) {
        return getSharedPreference(context).getInt(key, defaultValue);
    }

    public static void setPrefInt(Context context, String key, int value) {
        getSharedPreference(context).edit().putInt(key, value).apply();
    }


    public static float getPrefFloat(Context context, String key, float defaultValue) {
        return getSharedPreference(context).getFloat(key, defaultValue);
    }

    public static void setPrefFloat(Context context, String key, float value) {
        getSharedPreference(context).edit().putFloat(key, value).apply();
    }

    public static float getPrefLong(Context context, String key, long defaultValue) {
        return getSharedPreference(context).getLong(key, defaultValue);
    }

    public static void setPrefLong(Context context, String key, long value) {
        getSharedPreference(context).edit().putLong(key, value).apply();
    }


    public static Set<String> getPrefStringSet(Context context, String key, Set<String> defaultValue) {
        return getSharedPreference(context).getStringSet(key, defaultValue);
    }

    public static void setPrefStringSet(Context context, String key, Set<String> value) {
        getSharedPreference(context).edit().putStringSet(key, value).apply();
    }


    public static Object getPrefValue(Context context, String key, Object defaultValue) {
        SharedPreferences sharedPreference = getSharedPreference(context);
        if (defaultValue instanceof String) {
            return sharedPreference.getString(key, (String) defaultValue);
        }
        if (defaultValue instanceof Integer) {
            return sharedPreference.getInt(key, (Integer) defaultValue);
        }
        if (defaultValue instanceof Boolean) {
            return sharedPreference.getBoolean(key, (Boolean) defaultValue);
        }
        if (defaultValue instanceof Float) {
            return sharedPreference.getFloat(key, (Float) defaultValue);
        }
        if (defaultValue instanceof Long) {
            return sharedPreference.getLong(key, (Long) defaultValue);
        }
        if (defaultValue instanceof Set) {
            return sharedPreference.getStringSet(key, (Set<String>) defaultValue);
        }
        return new  RuntimeException("class type error");
    }
    public static Object setPrefValue(Context context, String key, Object value) {
        SharedPreferences sharedPreference = getSharedPreference(context);
        if (value instanceof String) {
            return sharedPreference.edit().putString(key, (String) value);
        }
        if (value instanceof Integer) {
            return sharedPreference.edit().putInt(key, (int) value);
        }
        if (value instanceof Boolean) {
            return sharedPreference.edit().putBoolean(key, (Boolean) value);
        }
        if (value instanceof Float) {
            return sharedPreference.edit().putFloat(key, (float) value);
        }
        if (value instanceof Long) {
            return sharedPreference.edit().putLong(key, (long) value);
        }
        if (value instanceof Set) {
            return sharedPreference.edit().putStringSet(key, (Set<String>) value);
        }
        return new  RuntimeException("class type error");
    }


    public static boolean hasKey(Context context, String key) {
        return getSharedPreference(context).contains(key);
    }

    private static SharedPreferences getSharedPreference(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }
}
