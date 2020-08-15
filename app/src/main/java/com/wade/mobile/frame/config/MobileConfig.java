package com.wade.mobile.frame.config;

import com.wade.mobile.common.MobileCache;
import com.wade.mobile.frame.multiple.MultipleManager;
import com.wade.mobile.util.Constant;
import com.wade.mobile.util.xml.MobileXML;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MobileConfig {

    private static final String ACTION_PATH = "CONFIGS/CONFIG";
    private static final String ATTR_NAME = "name";
    private static final String ATTR_VALUE = "value";
    private static MobileConfig config;
    private final String XML_FILE_PATH = "assets/mobile-config.xml";
    public static final String APP_PATH = "app_path";
    public static final String CACHE_MODE = "cache_mode";
    public static final String ENCODE = "encode";
    public static final String IS_DEBUG = "is_debug";
    public static final String IS_FORCE_UPDATE = "is_force_update";
    public static final String IS_LOADING_DIALOG = "is_loading_dialog";
    public static final String IS_MULT_WEBVIEW = "is_mult_webview";
    public static final String IS_OVERTIME_RETRY = "is_overtime_retry";
    public static final String LOADING_BG_IMAGE = "loading_bg_image";
    public static final String LOADING_PAGE = "loading_page";
    public static final String LOADURL_TIMEOUT = "loadurl_timeout";
    public static final String PUSH_ADDRESS = "push_address";
    public static final String PUSH_PORT = "push_port";
    public static final String REMOTE_URL = "remote_url";
    public static final String REQUEST_HOST = "request_host";
    public static final String REQUEST_PATH = "request_path";
    public static final String REQUEST_SERVLET = "request_servlet";
    public static final String UPDATE_URL = "update_url";

    private MobileConfig() {
    }

    public static MobileConfig getInstance() {
        if (config == null) {
            synchronized (MobileConfig.class) {
                if (config == null) {
                    config = new MobileConfig();
                }
            }
        }
        return config;
    }

    private Map<String, Map> getConfigMap() {
        Object cache = MobileCache.getInstance().get(Constant.MobileCache.WADE_MOBILE_CONFIG);
        if (cache != null) {
            return (Map) cache;
        }
        Map configs = transActions((List) new MobileXML("assets/mobile-config.xml").getConfig().get(ACTION_PATH));
        MobileCache.getInstance().put(Constant.MobileCache.WADE_MOBILE_CONFIG, configs);
        return configs;
    }

    private Map<String, Map> transActions(List<Map> list) {
        Map configs = new HashMap();
        for (Map action : list) {
            configs.put(action.get("name").toString(), action);
        }
        return configs;
    }

    public String getConfigValue(String key) {
        return getConfigValue(key, (String) null);
    }

    public String getConfigValue(String key, String defaultValue) {
        if (key == null || "".equals(key)) {
            return null;
        }
        Map<?, ?> config2 = getConfigMap().get(key);
        return (config2 == null || config2.get("value") == null) ? defaultValue : config2.get("value").toString().trim();
    }

    private String getConfigUrl(String key) {
        String url = getConfigValue(key);
        if (url == null || url.startsWith(Constant.START_HTTP) || url.startsWith(Constant.START_HTTPS)) {
            return url;
        }
        StringBuilder append = new StringBuilder().append(getRequestHost()).append(getRequestPath());
        if (!url.startsWith("/")) {
            url ="/" + url;
        }
        return append.append(url).toString();
    }

    public String getRemoteUrl() {
        return getConfigValue(REMOTE_URL);
    }

    public String getRequestHost() {
        if (MultipleManager.isMultiple()) {
            return MultipleManager.getCurrRequestHost();
        }
        return getConfigValue(REQUEST_HOST);
    }

    public String getRequestPath() {
        if (MultipleManager.isMultiple()) {
            return MultipleManager.getCurrRequestPath();
        }
        return getConfigValue(REQUEST_PATH);
    }

    public String getRequestServlet() {
        if (MultipleManager.isMultiple()) {
            return MultipleManager.getCurrRequestServlet();
        }
        return getConfigValue(REQUEST_SERVLET);
    }

    public String getRequestBaseUrl() {
        String host = getRequestHost();
        return host + getRequestPath();
    }

    public String getRequestUrl() {
        String baseUrl = getRequestBaseUrl();
        return baseUrl + getRequestServlet();
    }

    public String getAppPath() {
        if (MultipleManager.isMultiple()) {
            return MultipleManager.getCurrAppPath();
        }
        String app_path = getConfigValue(APP_PATH);
        return app_path == null ? "" : app_path;
    }

    public String getEncode() {
        String encode = getConfigValue(ENCODE);
        return encode == null ? "UTF-8" : encode;
    }

    public int getCacheMode() {
        String mode = getConfigValue(CACHE_MODE);
        if (mode == null || mode.equals("")) {
            return 2;
        }
        try {
            return Integer.parseInt(mode.trim());
        } catch (Exception e) {
            return 2;
        }
    }

    public String getUpdateUrl() {
        return getConfigUrl(UPDATE_URL);
    }

    public boolean isForceUpdate() {
        return Constant.TRUE.equals(getConfigValue(IS_FORCE_UPDATE));
    }

    public String getLoadingPage() {
        return getConfigValue(LOADING_PAGE);
    }

    public boolean isMultWebview() {
        return Constant.TRUE.equals(getConfigValue(IS_MULT_WEBVIEW));
    }

    public boolean isLoadingDialog() {
        return Constant.TRUE.equals(getConfigValue(IS_LOADING_DIALOG));
    }

    public String getLoadingBgImage() {
        return getConfigValue(LOADING_BG_IMAGE);
    }

    public boolean isOvertimeRetry() {
        return Constant.TRUE.equals(getConfigValue(IS_OVERTIME_RETRY));
    }

    public int getLoadurlTimeout() {
        String value = getConfigValue(LOADURL_TIMEOUT);
        if (value == null) {
            return -1;
        }
        return Integer.parseInt(value);
    }

}
