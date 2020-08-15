package com.wade.mobile.app;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Environment;
import com.wade.mobile.common.MobileException;
import com.wade.mobile.frame.config.MobileConfig;
import com.wade.mobile.helper.ApkHelper;
import com.wade.mobile.util.Messages;
import java.io.File;

public class MobileAppInfo {
    public static final int Android_1_5 = 3;
    public static final int Android_1_6 = 4;
    public static final int Android_2_1 = 5;
    public static final int Android_2_2 = 8;
    public static final int Android_2_3 = 9;
    public static final int Android_2_3_3 = 10;
    public static final int Android_3_0 = 11;
    public static final int Android_3_1 = 12;
    public static final int Android_3_2 = 13;
    public static final int Android_4_0 = 14;
    public static final int Android_4_0_3 = 15;
    public static final int Android_4_1_2 = 16;
    public static final int Android_4_2_2 = 17;
    public static final int Android_4_3 = 18;
    public static final int Android_4_4 = 19;
    private static MobileAppInfo info;
    private Context context;

    private MobileAppInfo(Context context2) {
        this.context = context2;
    }

    public static MobileAppInfo getInstance(Context context2) {
        if (info == null) {
            info = new MobileAppInfo(context2);
        }
        return info;
    }

    public boolean isTablet(Context context2) {
        if (Build.VERSION.SDK_INT < 11) {
            return false;
        }
        Configuration con = context2.getResources().getConfiguration();
        try {
            return ((Boolean) con.getClass().getMethod("isLayoutSizeAtLeast", new Class[]{Integer.TYPE}).invoke(con, new Object[]{4})).booleanValue();
        } catch (Exception e) {
            return false;
        }
    }

    public String getAppName() {
        return this.context.getPackageManager().getApplicationLabel(this.context.getApplicationInfo()).toString();
    }

    public String getVersionName() throws PackageManager.NameNotFoundException {
        return this.context.getPackageManager().getPackageInfo(this.context.getPackageName(), 0).versionName;
    }

    public int getVersionCode() throws PackageManager.NameNotFoundException {
        return this.context.getPackageManager().getPackageInfo(this.context.getPackageName(), 0).versionCode;
    }

    public String getClassName() {
        return ((Activity) this.context).getLocalClassName();
    }

    public String getSourceDir() {
        return this.context.getPackageResourcePath();
    }

    public String getProcessName() {
        return this.context.getApplicationInfo().processName;
    }

    public String getPackageName() {
        return this.context.getPackageName();
    }

    public static String getOsVersion() {
        return Build.VERSION.RELEASE;
    }

    public static int getOsVersionNumber() {
        return Build.VERSION.SDK_INT;
    }

    public File getApk() {
        return ApkHelper.getFile(getPackageName());
    }

    public Drawable getIcon() {
        return this.context.getApplicationInfo().loadIcon(this.context.getPackageManager());
    }

    public static String getSdcardPath() {
        if (Environment.getExternalStorageState().equals("mounted")) {
            return Environment.getExternalStorageDirectory().toString();
        }
        throw new MobileException(Messages.SDCARD_NOT_EXIST);
    }

    public String getAppPath() {
        String appPath = MobileConfig.getInstance().getAppPath();
        if ("".equals(appPath)) {
            return getInstance(this.context).getAppName();
        }
        return appPath;
    }

    public  String getSdcardAppPath() {
        return getSdcardPath() + File.separator + getInstance(this.context).getAppPath();
    }

    public static String getSdcardAppPath(Context context2) {
        return getInstance(context2).getSdcardAppPath();
    }

    public String getAssetsAppPath() {
        return "assets" + File.separator + getInstance(this.context).getAppPath();
    }
}