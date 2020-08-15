package com.heclient.heapp;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.DisplayMetrics;
import android.util.Log;

import androidx.preference.PreferenceManager;

import com.ai.cmcchina.crm.bean.Host;
import com.ai.cmcchina.crm.util.PreferenceHelper;
import com.alibaba.fastjson.JSONArray;
import com.birbit.android.jobqueue.JobManager;
import com.birbit.android.jobqueue.config.Configuration;
import com.birbit.android.jobqueue.log.CustomLogger;
import com.heclient.heapp.db.DaoMaster;
import com.heclient.heapp.db.DaoSession;

import org.greenrobot.greendao.database.Database;

import java.security.Key;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.spec.SecretKeySpec;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.wade.mobile.util.cipher.RSA.loadPublicKey;

public class App extends Application {

    public static final boolean DEBUG = true;
    public static final float HEAP_UTILIZATION = 0.75f;
    public static final int MIN_HEAP_SIZE = 6291456;
    private static SharedPreferences sharedPreferences;
    private final String BUGLY_APP_ID = "141febdf2e";
    private String accountChannelType;

    private Application app;
    private String appsConfig;
    private String crmServerPath;
    private JSONArray currentMenus = new JSONArray();

    private Map<String, String> idCardImageInfoMap;

    private Boolean isCollectedChange = false;
    private List<Activity> needFinishActivityList;
    private String num = "";

    private Map<String, Object> params = new HashMap();
    private Map<String, Object> cacheMap = new HashMap();
    private String partnerAccountType;

    private String partnerBossAccount = "";
    private String partnerServerPath;
    private String esopServerPath;
    private String randomKey;
    private String defaultKey="G3YYT";
    private RSAPublicKey publicKey;
    private RSAPublicKey crmPublicKey;
    private Key DesKey=null;

    private String sessionId;
    private String staffRegionId;
    private String tingId = "";
    private String tingName = "";
    private String opName = "";
    private String userFullName = "";

    private static App _instance;
    private JobManager jobManager; //任务队列

    public String DB_NAME ="he-db" ;
    public DaoSession daoSession; //提供全局的数据库会话
    public DaoMaster daoMaster;

    @Override
    public void onCreate() {
        super.onCreate();
        this.app = this;
        set_instance(this);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        setPublicKey(null);//初始化PublicKey
        setDesKey("");//初始密码"G3YYT"生成deskey
        initGreenDao(); //初始化GreenDao,保持全局的会话
        getJobManager();// ensure it is created 确保创建成功

    }

    private void configureJobManager() {
        Configuration.Builder builder = new Configuration.Builder(this.app)
                .customLogger(new CustomLogger() {
                    private static final String TAG = "JOBS";
                    @Override
                    public boolean isDebugEnabled() {
                        return true;
                    }

                    @Override
                    public void d(String text, Object... args) {
                        Log.d(TAG, String.format(text, args));
                    }

                    @Override
                    public void e(Throwable t, String text, Object... args) {
                        Log.e(TAG, String.format(text, args), t);
                    }

                    @Override
                    public void e(String text, Object... args) {
                        Log.e(TAG, String.format(text, args));
                    }

                    @Override
                    public void v(String text, Object... args) {

                    }
                })
                .minConsumerCount(1)//always keep at least one consumer alive
                .maxConsumerCount(3)//up to 3 consumers at a time
                .loadFactor(3)//3 jobs per consumer
                .consumerKeepAlive(120);//wait 2 minute
        jobManager = new JobManager(builder.build());
    }

    public static void set_instance(App _instance) {
        App._instance = _instance;
    }

    public static void setSharedPreferences(SharedPreferences sharedPreferences) {
        App.sharedPreferences = sharedPreferences;
    }

    public static App getInstance() {
        return _instance;
    }
    public Map<String, Object> getGlobalCacheMap() {
        return this.cacheMap;
    }
    /**
     * 初始化GreenDao,直接在Application中进行初始化操作
     */
    private void initGreenDao() {

        // regular SQLite database
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, DB_NAME);
        Database db = helper.getWritableDb();

