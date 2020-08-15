package com.wade.mobile.app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Process;

import com.wade.mobile.app.MobileAppInfo;

public class MobileOperation {
    public static final int GPS_RESULT_CODE = 9002;
    public static final int NETWORK_RESULT_CODE = 9001;

    public static void exitApp() {
        Process.killProcess(Process.myPid());
    }

    public static void exitApp(Activity context) {
        exitApp();
    }


    public static void openNetWork(Activity context) {
        Intent intent;
        if (MobileAppInfo.getOsVersionNumber() > 13) {
            intent = new Intent("android.settings.SETTINGS");
        } else {
            intent = new Intent("android.settings.WIRELESS_SETTINGS");
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);//2097152
        context.startActivityForResult(intent, 9001);
    }

    public static void openGPS(Activity context) throws PendingIntent.CanceledException {
        openGPS(context, false);
    }

    public static void openGPS(Activity context, boolean isForced) throws PendingIntent.CanceledException {
        if (isForced) {
            Intent GPSIntent = new Intent();
            GPSIntent.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            GPSIntent.addCategory("android.intent.category.ALTERNATIVE");
            GPSIntent.setData(Uri.parse("custom:3"));
            PendingIntent.getBroadcast(context, 0, GPSIntent, 0).send();
            return;
        }
        context.startActivityForResult(new Intent("android.settings.LOCATION_SOURCE_SETTINGS"), 9002);
    }
}