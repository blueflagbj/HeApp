package com.wade.mobile.frame.config;

import com.wade.mobile.common.MobileCache;
import com.wade.mobile.util.Constant;
import com.wade.mobile.util.xml.MobileXML;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MobileActionConfig {
    private static final String ACTION_PATH = "ACTIONS/ACTION";
    private static final String ATTR_CLASS = "class";
    private static final String ATTR_METHOD = "method";
    private static final String ATTR_NAME = "name";
    private static final String XML_FILE_PATH = "assets/mobile-action.xml";
    private static MobileActionConfig config;

    private MobileActionConfig() {
    }

    private static MobileActionConfig getInstance() {
        if (config == null) {
            synchronized (MobileActionConfig.class) {
                if (config == null) {
                    config = new MobileActionConfig();
                }
            }
        }
        return config;
    }

    private Map<?, Map> getConfigMap() {
        Object cache = MobileCache.getInstance().get(Constant.MobileCache.WADE_MOBILE_ACTION);
        if (cache != null) {
            return (Map) cache;
        }
        Map actions = transActions((List) new MobileXML(XML_FILE_PATH).getConfig().get(ACTION_PATH));
        System.out.println("MobileActionConfig_getConfigMap:"+actions.toString());
        MobileCache.getInstance().put(Constant.MobileCache.WADE_MOBILE_ACTION, actions);
        return actions;
    }

    private Map transActions(List<Map> list) {
        Map actions = new HashMap();
        for (Map action : list) {
            actions.put(action.get("name").toString().toUpperCase(), action);
        }
        return actions;
    }

    public static String getActionClass(String action) {
        Map actionMap = getInstance().getConfigMap().get(action);
        if (actionMap != null) {
            return actionMap.get("class").toString();
        }
        return null;
    }

    public static String getActionMethod(String action) {
        Map actionMap = getInstance().getConfigMap().get(action);
        if (actionMap != null) {
            return actionMap.get("method").toString();
        }
        return null;
    }
}