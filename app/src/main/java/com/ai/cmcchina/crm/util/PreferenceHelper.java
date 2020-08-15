package com.ai.cmcchina.crm.util;

import android.util.Log;

import com.ailk.common.data.impl.DataMap;

import com.heclient.heapp.App;
import com.heclient.heapp.util.DesPlus;
import com.wade.mobile.common.MobileCache;
import com.wade.mobile.util.Constant;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class PreferenceHelper {
    public static void commit(String key, String value) {
        if (key != null && value != null) {
            try {
                App.getSharedPreferences().edit().putString(key, new DesPlus().encrypt(value)).apply();
            } catch (Exception e) {
                Log.e(TAG, "commit: ",e);
                App.getSharedPreferences().edit().putString(key, value).apply();
            }
        }
    }
    public static void commitDirect(String key, String value) {
        if (key != null && value != null) {
            try {
                App.getSharedPreferences().edit().putString(key, value).apply();
            } catch (Exception e) {
                Log.e(TAG, "commit: ",e);
            }
        }
    }
    public static String get(String key, String defaultValue) {
        try {
            return new DesPlus().decrypt(App.getSharedPreferences().getString(key, defaultValue));
        } catch (Exception e) {
            Log.d(TAG, "get: "+ Arrays.toString(e.getStackTrace()));
            return defaultValue;
        }
    }
    public static String getDirect(String key, String defaultValue) {
        try {
            return App.getSharedPreferences().getString(key, defaultValue);
        } catch (Exception e) {
            Log.d(TAG, "get: "+ Arrays.toString(e.getStackTrace()));
            return defaultValue;
        }
    }
    public static void commitWithKeyEncry(String key, boolean value) {
        try {
            App.getSharedPreferences().edit().putBoolean(new DesPlus().encrypt(key), value).apply();
        } catch (Exception e) {
            Log.e(TAG, "commitWithKeyEncry: ",e );
            App.getSharedPreferences().edit().putBoolean(key, value).apply();
        }
    }

    public static boolean getWithKeyEncry(String key, boolean defaultValue) {
        try {
            return App.getSharedPreferences().getBoolean(new DesPlus().encrypt(key), defaultValue);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static void commitWithKeyValueEncry(String key, String value) {
        try {
            App.getSharedPreferences().edit().putString(new DesPlus().encrypt(key), new DesPlus().encrypt(value)).apply();
        } catch (Exception e) {
            Log.e(TAG, "commitWithKeyValueEncry: ", e);
            App.getSharedPreferences().edit().putString(key, value).apply();
        }
    }

    public static String getWithKeyValueEncry(String key, String defaultValue) {
        try {
            return new DesPlus().decrypt(App.getSharedPreferences().getString(new DesPlus().encrypt(key), defaultValue));
        } catch (Exception e) {
            return defaultValue;
        }
    }

    private static Map getMemoryCache() {
        Object map = MobileCache.getInstance().get(Constant.MobileCache.WADE_MOBILE_STORAGE);
        if (map != null) {
            return (HashMap) map;
        }
        Map cache = new HashMap();
        MobileCache.getInstance().put(Constant.MobileCache.WADE_MOBILE_STORAGE, cache);
        return cache;
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
}