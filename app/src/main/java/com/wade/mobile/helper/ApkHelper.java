package com.wade.mobile.helper;


import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ApkHelper {
    private static String FILE_TYPE = ".apk";
    private static String MAIN_PATH = "/data/app/";
    private static String MINOR_PATH = "/mnt/asec/";
    private Context context;
    /* access modifiers changed from: private */
    public PackageManager packageManager;

    public ApkHelper(Context context2) {
        this.context = context2;
        this.packageManager = context2.getPackageManager();
    }

    public List<ApkInfo> getAllAppInfo() {
        List<ApkInfo> appInfoList = new ArrayList<>();
        List<PackageInfo> pkgInfolist = this.packageManager.getInstalledPackages(0);
        for (int i = 0; i < pkgInfolist.size(); i++) {
            PackageInfo pkgInfo = pkgInfolist.get(i);
            if ((pkgInfo.applicationInfo.flags & 1) <= 0) {
                appInfoList.add(new ApkInfo(pkgInfo));
            }
        }
        return appInfoList;
    }

    public ApkInfo getAppInfo(String packageName) {
        return new ApkInfo(this.context.getPackageManager().getPackageArchiveInfo(getFile(packageName).getAbsolutePath(), PackageManager.GET_ACTIVITIES));
    }

    public static File getFile(String packageName) {
        File file = new File(MAIN_PATH + packageName + FILE_TYPE);
        if (file.exists()) {
            File file2 = file;
            return file;
        }
        for (int i = 9; i > 0; i--) {
            File file3 = new File(MAIN_PATH + packageName + "-" + i + FILE_TYPE);
            if (file3.exists()) {
                File file4 = file3;
                return file3;
            }
        }
        File file5 = new File(MAIN_PATH + packageName);
        if (!file5.exists() || !file5.isDirectory()) {
            int i2 = 9;
            while (i2 > 0) {
                file5 = new File(MAIN_PATH + packageName + "-" + i2);
                if (!file5.exists() || !file5.isDirectory()) {
                    i2--;
                } else {
                    File file6 = file5;
                    return new File(file5, "base.apk");
                }
            }
            for (File f : new File(MINOR_PATH).listFiles()) {
                if (f.getName().contains(packageName)) {
                    file5 = new File(f, "pkg.apk");
                    if (file5.exists()) {
                        File file7 = file5;
                        return file5;
                    }
                }
            }
            File file8 = file5;
            return null;
        }
        File file9 = file5;
        return new File(file5, "base.apk");
    }

    public class ApkInfo {
        private File apk;
        private PackageInfo packageInfo;

        public ApkInfo(PackageInfo packageInfo2) {
            this.packageInfo = packageInfo2;
        }

        public PackageInfo getPackageInfo() {
            return this.packageInfo;
        }

        public File getApk() {
            if (this.apk == null) {
                this.apk = ApkHelper.getFile(this.packageInfo.packageName);
            }
            return this.apk;
        }

        public String getAppName() {
            return ApkHelper.this.packageManager.getApplicationLabel(this.packageInfo.applicationInfo).toString();
        }

        public String getPackageName() {
            return this.packageInfo.applicationInfo.packageName;
        }

        public Drawable getIcon() {
            return this.packageInfo.applicationInfo.loadIcon(ApkHelper.this.packageManager);
        }
    }
}