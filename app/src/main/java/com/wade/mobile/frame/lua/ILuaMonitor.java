package com.wade.mobile.frame.lua;

public interface ILuaMonitor {
    void debug(String str) throws Exception;

    void error(String str) throws Exception;

    String getError();

    String getTrace();

    void trace(String str) throws Exception;
}