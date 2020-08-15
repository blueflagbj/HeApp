package com.ai.cmcchina.crm.util;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import java.io.File;
import java.util.List;

import static android.content.Context.WINDOW_SERVICE;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static androidx.constraintlayout.widget.Constraints.TAG;

public class DeviceUtil {
    private static float screenRate = 0.0f;

    @SuppressLint("MissingPermission")
    public static String getImei(Context ctx) {
        TelephonyManager telManager = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);

        return telManager.getDeviceId();
    }

    @SuppressLint("MissingPermission")
    public static String getImsi(Context ctx) {
        return ((TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE)).getSubscriberId();
    }

    public static String getMacAddress(Context ctx) {
        return ((WifiManager) ctx.getSystemService(Context.WIFI_SERVICE)).getConnectionInfo().getMacAddress();
    }

    public static float getScreenRate(Activity context) {
        if (screenRate != 0.0f) {
            return screenRate;
        }
        screenRate = ((float) getScreenWidth(context)) / 480.0f;
        return screenRate;
    }

    public static int dp2Px(Context context, float dp) {
        return (int) ((dp * context.getResources().getDisplayMetrics().density) + 0.5f);
    }

    public static int getStatusBarHeight(Context ctx) {
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            int statusBarHeight = ctx.getResources().getDimensionPixelSize(Integer.parseInt(clazz.getField("status_bar_height").get(clazz.newInstance()).toString()));
            int i = statusBarHeight;
            return statusBarHeight;
        } catch (Exception e1) {
            e1.printStackTrace();
            return 0;
        }
    }

    public static float getDensity(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.density;
    }

    public static int getScreenWidth(Activity activity) {
        return activity.getWindowManager().getDefaultDisplay().getWidth();
    }

    public static int getScreenWidth(Context context) {
        return ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth();
    }

    public static String getDeviceModel() {
        return Build.MODEL;
    }

    public static int getScreenHeight(Activity activity) {
        return activity.getWindowManager().getDefaultDisplay().getHeight();
    }

    public static boolean isSdcardWriteAble() {
        String state = Environment.getExternalStorageState();
        if (!"mounted".equals(state) && !"mounted_ro".equals(state)) {
            return false;
        }
        return true;
    }

    public static int getAndroidSDKVersion() {
        try {
            return Integer.valueOf(Build.VERSION.SDK).intValue();
        } catch (NumberFormatException e) {
            Log.e(TAG, "getAndroidSDKVersion: ",e );
            return 0;
        }
    }

    public static String getAPKVersion(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 1);
            if (info != null) {
                return info.versionName;
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "getAPKVersion: ",e );
        }
        return null;
    }

    public static boolean isPad(Context ctx) {

        Display display = ((WindowManager) ctx.getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        display.getMetrics(dm);
        if (Math.sqrt(Math.pow((double) (((float) dm.widthPixels) / dm.xdpi), 2.0d) + Math.pow((double) (((float) dm.heightPixels) / dm.ydpi), 2.0d)) >= 6.0d) {
            return true;
        }
        return false;
    }

    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout & 15) >= 3;
    }

    public static String getFileRootPath(Context context) {
        File external;
        if (!Environment.getExternalStorageState().equals("mounted") || (external = context.getExternalFilesDir((String) null)) == null) {
            return context.getFilesDir().getAbsolutePath();
        }
        return external.getAbsolutePath();
    }

    public static int sp2Px(float spValue) {
        return (int) ((spValue * Resources.getSystem().getDisplayMetrics().scaledDensity) + 0.5f);
    }

    public static boolean checkAppInstalled(Context context, String pkgName) {
        if (pkgName == null || pkgName.isEmpty()) {
            return false;
        }
        List<PackageInfo> info = context.getPackageManager().getInstalledPackages(0);
        if (info == null || info.isEmpty()) {
            return false;
        }
        for (int i = 0; i < info.size(); i++) {
            if (pkgName.equals(info.get(i).packageName)) {
                return true;
            }
        }
        return false;
    }

    public static void openApp(Context context, String packageName, String className, String appName) throws Exception {
        Intent intent;
        try {
            PackageManager packageManager = context.getPackageManager();
            if (StringUtil.isBlank(className)) {
                intent = packageManager.getLaunchIntentForPackage(packageName);
            } else {
                intent = new Intent();
                intent.setComponent(new ComponentName(packageName, className));
                intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
        } catch (Exception e) {
            Log.d(TAG, "openApp: "+ appName);
            Log.d(TAG, "openApp: "+ packageName);

            throw new Exception("打开第三方应用失败：" + appName);
        }
    }

    public static void openApp(Context context, String scheme, String appName) throws Exception {
        try {
            context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(scheme)));
        } catch (Exception e) {
            Log.e(TAG, "打开第三方应用失败scheme：" + scheme,e );
            Log.e(TAG, "打开第三方应用失败appName：" + appName,e );
            throw new Exception("打开第三方应用失败：" + appName);
        }
    }

    public static void openDownLoadURL(Context context, String downLoadUrl) {
        context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(downLoadUrl)));
    }

}
