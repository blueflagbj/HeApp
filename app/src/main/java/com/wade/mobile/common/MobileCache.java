package com.wade.mobile.common;

import com.heclient.heapp.App;

import java.util.HashMap;
import java.util.Map;

public class MobileCache {
    private static MobileCache cache;
    private Map<String, Object> cacheMap;

    private MobileCache() {
        if (App.getInstance() != null) {
          //  System.out.println("MobileCache-MobileCache:0:");
            this.cacheMap = App.getInstance().getGlobalCacheMap();
        } else {
          //  System.out.println("MobileCache-MobileCache:2:");
            this.cacheMap = new HashMap();
        }
    }

    public static MobileCache getInstance() {
        if (cache == null) {
            synchronized (MobileCache.class) {
                if (cache == null) {
                    cache = new MobileCache();
                }
            }
        }
        return cache;
    }

    public Object get(String key) {
        return this.cacheMap.get(key);
    }

    public void put(String key, Object obj) {
        if (this.cacheMap != null) {
            this.cacheMap.put(key, obj);
        }
    }

    public void clear() {
        if (this.cacheMap != null) {
            this.cacheMap.clear();
        }
    }

    public void remove(String key) {
        if (this.cacheMap.get(key) != null) {
            this.cacheMap.remove(key);
        }
    }

}
