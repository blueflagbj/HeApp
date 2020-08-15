package com.wade.mobile.app;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.telephony.TelephonyManager;

import androidx.core.content.ContextCompat;

import com.tencent.tinker.loader.hotplug.EnvConsts;

import java.util.List;

import static androidx.core.content.PermissionChecker.PERMISSION_GRANTED;
import static androidx.core.content.PermissionChecker.checkSelfPermission;

public class MobileUtil {
 /*   public static boolean checkNetWorkActive(Activity context) {
        NetworkInfo[] infos;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService("connectivity");
        if (!(cm == null || (infos = cm.getAllNetworkInfo()) == null)) {
            for (NetworkInfo ni : infos) {
                if (ni.isConnected()) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean checkWifiActive(Activity context) {
        NetworkInfo networkInfo;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService("connectivity");
        if (cm == null || (networkInfo = cm.getActiveNetworkInfo()) == null || networkInfo.getType() != 1) {
            return false;
        }
        return true;
    }*/

/*    public static boolean check3gActive(Context context) {
        NetworkInfo networkInfo;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService("connectivity");
        if (cm == null || (networkInfo = cm.getActiveNetworkInfo()) == null || networkInfo.getType() != 0) {
            return false;
        }
        return true;
    }*/

/*
    public static boolean checkNetWorkConnecting(Activity context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService("connectivity");
        if (cm != null) {
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            if (networkInfo == null) {
                for (NetworkInfo ni : cm.getAllNetworkInfo()) {
                    if (ni.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            } else if (networkInfo.getState() != NetworkInfo.State.CONNECTING) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }
*/

    public static boolean isInBackground(Context context) {
        List<ActivityManager.RunningTaskInfo> tasksInfo = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getRunningTasks(1);
        if (tasksInfo.size() <= 0 || !context.getPackageName().equals(tasksInfo.get(0).topActivity.getPackageName())) {
            return true;
        }
        return false;
    }

    public static boolean isEmulator(Context context) {

        if (checkSelfPermission(context,Context.TELEPHONY_SERVICE)==PERMISSION_GRANTED) {
            String imei = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
            if (imei == null || imei.equals("000000000000000")) {
                return true;
            }
        }
         return false;
    }

    public static boolean isGPSActive(Context context) {
        for (String name : ((LocationManager) context.getSystemService(Context.LOCATION_SERVICE)).getProviders(true)) {
            if ("gps".equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    public static boolean checkGPSActive(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean gps = locationManager.isProviderEnabled("gps");
        boolean network = locationManager.isProviderEnabled("network");
        if (gps || network) {
            return true;
        }
        return false;
    }

    public static boolean checkSDcard() {
        return false;
    }

    public static boolean checkApp(Context context, String packageName) {
        if (packageName == null || "".equals(packageName)) {
            return false;
        }
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                context.getPackageManager().getApplicationInfo(packageName, PackageManager.MATCH_UNINSTALLED_PACKAGES);//8192
            }
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public static boolean checkActivity(Activity context, String packageName, String className) {
        Intent intent = new Intent();
        intent.setClassName(packageName, className);
        if (context.getPackageManager().resolveActivity(intent, 0) != null) {
            return true;
        }
        return false;
    }
}