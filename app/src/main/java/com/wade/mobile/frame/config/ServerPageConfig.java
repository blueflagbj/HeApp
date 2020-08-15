package com.wade.mobile.frame.config;

import com.wade.mobile.common.MobileCache;
import com.wade.mobile.frame.config.AbstractConfig;
import com.wade.mobile.frame.multiple.MultipleManager;
import com.wade.mobile.frame.template.TemplateManager;
import com.wade.mobile.util.Constant;
import com.wade.mobile.util.FileUtil;
import com.wade.mobile.util.xml.MobileXML;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServerPageConfig extends AbstractConfig {
    private static final String ATTR_DATA = "data";
    private static final String ATTR_NAME = "name";
    private static final String ATTR_TEMPLATE = "template";
    private static final String PAGE_PATH = "PAGES/ACTION";
    private static final String XML_FILE_PATH = "template/server-page.xml";
    private static ServerPageConfig config;

    private ServerPageConfig() {
    }

    public static ServerPageConfig getInstance() {
        if (!MultipleManager.isMultiple()) {
            if (config == null) {
                synchronized (ServerPageConfig.class) {
                    if (config == null) {
                        config = new ServerPageConfig();
                    }
                }
            }
            return config;
        } else if (MultipleManager.getCurrServerPageConfig() != null) {
            return MultipleManager.getCurrServerPageConfig();
        } else {
            synchronized (ServerConfig.class) {
                MultipleManager.getCurrAppConfig().setServerPageConfig(new ServerPageConfig());
            }
            return MultipleManager.getCurrServerPageConfig();
        }
    }

    /* access modifiers changed from: protected */
    public Map<String, ?> loadConfig() throws Exception {
        System.out.println("ServerPageConfig-loadConfig:0:");
        String filePath =FileUtil.connectFilePath(TemplateManager.getBasePath(), XML_FILE_PATH);
        System.out.println("ServerPageConfig-loadConfig:1:"+filePath);
        MobileXML mobileXML = new MobileXML(filePath, true);
        System.out.println("ServerPageConfig-loadConfig:2:"+mobileXML.toString());
        System.out.println("ServerPageConfig-loadConfig:2-1:"+mobileXML.getConfig().toString());
        List<Map> list= (List)mobileXML.getConfig().get(PAGE_PATH);
      //  System.out.println("ServerPageConfig-loadConfig:3:"+list.toString());
        Map<String, ?> actions = transActions(list);
        System.out.println("ServerPageConfig-loadConfig:4:"+actions.toString());
       // Map<String, ?> actions = transActions((List) new MobileXML(FileUtil.connectFilePath(TemplateManager.getBasePath(), XML_FILE_PATH), true).getConfig().get(PAGE_PATH));
        System.out.println("ServerPageConfig-loadConfig:5:");
        MobileCache.getInstance().put(Constant.MobileCache.SERVER_PAGE_CONFIG, actions);
        System.out.println("ServerPageConfig-loadConfig:6:");
        return actions;
    }

    /* access modifiers changed from: protected */
    public Map<String, Map> getConfigMap() throws Exception {
        System.out.println("ServerPageConfig-getConfigMap:0:");
        Map map =  super.getConfigMap();
        System.out.println("ServerPageConfig-getConfigMap:6:");
        return map;
    }

    private Map transActions(List<Map> list) {
        Map actions = new HashMap();
        for (Map action : list) {
            actions.put(action.get("name").toString(), action);
        }
        return actions;
    }

    public static String getTemplate(String action) throws Exception {
        System.out.println("ServerPageConfig-getTemplate:0:"+action);
        Map actionMap = getInstance().getConfigMap().get(action);
        System.out.println("ServerPageConfig-getTemplate:1:"+action);
        if (actionMap != null) {
            System.out.println("ServerPageConfig-getTemplate:2:"+actionMap.get(ATTR_TEMPLATE).toString());
            return actionMap.get(ATTR_TEMPLATE).toString();
        }
        System.out.println("ServerPageConfig-getTemplate:6:");
        return null;
    }

    public static String getData(String action) throws Exception {
        Map actionMap = getInstance().getConfigMap().get(action);
        if (actionMap == null || actionMap.get("data") == null) {
            return null;
        }
        return actionMap.get("data").toString();
    }

}
