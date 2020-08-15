package com.wade.mobile.common;

import android.util.Log;


public class MobileLog {
    private static int LOG_LEVEL;

    static {
        String value = MobileGlobalConfig.getValue(MobileGlobalConfig.LOG_LEVEL);
        if (value == null) {
            value = "2";
        }
        LOG_LEVEL = Integer.parseInt(value);
       // System.out.println("Log_level:"+LOG_LEVEL);
    }

    private enum LogLevel {
        ERROR(4),
        WARN(3),
        DEBUG(2),
        INFO(1);

        private int level;

        private LogLevel(int level2) {
            this.level = level2;
        }

        public int level() {
            return this.level;
        }
    }

    /* renamed from: e */
    public static void e(String tag, String msg) {
        if (LOG_LEVEL >= LogLevel.ERROR.level()) {
            Log.e(tag, msg);
        }
    }

    /* renamed from: e */
    public static void e(String tag, String msg, Throwable tr) {
        if (LOG_LEVEL >= LogLevel.ERROR.level()) {
            Log.e(tag, msg, tr);
        }
    }

    /* renamed from: w */
    public static void w(String tag, String msg) {
        if (LOG_LEVEL >= LogLevel.WARN.level()) {
            Log.w(tag, msg);
        }
    }

    /* renamed from: w */
    public static void w(String tag, Throwable tr) {
        if (LOG_LEVEL >= LogLevel.WARN.level()) {
            Log.w(tag, tr);
        }
    }

    /* renamed from: w */
    public static void w(String tag, String msg, Throwable tr) {
        if (LOG_LEVEL >= LogLevel.WARN.level()) {
            Log.w(tag, msg, tr);
        }
    }

    /* renamed from: d */
    public static void d(String tag, String msg) {
        if (LOG_LEVEL >= LogLevel.DEBUG.level()) {
            Log.d(tag, msg);
        }
    }

    /* renamed from: d */
    public static void d(String tag, String msg, Throwable tr) {
        if (LOG_LEVEL >= LogLevel.DEBUG.level()) {
            Log.d(tag, msg, tr);
        }
    }

    /* renamed from: i */
    public static void i(String tag, String msg) {
        if (LOG_LEVEL >= LogLevel.INFO.level()) {
            Log.i(tag, msg);
        }
    }

    /* renamed from: i */
    public static void i(String tag, String msg, Throwable tr) {
        if (LOG_LEVEL >= LogLevel.INFO.level()) {
            Log.i(tag, msg, tr);
        }
    }
}