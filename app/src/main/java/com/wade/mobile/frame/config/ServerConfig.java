package com.wade.mobile.frame.config;

import com.wade.mobile.common.MobileCache;
import com.wade.mobile.frame.config.AbstractConfig;
import com.wade.mobile.frame.multiple.MultipleAppConfig;
import com.wade.mobile.frame.multiple.MultipleManager;
import com.wade.mobile.frame.template.TemplateManager;
import com.wade.mobile.util.Constant;
import com.wade.mobile.util.FileUtil;
import com.wade.mobile.util.xml.MobileXML;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServerConfig extends AbstractConfig {
    private static ServerConfig config;
    private final String ATTR_NAME = "name";
    private final String ATTR_VALUE = Constant.ATTR_VALUE;
    private final String ATTR_VISIBLE = Constant.ATTR_VISIBLE;
    private final String CONFIG_PATH = "CONFIGS/CONFIG";
    private final String XML_FILE_PATH = "template/server-config.xml";

    private ServerConfig() {
    }

    public static ServerConfig getInstance() {
        if (!MultipleManager.isMultiple()) {
            if (config == null) {
                synchronized (ServerConfig.class) {
                    if (config == null) {
                        config = new ServerConfig();
                    }
                }
            }
            return config;
        } else if (MultipleManager.getCurrServerConfig() != null) {
            return MultipleManager.getCurrServerConfig();
        } else {
            synchronized (ServerConfig.class) {
                MultipleManager.getCurrAppConfig().setServerConfig(new ServerConfig());
            }
            return MultipleManager.getCurrServerConfig();
        }
    }

    /* access modifiers changed from: protected */
    public Map<String, ?> loadConfig() throws Exception {
        System.out.println("ServerConfig_loadConfig:0:");

        Map<String, String> transActions = transActions((List) new MobileXML(FileUtil.connectFilePath(TemplateManager.getBasePath(), "template/server-config.xml"), true).getConfig().get("CONFIGS/CONFIG"));
        MobileCache.getInstance().put(Constant.MobileCache.SERVER_CONFIG, transActions);
        System.out.println("ServerConfig_loadConfig:1:"+transActions.toString());
        return transActions;
    }

    private Map<String, String> transActions(List<Map<String, String>> list) {
        Map<String, String> actions = new HashMap<>();
        for (Map<String, String> action : list) {
            actions.put(action.get("name").toString(), action.get(Constant.ATTR_VALUE).toString());
        }
        return actions;
    }

    private Map<String, String> transVisibleActions(List<Map<String, String>> list) {
        Map<String, String> actions = new HashMap<>();
        for (Map<String, String> action : list) {
            if (Constant.TRUE.equals(action.get(Constant.ATTR_VISIBLE))) {
                actions.put(action.get("name").toString(), action.get(Constant.ATTR_VALUE).toString());
            }
        }
        return actions;
    }

    public String getValue(String action) throws Exception {
        if (getConfigMap().get(action) == null) {
            return null;
        }
        return getConfigMap().get(action).toString();
    }
}

