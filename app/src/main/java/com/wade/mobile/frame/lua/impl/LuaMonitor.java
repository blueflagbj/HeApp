package com.wade.mobile.frame.lua.impl;

import com.wade.mobile.common.MobileLog;
import com.wade.mobile.frame.lua.ILuaMonitor;

public class LuaMonitor implements ILuaMonitor {
    public final String TAG = LuaMonitor.class.getSimpleName();
    private String error;
    private String trace;

    public void debug(String msg) throws Exception {
        MobileLog.d(this.TAG, msg);
    }

    public void error(String error2) throws Exception {
        MobileLog.e(this.TAG, error2);
        this.error = error2;
    }

    public void trace(String trace2) throws Exception {
        MobileLog.e(this.TAG, trace2);
        this.trace = trace2;
    }

    public String getError() {
        return this.error;
    }

    public String getTrace() {
        return this.trace;
    }
}