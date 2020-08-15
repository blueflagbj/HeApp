package com.wade.mobile.frame.template;
import android.content.ContextWrapper;

import com.alibaba.fastjson.JSONObject;
import com.wade.mobile.frame.multiple.MultipleManager;
import com.wade.mobile.helper.SharedPrefHelper;
import com.wade.mobile.util.Constant;
import com.wade.mobile.util.FileUtil;
import com.wade.mobile.util.MobileProperties;
import com.wade.mobile.util.http.HttpTool;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class ResVersionManager {
    private static final String LOCAL_RES_VERSION = "LOCAL_RES_VERSION";
    static Map<String, String> localResVersions;
    static Map<String, Map<String, String>> multipleLocalResVersions = new HashMap();
    static Map<String, Map<String, ?>> multipleRemoteResVersions = new HashMap();
    static Map<String, ?> remoteResVersions;
    public static int updateCount = 0;

    public static void setLocalResVersion(ContextWrapper context, String resPath, String resVersion) {
        getLocalResVersions(context).put(resPath, resVersion);
        if (MultipleManager.isMultiple()) {
            new SharedPrefHelper(context).put("LOCAL_RES_VERSION_" + MultipleManager.getCurrAppId(), resPath, (Object) resVersion);
        } else {
            new SharedPrefHelper(context).put("LOCAL_RES_VERSION", resPath, (Object) resVersion);
        }
    }

    public static String getLocalResVersion(ContextWrapper context, String resPath) {
        return getLocalResVersions(context).get(resPath);
    }

    public static void removeLocalResVersion(ContextWrapper context, String resPath) {
        getLocalResVersions(context).remove(resPath);
        if (MultipleManager.isMultiple()) {
            new SharedPrefHelper(context).remove("LOCAL_RES_VERSION_" + MultipleManager.getCurrAppId(), resPath);
        } else {
            new SharedPrefHelper(context).remove("LOCAL_RES_VERSION", resPath);
        }
    }

    public static Map<String, String> getLocalResVersions(ContextWrapper context) {
        if (MultipleManager.isMultiple()) {
            Map map = multipleLocalResVersions.get(MultipleManager.getCurrAppId());
            if (map != null) {
                return map;
            }
            try {
                map = new SharedPrefHelper(context).getAll("LOCAL_RES_VERSION_" + MultipleManager.getCurrAppId());
            } catch (Exception e) {
            }
            if (map == null) {
                map = new HashMap();
            }
            multipleLocalResVersions.put(MultipleManager.getCurrAppId(), map);
            return map;
        }
        if (localResVersions == null) {
            Map localResVersionsMap = new SharedPrefHelper(context).getAll("LOCAL_RES_VERSION");
            localResVersions =localResVersionsMap;
        }
        return localResVersions;
    }

    public static boolean isUpdateResource(ContextWrapper context, Map<String, ?> remoteResVersions2) throws Exception {
      System.out.println("ResVersionManager-isUpdateResource:0:"+remoteResVersions2.toString());
        if (MultipleManager.isMultiple()) {
            Map<String, String> localResVersions2 = getLocalResVersions(context);
            Map<String, ?> subAppRemoteResVersionsMap = getRemoteResVersions();
            Set<String> itLocalDel = new HashSet<>();
            for (String keyLocal : localResVersions2.keySet()) {
                if (!subAppRemoteResVersionsMap.containsKey(keyLocal)) {
                    itLocalDel.add(keyLocal);
                }
            }
            for (String resPath : itLocalDel) {
                removeLocalResVersion(context, resPath);
            }
            updateCount = 0;
            for (Object key : subAppRemoteResVersionsMap.keySet()) {
                if (!localResVersions2.containsKey(key)) {
                    updateCount++;
                } else if (!subAppRemoteResVersionsMap.get(key).equals(localResVersions2.get(key))) {
                    updateCount++;
                }
            }
            if (updateCount > 0) {
                return true;
            }
            return false;
        }
        Map<String, String> localResVersions3 = getLocalResVersions(context);
        Set<String> itLocalDel2 = new HashSet<>();
        for (String keyLocal2 : localResVersions3.keySet()) {
            if (!remoteResVersions2.containsKey(keyLocal2)) {
                itLocalDel2.add(keyLocal2);
            }
        }
        for (String resPath2 : itLocalDel2) {
            removeLocalResVersion(context, resPath2);
        }
        updateCount = 0;
        for (Object key2 : remoteResVersions2.keySet()) {
            if (!localResVersions3.containsKey(key2)) {
                updateCount++;
            } else if (!remoteResVersions2.get(key2).equals(localResVersions3.get(key2))) {
                updateCount++;
            }
        }
        if (updateCount <= 0) {
            return false;
        }
        return true;
    }

    public static Map<String, ?> getRemoteResVersions() throws Exception {
        String url =TemplateManager.getBaseUrl();
        String fileUrl=FileUtil.connectFilePath(url, Constant.Server.RES_VERSION_CONFIG);
       // System.out.println("00051"+url);
       // System.out.println("00052"+fileUrl);

        HttpTool.httpDownload(fileUrl, new HttpTool.DownStreamOper() {
            public void downloading(InputStream in) throws Exception {
              //  System.out.println("00053");
                Properties properties = new Properties();
                properties.load(in);
               // System.out.println("000531"+properties.toString());
                Map<String, String> map = new HashMap<String, String>((Map) properties);
              //  System.out.println("000532"+map.toString());
                Set propertySet = map.entrySet();
                Map<String, Object> pro = new HashMap<String, Object>();
                for (Object o : propertySet) {
                    Map.Entry entry = (Map.Entry) o;
                    pro.put(entry.getKey().toString(), entry.getValue());
                   // System.out.printf("%s = %s%n", entry.getKey(), entry.getValue());
                }
                if (MultipleManager.isMultiple()) {
                  //  System.out.println("000533"+pro.toString());
                    ResVersionManager.multipleRemoteResVersions.put(MultipleManager.getCurrAppId(), pro);

                    return;
                }
                //System.out.println("000534"+pro.toString());
                ResVersionManager.remoteResVersions = pro;
            }
        });
       // System.out.println("00055");
        if (MultipleManager.isMultiple()) {
            return multipleRemoteResVersions.get(MultipleManager.getCurrAppId());
        }
        return remoteResVersions;
    }

    public static String getRemoteResVersion(String resPath) throws Exception {
        if (MultipleManager.isMultiple()) {
            return (String) multipleRemoteResVersions.get(MultipleManager.getCurrAppId()).get(resPath);
        }
        return (String) (remoteResVersions.get(resPath) == null ? "" : remoteResVersions.get(resPath));
    }

}
