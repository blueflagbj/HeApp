package com.wade.mobile.util;

import com.ailk.common.data.IData;
import com.ailk.common.data.impl.DataMap;
import com.wade.mobile.common.MobileCache;

public class BusinessCache {
    private static BusinessCache cache;
    private final String BUSINESS_CACHE = "BUSINESS_CACHE";

    private BusinessCache() {
        MobileCache.getInstance().put("BUSINESS_CACHE", new DataMap());
    }

    public static BusinessCache getInstance() {
        if (cache == null) {
            synchronized (BusinessCache.class) {
                cache = new BusinessCache();
            }
        }
        return cache;
    }

    private IData getCache() {
        if (MobileCache.getInstance().get("BUSINESS_CACHE") == null) {
            MobileCache.getInstance().put("BUSINESS_CACHE", new DataMap());
        }
        return (IData) MobileCache.getInstance().get("BUSINESS_CACHE");
    }

    public Object get(String key) {
        Object obj;
        synchronized (key) {
            obj = getCache().get(key);
        }
        return obj;
    }

    public void put(String key, Object obj) {
        synchronized (key) {
            getCache().put(key, obj);
        }
    }

    public void remove(String key) {
        synchronized (key) {
            if (getCache().get(key) != null) {
                getCache().remove(key);
            }
        }
    }

    public void clear() {
        getCache().clear();
    }
}