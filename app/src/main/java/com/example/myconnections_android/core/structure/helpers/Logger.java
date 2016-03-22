package com.example.myconnections_android.core.structure.helpers;

import android.util.Log;

import com.example.myconnections_android.BuildConfig;


public class Logger {


    public static void debug(Class<?> cls, String message) {
        if (BuildConfig.DEBUG)
            Log.d(cls.getSimpleName(), "--------" + message);
    }

    public static void info(Class<?> cls, String message) {
        if (BuildConfig.DEBUG)
            Log.e(cls.getSimpleName(), "--------" + message);
    }

    public static void error(Class<?> cls, String message, Exception e) {
        if (BuildConfig.DEBUG)
            Log.e(cls.getSimpleName(), "--------" + message, e);
    }

    public static void warn(Class<?> cls, String message) {
        if (BuildConfig.DEBUG)
            Log.w(cls.getName(), "--------" + message);
    }

    public static void error(Class<?> cls, String message) {
        if (BuildConfig.DEBUG)
            Log.e(cls.getSimpleName(), "--------" + message);
    }
}