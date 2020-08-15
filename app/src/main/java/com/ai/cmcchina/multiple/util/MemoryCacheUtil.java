package com.ai.cmcchina.multiple.util;

import com.ailk.common.data.IData;
import com.ailk.common.data.impl.DataMap;
import com.wade.mobile.common.MobileCache;
import com.wade.mobile.util.Constant;
import com.wade.mobile.util.StringUtil;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;

/* renamed from: com.ai.cmcchina.multiple.util.MemoryCacheUtil */
public class MemoryCacheUtil {
    public static Map getMemoryCache() {
        Object map = MobileCache.getInstance().get(Constant.MobileCache.WADE_MOBILE_STORAGE);
        if (map != null) {
            return (HashMap) map;
        }
        Map cache = new HashMap();
        MobileCache.getInstance().put(Constant.MobileCache.WADE_MOBILE_STORAGE, cache);
        return cache;
    }

    public static void setMemoryCache(JSONArray param) throws Exception {
        setMemoryCache(param.getString(0), param.getString(1));
    }

    public static void setMemoryCache(String key, Object value) {
        if (key != null && !"".equals(key)) {
            Map map = getMemoryCache();
            if (StringUtil.isDataMap(key)) {
                map.putAll(new DataMap(key));
            } else {
                map.put(key, value);
            }
        }
    }

    public static Object getMemoryCache(JSONArray param) throws Exception {
        Object value = getMemoryCache(param.getString(0), param.getString(1));
        if (value == null) {
            return null;
        }
        return value.toString();
    }

    public static Object getMemoryCache(String key, Object defValue) {
        Object value = null;
        if (key != null) {
            try {
                if (!"".equals(key)) {
                    Map map = getMemoryCache();
                    if (StringUtil.isJSONArray(key)) {
                        JSONArray param = new JSONArray(key);
                        IData data = new DataMap();
                        int len = param.length();
                        for (int i = 0; i < len; i++) {
                            String tmpKey = param.getString(i);
                            data.put(tmpKey, map.get(tmpKey));
                        }
                        return data;
                    }
                    Object obj = map.get(key);
                    if (obj != null) {
                        value = obj;
                    }
                    if (value == null && defValue != null) {
                        value = defValue;
                    }
                    return value;
                }
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    public static void removeMemoryCache(JSONArray param) throws Exception {
        removeMemoryCache(param.getString(0));
    }

    public static void removeMemoryCache(String key) throws JSONException {
        if (key != null && !"".equals(key)) {
            Map map = getMemoryCache();
            if (StringUtil.isJSONArray(key)) {
                JSONArray param = new JSONArray(key);
                int len = param.length();
                for (int i = 0; i < len; i++) {
                    map.remove(param.getString(i));
                }
                return;
            }
            map.remove(key);
        }
    }

    public static void clearMemoryCache(JSONArray param) throws Exception {
        clearMemoryCache();
    }

    public static void clearMemoryCache() {
        getMemoryCache().clear();
    }
}