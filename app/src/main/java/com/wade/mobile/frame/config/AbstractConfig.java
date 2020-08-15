package com.wade.mobile.frame.config;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractConfig {
    protected Map<String, ?> configsMap = new HashMap();

    /* access modifiers changed from: protected */
    protected abstract Map<String, ?> loadConfig() throws Exception;

    protected AbstractConfig() {
    }

    /* access modifiers changed from: protected */
    protected Map<String, ?> getConfigMap() throws Exception {
        System.out.println("AbstractConfig-getConfigMap:0:");
        if (this.configsMap.isEmpty()) {
            this.configsMap = loadConfig();
        }
        System.out.println("AbstractConfig-getConfigMap:6:"+this.configsMap.toString());
        return this.configsMap;
    }

}
