package com.wade.mobile.app;

import android.content.ContextWrapper;
import android.content.pm.PackageManager;
import com.wade.mobile.frame.multiple.MultipleManager;
import com.wade.mobile.helper.SharedPrefHelper;
import com.wade.mobile.util.Constant;

public class AppRecord {
    private static final String APP_RECORD = "APP_RECORD";
    private static final String CLIENT_VERSION = "CLIENT_VERSION";
    private static final String IS_FIRST = "IS_FIRST";
    private static final String LUA_VERSION = "LUA_VERSION";
    private static final String RESOURCE_VERSION = "RESOURCE_VERSION";

    public static boolean isFirst(ContextWrapper context) {
        if (MultipleManager.isMultiple()) {
            if (new SharedPrefHelper(context).get("APP_RECORD_" + MultipleManager.getCurrAppId(), IS_FIRST, (String) null) == null) {
                return true;
            }
            return false;
        } else if (new SharedPrefHelper(context).get("APP_RECORD", IS_FIRST, (String) null) != null) {
            return false;
        } else {
            return true;
        }
    }

    public static void dirtyFirst(ContextWrapper context) {
        if (MultipleManager.isMultiple()) {
            new SharedPrefHelper(context).put("APP_RECORD_" + MultipleManager.getCurrAppId(), IS_FIRST, (Object) Constant.FALSE);
        } else {
            new SharedPrefHelper(context).put("APP_RECORD", IS_FIRST, (Object) Constant.FALSE);
        }
    }

    public static void setLuaVersion(ContextWrapper context) throws PackageManager.NameNotFoundException {
        new SharedPrefHelper(context).put("APP_RECORD", LUA_VERSION, (Object) MobileAppInfo.getInstance(context).getVersionName());
        if (MultipleManager.isMultiple()) {
            new SharedPrefHelper(context).put("APP_RECORD_" + MultipleManager.getCurrAppId(), LUA_VERSION, (Object) MobileAppInfo.getInstance(context).getVersionName());
        } else {
            new SharedPrefHelper(context).put("APP_RECORD", LUA_VERSION, (Object) MobileAppInfo.getInstance(context).getVersionName());
        }
    }

    public static String getLuaVersion(ContextWrapper context) {
        if (MultipleManager.isMultiple()) {
            return new SharedPrefHelper(context).get("APP_RECORD_" + MultipleManager.getCurrAppId(), LUA_VERSION, "");
        }
        return new SharedPrefHelper(context).get("APP_RECORD", LUA_VERSION, "");
    }

    public static void setResVersion(ContextWrapper context, String resVersion) {
        new SharedPrefHelper(context).put("APP_RECORD", RESOURCE_VERSION, (Object) resVersion);
    }

    public static String getResVersion(ContextWrapper context) {
        return new SharedPrefHelper(context).get("APP_RECORD", RESOURCE_VERSION, "");
    }

    public static void setClientVersion(ContextWrapper context, String clientVersion) {
        new SharedPrefHelper(context).put("APP_RECORD", CLIENT_VERSION, (Object) clientVersion);
    }

    public static String getClientVersion(ContextWrapper context) {
        return new SharedPrefHelper(context).get("APP_RECORD", CLIENT_VERSION, "");
    }
}