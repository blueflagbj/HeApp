package com.wade.mobile.frame.multiple;

import com.wade.mobile.frame.config.ServerConfig;
import com.wade.mobile.frame.config.ServerDataConfig;
import com.wade.mobile.frame.config.ServerPageConfig;

import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.Map;

public class MultipleAppConfig {
    private String appPath;
    private Map<String, String> defineConfig = new HashMap();
    private RSAPublicKey publicKey;
    private String requestHost;
    private String requestPath;
    private String requestServlet;
    private ServerConfig serverConfig;
    private ServerDataConfig serverDataConfig;
    private ServerPageConfig serverPageConfig;

    public MultipleAppConfig(String requestHost2, String requestPath2, String requestServlet2, String appPath2) {
        this.requestHost = requestHost2;
        this.requestPath = requestPath2;
        this.requestServlet = requestServlet2;
        this.appPath = appPath2;
    }

    public String getRequestHost() {
        return this.requestHost;
    }

    public void setRequestHost(String requestHost2) {
        this.requestHost = requestHost2;
    }

    public String getRequestPath() {
        return this.requestPath;
    }

    public void setRequestPath(String requestPath2) {
        this.requestPath = requestPath2;
    }

    public String getRequestServlet() {
        return this.requestServlet;
    }

    public void setRequestServlet(String requestServlet2) {
        this.requestServlet = requestServlet2;
    }

    public String getAppPath() {
        return this.appPath;
    }

    public void setAppPath(String appPath2) {
        this.appPath = appPath2;
    }

    public ServerPageConfig getServerPageConfig() {
        return this.serverPageConfig;
    }

    public void setServerPageConfig(ServerPageConfig serverPageConfig2) {
        this.serverPageConfig = serverPageConfig2;
    }

    public ServerDataConfig getServerDataConfig() {
        return this.serverDataConfig;
    }

    public void setServerDataConfig(ServerDataConfig serverDataConfig2) {
        this.serverDataConfig = serverDataConfig2;
    }

    public ServerConfig getServerConfig() {
        return this.serverConfig;
    }

    public void setServerConfig(ServerConfig serverConfig2) {
        this.serverConfig = serverConfig2;
    }

    public RSAPublicKey getPublicKey() {
        return this.publicKey;
    }

    public void setPublicKey(RSAPublicKey publicKey2) {
        this.publicKey = publicKey2;
    }

    public void putDefine(String key, String value) {
        this.defineConfig.put(key, value);
    }

    public String getDefine(String key) {
        return this.defineConfig.get(key);
    }

}
