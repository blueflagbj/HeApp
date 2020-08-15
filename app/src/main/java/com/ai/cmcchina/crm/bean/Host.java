package com.ai.cmcchina.crm.bean;

public class Host {
    static {
        System.loadLibrary("host");
    }

    public native String getGisHost();

    public native String getKey();

    public native String getMD5();

}