        // encrypted SQLCipher database
        // note: you need to add SQLCipher to your dependencies, check the build.gradle file
        // DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "notes-db-encrypted");
        // Database db = helper.getEncryptedWritableDb("encryption-key");

        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }

    public DaoMaster getDaoMaster(){
        return daoMaster;
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getCrmServerPath() {
        return crmServerPath;
    }

    public void setCrmServerPath(String crmServerPath) {
        this.crmServerPath = crmServerPath;
    }

    public String getRandomKey() {
        return randomKey;
    }

    public void setRandomKey(String randomKey) {
        this.randomKey = randomKey;
    }

    public String getDefaultKey() {
        return defaultKey;
    }


    public RSAPublicKey getPublicKey() {
        return publicKey;
    }

    public void setDesKey(Key desKey) {
        DesKey = desKey;
    }
    public void setDefaultKey(String defaultKey) {
        if(defaultKey==""){
            String s =(new Host()).getKey();
           // Log.d(TAG, "Key: "+s);
            String[] arrayOfString = (new Host()).getKey().split(",");
            byte[] arrayOfByte = new byte[arrayOfString.length];
            for (byte b = 0; b < arrayOfString.length; b++)
                arrayOfByte[b] = Byte.valueOf(arrayOfString[b]).byteValue();
            this.defaultKey=new String(arrayOfByte);
           // Log.d(TAG, "strDefaultKey: "+ this.defaultKey);
        }else{
            this.defaultKey = defaultKey;
        }
    }
    public void createRandomKey() {
        double d = Math.random();
        String randomStr =String.valueOf((8.9999999E7d * Math.random()) + 1.0E7d);
      //  Log.d(ContentValues.TAG, "App-createRandomKey: "+randomStr);
        setRandomKey(randomStr);
    }

    public void setPublicKey(RSAPublicKey publicKey){
        if(publicKey==null){
            try {
                publicKey = loadPublicKey(this.getResources().openRawResource(R.raw.public_key));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        this.publicKey = publicKey;
    }

    public Key getDesKey() {
       // Log.d(TAG, "getDesKey: 0:");
      //  Log.d(TAG, "getDesKey: 0-1"+this.DesKey.toString());
        if(this.DesKey==null){
            byte[] arrBTmp=defaultKey.getBytes();
            byte[] arrB = new byte[8];
            int i = 0;
            while (i < arrBTmp.length && i < arrB.length) {
                arrB[i] = arrBTmp[i];
                i++;
            }
           // Log.d(TAG, "getDesKey: 1:");
            this.DesKey=new SecretKeySpec(arrB, "DES");
           // Log.d(TAG, "getDesKey: 2:");
        }
       // Log.d(TAG, "getDesKey:3:");
        return this.DesKey;
    }

    public void setDesKey(String desKey) {
        byte[] arrBTmp;
        if (desKey==""){
            arrBTmp =defaultKey.getBytes();
        }else{
            arrBTmp =desKey.getBytes();
        }
        byte[] arrB = new byte[8];
        int i = 0;
        while (i < arrBTmp.length && i < arrB.length) {
            arrB[i] = arrBTmp[i];
            i++;
        }
        this.DesKey=new SecretKeySpec(arrB, "DES");

    }
    public static SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    public JSONArray getCurrentMenus() {
        return currentMenus;
    }

    public void setCurrentMenus(JSONArray currentMenus) {
        this.currentMenus = currentMenus;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }



    public String getPartnerServerPath() {
        return partnerServerPath;
    }

    public void setPartnerServerPath(String partnerServerPath) {
        this.partnerServerPath = partnerServerPath;
    }

    public String getEsopServerPath() {
        return esopServerPath;
    }

    public void setEsopServerPath(String esopServerPath) {
        this.esopServerPath = esopServerPath;
    }

    public String getPartnerAccountType() {
        return partnerAccountType;
    }

    public void setPartnerAccountType(String partnerAccountType) {
        this.partnerAccountType = partnerAccountType;
    }

    public Boolean getIsCollectedChange() {
        return isCollectedChange;
    }

    public void setIsCollectedChange(Boolean collectedChange) {
        isCollectedChange = collectedChange;
    }

    public String getTingName() {
        return tingName;
    }

    public void setTingName(String tingName) {
        this.tingName = tingName;
    }

    public String getTingId() {
        return tingId;
    }

    public void setTingId(String tingId) {
        this.tingId = tingId;
    }

    public String getPartnerBossAccount() {
        return partnerBossAccount;
    }

    public void setPartnerBossAccount(String partnerBossAccount) {
        this.partnerBossAccount = partnerBossAccount;
    }

    public String getOpName() {
        return opName;
    }

    public void setOpName(String opName) {
        this.opName = opName;
    }

    public Map<String, String> getIdCardImageInfoMap() {
        return idCardImageInfoMap;
    }

    public void setIdCardImageInfoMap(Map<String, String> idCardImageInfoMap) {
        this.idCardImageInfoMap = idCardImageInfoMap;
    }

    public RSAPublicKey getCrmPublicKey() {
        return crmPublicKey;
    }

    public void setCrmPublicKey(RSAPublicKey crmPublicKey) {
        this.crmPublicKey = crmPublicKey;
    }


    public synchronized JobManager getJobManager() {
        if (jobManager == null) {
         //   System.out.println("创建jobmanager");
            configureJobManager();
        }
        return jobManager;
    }
    public void setJobManager(JobManager jobManager) {
        this.jobManager = jobManager;
    }

    public static class Msg {
        public static final int EXAMPLE = 1985;
    }

    public void addNeedFinishedActivity(Activity activity) {
        if (this.needFinishActivityList == null) {
            this.needFinishActivityList = new ArrayList();
        }
        if (!this.needFinishActivityList.contains(activity)) {
            this.needFinishActivityList.add(activity);
        }
    }

    public boolean removeActivityFromList(Activity activity) {
        if (this.needFinishActivityList == null) {
            return false;
        }
        return this.needFinishActivityList.remove(activity);
    }

    public boolean removeAllActivityFromList() {
        if (this.needFinishActivityList == null) {
            return false;
        }
        for (int i = 0; i < this.needFinishActivityList.size(); i++) {
            this.needFinishActivityList.remove(i);
        }
        return true;
    }

    @TargetApi(17)
    public void finishAllNeedActivity() {
        if (this.needFinishActivityList != null) {
            for (int i = 0; i < this.needFinishActivityList.size(); i++) {
                Activity activity = this.needFinishActivityList.get(i);
                if (!activity.isDestroyed()) {
                    activity.finish();
                }
            }
        }
    }

    public static void optimizeMemory() {
    }

    public static void changeMetrics(Context context) {
        if (context.getResources().getDisplayMetrics().densityDpi == 240) {
            change2MediumMetrics(context);
        }
    }

    public static void change2HighMetrics(Context context) {
        DisplayMetrics curMetrics = context.getResources().getDisplayMetrics();
        DisplayMetrics metrics = new DisplayMetrics();
        metrics.scaledDensity = 1.5f;
        metrics.density = 1.5f;
        metrics.densityDpi = 240;
        metrics.xdpi = 240.0f;
        metrics.ydpi = 240.0f;
        metrics.heightPixels = curMetrics.heightPixels;
        metrics.widthPixels = curMetrics.widthPixels;
        context.getResources().getDisplayMetrics().setTo(metrics);
    }

    public static void change2MediumMetrics(Context context) {
        DisplayMetrics curMetrics = context.getResources().getDisplayMetrics();
        DisplayMetrics metrics = new DisplayMetrics();
        metrics.scaledDensity = 1.0f;
        metrics.density = 1.0f;
        metrics.densityDpi = 160;
        metrics.xdpi = 160.0f;
        metrics.ydpi = 160.0f;
        metrics.heightPixels = curMetrics.heightPixels;
        metrics.widthPixels = curMetrics.widthPixels;
        context.getResources().getDisplayMetrics().setTo(metrics);
    }

    public static class OpenAccountCache {
        String cellId;
        String feeTotal;
        String packageId;
        String serialNum;
        String yxhdId;

        public String getSerialNum() {
            return this.serialNum;
        }

        public void setSerialNum(String serialNum2) {
            this.serialNum = serialNum2;
        }

        public String getCellId() {
            return this.cellId;
        }

        public void setCellId(String cellId2) {
            this.cellId = cellId2;
        }

        public String getPackageId() {
            return this.packageId;
        }

        public void setPackageId(String packageId2) {
            this.packageId = packageId2;
        }

        public String getYxhdId() {
            return this.yxhdId;
        }

        public void setYxhdId(String yxhdId2) {
            this.yxhdId = yxhdId2;
        }

        public String getFeeTotal() {
            return this.feeTotal;
        }

        public void setFeeTotal(String feeTotal2) {
            this.feeTotal = feeTotal2;
        }
    }

    public String getNum() {
        return this.num;
    }

    public void setNum(String num2) {
        this.num = num2;
    }

    public String getUserFullName() {
        return this.userFullName;
    }

    public void setUserFullName(String userFullName2) {
        this.userFullName = userFullName2;
    }

    public String getStaffRegionId() {
        return this.staffRegionId;
    }

    public void setStaffRegionId(String staffRegionId2) {
        this.staffRegionId = staffRegionId2;
    }




    public String getAccountChannelType() {
        return this.accountChannelType;
    }

    public void setAccountChannelType(String accountChannelType2) {
        this.accountChannelType = accountChannelType2;
    }

    public String getAppsConfig() {
        return this.appsConfig;
    }

    public void setAppsConfig(String appsConfig2) {
        this.appsConfig = appsConfig2;
    }

    public String getAccount() {
        return PreferenceHelper.getWithKeyValueEncry("APP_ACCOUNT", "没获取到工号");
    }

    public void setAccount(String account) {
        PreferenceHelper.commitWithKeyValueEncry("APP_ACCOUNT", account);
    }

}
