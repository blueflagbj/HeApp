package com.wade.mobile.frame.config;

import com.wade.mobile.common.MobileCache;
import com.wade.mobile.frame.multiple.MultipleManager;
import com.wade.mobile.frame.template.TemplateManager;
import com.wade.mobile.util.Constant;
import com.wade.mobile.util.FileUtil;
import com.wade.mobile.util.xml.MobileXML;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServerDataConfig extends AbstractConfig {
    private static final String ACTION_PATH = "DATAS/ACTION";
    private static final String ATTR_CLASS = "class";
    private static final String ATTR_ENCRYPT = "encrypt";
    private static final String ATTR_METHOD = "method";
    private static final String ATTR_NAME = "name";
    private static final String XML_FILE_PATH = "template/server-data.xml";
    private static ServerDataConfig config;

    private ServerDataConfig() {
    }

    public static ServerDataConfig getInstance() {
        if (!MultipleManager.isMultiple()) {
            if (config == null) {
                synchronized (ServerDataConfig.class) {
                    if (config == null) {
                        config = new ServerDataConfig();
                    }
                }
            }
            return config;
        } else if (MultipleManager.getCurrServerDataConfig() != null) {
            return MultipleManager.getCurrServerDataConfig();
        } else {
            synchronized (ServerConfig.class) {
                MultipleManager.getCurrAppConfig().setServerDataConfig(new ServerDataConfig());
            }
            return MultipleManager.getCurrServerDataConfig();
        }
    }

    /* access modifiers changed from: protected */
    public Map<String, ?> loadConfig() throws Exception {
        Map<String, ?> actions = transActions((List) new MobileXML(FileUtil.connectFilePath(TemplateManager.getBasePath(), XML_FILE_PATH), true).getConfig().get(ACTION_PATH));
        MobileCache.getInstance().put(Constant.MobileCache.SERVER_DATA_CONFIG, actions);
        return actions;
    }

    private Map transActions(List<Map> list) {
        Map actions = new HashMap();
        for (Map action : list) {
            actions.put(action.get("name").toString(), action);
        }
        return actions;
    }

    /* access modifiers changed from: protected */
    public Map<String, Map> getConfigMap() throws Exception {
        Map map =super.getConfigMap();
        return map;
    }

    public static Boolean isEncrypt(String dataAction) throws Exception {
        boolean z = false;
        Map actionMap = getInstance().getConfigMap().get(dataAction);
        if (actionMap == null) {
            return false;
        }
        if (Constant.TRUE.equals(actionMap.get("encrypt"))) {
            z = true;
        }
        return Boolean.valueOf(z);
    }

    public static String getActionClass(String action) throws Exception {
        Map actionMap = getInstance().getConfigMap().get(action);
        if (actionMap != null) {
            return actionMap.get("class").toString();
        }
        return null;
    }

    public static String getActionMethod(String action) throws Exception {
        Map actionMap = getInstance().getConfigMap().get(action);
        if (actionMap != null) {
            return actionMap.get("method").toString();
        }
        return null;
    }
}