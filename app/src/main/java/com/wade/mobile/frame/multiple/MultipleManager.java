package com.wade.mobile.frame.multiple;

import com.wade.mobile.frame.config.ServerConfig;
import com.wade.mobile.frame.config.ServerDataConfig;
import com.wade.mobile.frame.config.ServerPageConfig;

import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.Map;

public class MultipleManager {
    private static String currAppId;
    private static boolean isMultiple;
    private static String multBasePath;
    private static Map<String, MultipleAppConfig> multipleAppConfigs = new HashMap();

    public static void setMultBasePath(String path) {
        multBasePath = path;
    }

    public static String getMultBasePath() {
        return multBasePath;
    }

    public static void putAppConfig(String appId, MultipleAppConfig config) {
        multipleAppConfigs.put(appId, config);
    }

    public static MultipleAppConfig getAppConfig(String appId) {
        return multipleAppConfigs.get(appId);
    }

    public static String getCurrAppId() {
        return currAppId;
    }

    public static void setCurrAppId(String currAppId2) {
        currAppId = currAppId2;
    }

    public static boolean isMultiple() {
        return isMultiple;
    }

    public static void setMultiple(boolean isMultiple2) {
        isMultiple = isMultiple2;
    }

    public static MultipleAppConfig getCurrAppConfig() {
        return multipleAppConfigs.get(currAppId);
    }

    public static String getCurrRequestHost() {
        return multipleAppConfigs.get(currAppId).getRequestHost();
    }

    public static String getCurrRequestPath() {
        return multipleAppConfigs.get(currAppId).getRequestPath();
    }

    public static String getCurrRequestServlet() {
        return multipleAppConfigs.get(currAppId).getRequestServlet();
    }

    public static String getCurrAppPath() {
        return multipleAppConfigs.get(currAppId).getAppPath();
    }

    public static ServerPageConfig getCurrServerPageConfig() {
        return multipleAppConfigs.get(currAppId).getServerPageConfig();
    }

    public static ServerDataConfig getCurrServerDataConfig() {
        return multipleAppConfigs.get(currAppId).getServerDataConfig();
    }

    public static ServerConfig getCurrServerConfig() {
        return multipleAppConfigs.get(currAppId).getServerConfig();
    }

    public static RSAPublicKey getCurrPublicKey() {
        return multipleAppConfigs.get(currAppId).getPublicKey();
    }

}
