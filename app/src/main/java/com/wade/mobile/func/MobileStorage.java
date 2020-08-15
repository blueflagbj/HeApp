package com.wade.mobile.func;

import com.ailk.common.data.IData;
import com.ailk.common.data.impl.DataMap;
import com.wade.mobile.common.MobileCache;
import com.wade.mobile.frame.IWadeMobile;
import com.wade.mobile.frame.plugin.Plugin;
import com.wade.mobile.helper.SharedPrefHelper;
import com.wade.mobile.util.Constant;
import com.wade.mobile.util.StringUtil;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;

public class MobileStorage extends Plugin {
    private SharedPrefHelper sharedPrefHelper;

    public MobileStorage(IWadeMobile wademobile) {
        super(wademobile);
        this.sharedPrefHelper = new SharedPrefHelper(wademobile.getActivity());
    }

    public Map getMemoryCache() {
        Object map = MobileCache.getInstance().get(Constant.MobileCache.WADE_MOBILE_STORAGE);
        if (map != null) {
            return (HashMap) map;
        }
        Map cache = new HashMap();
        MobileCache.getInstance().put(Constant.MobileCache.WADE_MOBILE_STORAGE, cache);
        return cache;
    }

    public void setMemoryCache(JSONArray param) throws Exception {
        setMemoryCache(param.getString(0), param.getString(1));
    }

    public void setMemoryCache(String key, Object value) {
        if (key != null && !"".equals(key)) {
            Map map = getMemoryCache();
            if (StringUtil.isDataMap(key)) {
                map.putAll(new DataMap(key));
            } else {
                map.put(key, value);
            }
        }
    }

    public void getMemoryCache(JSONArray param) throws Exception {
        Object value = getMemoryCache(param.getString(0), param.getString(1));
        callback(value == null ? null : value.toString());
    }

    public Object getMemoryCache(String key, Object defValue) throws JSONException {
        Object value;
        if (key == null || "".equals(key)) {
            return null;
        }
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
        if (obj == null) {
            value = null;
        } else {
            value = obj;
        }
        if (value == null && defValue != null) {
            value = defValue;
        }
        return value;
    }

    public void removeMemoryCache(JSONArray param) throws Exception {
        removeMemoryCache(param.getString(0));
    }

    public void removeMemoryCache(String key) throws JSONException {
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

    public void clearMemoryCache(JSONArray param) throws Exception {
        clearMemoryCache();
    }

    public void clearMemoryCache() {
        getMemoryCache().clear();
    }

    public void setOfflineCache(JSONArray param) throws Exception {
        String key = param.getString(0);
        String value = param.getString(1);
        setOfflineCache(key, value);
        callback(value);
    }

    public void setOfflineCache(String key, String value) {
        if (key != null && !"".equals(key)) {
            if (StringUtil.isDataMap(key)) {
                this.sharedPrefHelper.put(Constant.MobileCache.WADE_MOBILE_STORAGE, new DataMap(key));
                return;
            }
            this.sharedPrefHelper.put(Constant.MobileCache.WADE_MOBILE_STORAGE, key, (Object) value);
        }
    }

    public void getOfflineCache(JSONArray param) throws Exception {
        callback(getOfflineCache(param.getString(0), param.getString(1)));
    }

    public String getOfflineCache(String key, String defValue) throws JSONException {
        if (key == null || "".equals(key)) {
            return null;
        }
        if (!StringUtil.isJSONArray(key)) {
            return this.sharedPrefHelper.get(Constant.MobileCache.WADE_MOBILE_STORAGE, key, defValue);
        }
        return this.sharedPrefHelper.get(Constant.MobileCache.WADE_MOBILE_STORAGE, new JSONArray(key)).toString();
    }

    public void removeOfflineCache(JSONArray param) throws Exception {
        removeOfflineCache(param.getString(0));
    }

    public void removeOfflineCache(String key) throws JSONException {
        if (key != null && !"".equals(key)) {
            if (StringUtil.isJSONArray(key)) {
                this.sharedPrefHelper.remove(Constant.MobileCache.WADE_MOBILE_STORAGE, new JSONArray(key));
                return;
            }
            this.sharedPrefHelper.remove(Constant.MobileCache.WADE_MOBILE_STORAGE, key);
        }
    }

    public void clearOfflineCache(JSONArray param) throws Exception {
        clearOfflineCache();
    }

    public void clearOfflineCache() {
        this.sharedPrefHelper.clear(Constant.MobileCache.WADE_MOBILE_STORAGE);
    }

    public void onDestroy() {
        super.onDestroy();
    }
}