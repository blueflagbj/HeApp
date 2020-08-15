package com.wade.mobile.frame.template;

import android.content.ContextWrapper;
import android.os.Handler;
import android.util.Log;

import com.heclient.heapp.App;

import com.wade.mobile.frame.config.MobileConfig;
import com.wade.mobile.frame.config.ServerPageConfig;
import com.wade.mobile.frame.multiple.MultipleManager;
import com.wade.mobile.util.Constant;
import com.wade.mobile.util.CpuArchitecture;
import com.wade.mobile.util.FileUtil;
import com.wade.mobile.util.cipher.DES;
import com.wade.mobile.util.cipher.MD5;
import com.wade.mobile.util.http.HttpTool;
import java.io.File;
import java.security.Key;
import java.util.HashMap;
import java.util.Map;


public class TemplateManager {
    private static String ENCRYPT_DIR = Constant.ATTR_ENCRYPT;//"encrypt"
    private static final String TAG = TemplateManager.class.getSimpleName();
    private static String basePath;
    private static int downloadCount = 0;
    private static int fileCount = 0;
    private static Map<String, String> multipleBasePaths = new HashMap();
    private static Map<String, Key> multipleResKeys = new HashMap();
    private static Key resKey;

    public static void downloadResource() throws Exception {
        downloadResource((Handler) null, (ContextWrapper) null);
    }

    public static void downloadResource(Handler handler, ContextWrapper context) throws Exception {
        System.out.println("TemplateManager-downloadResource:0:");
        Map<String, ?> remoteResVersions;
        long start = System.currentTimeMillis();
        if (MultipleManager.isMultiple()) {
            remoteResVersions = ResVersionManager.multipleRemoteResVersions.get(MultipleManager.getCurrAppId());
            System.out.println("TemplateManager-downloadResource:0-1:"+remoteResVersions.toString());
        } else {
            remoteResVersions = ResVersionManager.remoteResVersions;
            System.out.println("TemplateManager-downloadResource:0-2:"+remoteResVersions.toString());
        }
        System.out.println("TemplateManager-downloadResource:1:");
        fileCount = remoteResVersions.size();
        for (String obj : remoteResVersions.keySet()) {
            if (!Thread.currentThread().isInterrupted()) {
                checkResource(obj.toString(), context, handler);
            } else {
                return;
            }
        }
        Log.d(TAG, "downloadResource: resource download time : " + (System.currentTimeMillis() - start));

        ServerPageConfig.getInstance();
    }

    public static void checkResource(String path, ContextWrapper context, Handler handler) throws Exception {
        String downPath;
        System.out.println("checkResource0001:"+path);
        String cpupath =CpuArchitecture.CPU_ARCHITECTURE_PATH;
        System.out.println("checkResource0002:"+cpupath);
        if (!path.startsWith("libs"+ File.separator) || path.startsWith(CpuArchitecture.CPU_ARCHITECTURE_PATH)) {
            System.out.println("checkResource0003:"+path);
            if (path.startsWith(ENCRYPT_DIR)) {
                System.out.println("checkResource0003-1:"+path);
                downPath = FileUtil.connectFilePath(getBasePath(), path.substring(8));
            } else {
                System.out.println("checkResource0003-2:"+path);
                String bp=getBasePath();
                System.out.println("checkResource0003-3:"+bp);
                downPath = FileUtil.connectFilePath(getBasePath(), path);
            }
            if (!new File(downPath).exists()) {
                String dirPath = downPath.substring(0, downPath.lastIndexOf(Constant.FILE_SEPARATOR));
                if (!FileUtil.check(dirPath)) {
                    FileUtil.createDir(dirPath);
                }
            } else if (!Constant.TRUE.equals(MobileConfig.getInstance().getConfigValue(MobileConfig.IS_DEBUG))) {
                System.out.println("TemplateManager-checkResource:2:"+path);
                if (ResVersionManager.getRemoteResVersion(path).equals(ResVersionManager.getLocalResVersion(context, path))) {
                    return;
                }
                if (ResVersionManager.getRemoteResVersion(path).equals(MD5.hexDigestByFile(downPath))) {
                    ResVersionManager.setLocalResVersion(context, path, ResVersionManager.getRemoteResVersion(path));
                    return;
                }
            }
            HttpTool.httpDownload(FileUtil.connectFilePath(getBaseUrl(), path), downPath);
            Log.d(TAG, "checkResource:download resource : " + downPath);
            ResVersionManager.setLocalResVersion(context, path, ResVersionManager.getRemoteResVersion(path));
            downloadCount++;
            if (handler != null) {
                handler.sendEmptyMessage(2);
                return;
            }
            return;
        }
        System.out.println("checkResource0006:"+cpupath);
        ResVersionManager.setLocalResVersion(context, path, ResVersionManager.getRemoteResVersion(path));
    }

    public static void initBasePath(String baseResPath) {
        System.out.println("TemplateManager-initBasePath:0:"+baseResPath);
        if (MultipleManager.isMultiple()) {
            System.out.println("TemplateManager-initBasePath:1:"+MultipleManager.getCurrAppId());
            multipleBasePaths.put(MultipleManager.getCurrAppId(), baseResPath);
        } else {
            basePath = baseResPath;
        }
        System.out.println("TemplateManager-initBasePath:6:"+basePath);
    }

    public static String getBasePath() {
        System.out.println("TemplateManager-getBasePath:0:");
        if (MultipleManager.isMultiple()) {
            System.out.println("TemplateManager-getBasePath:1:"+multipleBasePaths.get(MultipleManager.getCurrAppId()));
            return multipleBasePaths.get(MultipleManager.getCurrAppId());
        }
        System.out.println("TemplateManager-getBasePath:6:"+basePath);
        return basePath;
    }

    public static String getBaseUrl() {
        return MobileConfig.getInstance().getRequestBaseUrl();
    }

    public static void initResKey(String resKey2) throws Exception {
        if (MultipleManager.isMultiple()) {
            multipleResKeys.put(MultipleManager.getCurrAppId(), DES.getKey(resKey2));
        } else {
            resKey = DES.getKey(resKey2);
        }
    }

    public static Key getResKey() {
        if (MultipleManager.isMultiple()) {
            return multipleResKeys.get(MultipleManager.getCurrAppId());
        }
       // System.out.println("TemplateManager-getResKey:"+resKey);
        return resKey;
    }

    public static int getDownloadCount() {
        return downloadCount;
    }

    public static void resetDownloadPercent() {
        downloadCount = 0;
        fileCount = 0;
    }

}
