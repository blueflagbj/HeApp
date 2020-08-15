package com.ai.ipu.basic.util;

public class IpuLog {
    public static void debug(String msg) {
        System.out.println(msg);
    }

    public static void info(String msg) {
        System.out.println(msg);
    }

    public static void warn(String msg) {
        System.out.println(msg);
    }

    public static void error(String msg) {
        System.out.println(msg);
    }

    public static void error(String msg, Throwable tr) {
        System.out.println(msg);
        tr.printStackTrace();
    }
}