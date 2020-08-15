package com.heclient.heapp.jobs;

import android.annotation.SuppressLint;
import android.os.SystemClock;
import android.util.Log;

import androidx.annotation.Nullable;

import com.alibaba.fastjson.JSON;
import com.birbit.android.jobqueue.CancelReason;
import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.heclient.heapp.App;
import com.heclient.heapp.db.DaoSession;
import com.heclient.heapp.db.PhoneNumDao;
import com.heclient.heapp.db.ResultInfo;
import com.heclient.heapp.db.ResultInfoDao;
import com.wade.mobile.app.MobileAppInfo;
import com.wade.mobile.frame.config.MobileConfig;
import com.wade.mobile.frame.config.ServerDataConfig;
import com.wade.mobile.frame.multiple.MultipleManager;
import com.wade.mobile.safe.MobileSecurity;
import com.wade.mobile.util.Constant;
import com.wade.mobile.util.cipher.DES;
import com.wade.mobile.util.cipher.RSA;
import com.wade.mobile.util.http.HttpTool;

import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.security.Key;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import static com.wade.mobile.util.http.HttpTool.getCurrentTimestamp;
import static com.wade.mobile.util.http.HttpTool.getEncryptFlag;

public class CustomerLoginJob extends Job {
    private long localId;
    private String text;
    private String tag;
    private int inId;
    private JSONObject req;
    private String result;
    private static String randomDesKey;
    private static SecretKey desKey ;
    private static RSAPublicKey publicKey;
    private String getDesKey;


    public CustomerLoginJob(String text) {
        /**
         * 默认构造器传入的是int(priority)参数是该任务的优先级，优先级越高，越优先执行。
         *<p>
         * requireNetwork(): 设置该任务要求访问网络；
         *<p>
         * groupBy(String groupId)：设置组ID，被设置相同组ID的任务，将会按照顺序执行；
         *<p>
         * persist()：设置任务为可持久化的，持久化要求Job类为序列化的，这一点并不意外，
         * 因为一个类的内容只有序列化之后才能变成字节模式保存在硬盘上；
         * <p>
         *delayInMs(long delayMs)：设置延迟时间，ms为单位，在该时间之后再放入任务队列中。
         * <p>
         * addTags :添加标记取消任务时使用
         */
        super(new Params(Priority.MID).requireNetwork().groupBy("CustomerLoginJob"));
        //order of tweets matter, we don't want to send two in parallel
        //use a negative id so that it cannot collide w/ twitter ids
        //we have to set local id here so it gets serialized into job (to find tweet later on)
        localId = -System.currentTimeMillis();
        this.text = text;
        try {
           req = new JSONObject(text);
           tag =req.getString("tag");

          /* inId=req.getInt("inId");*/
           initKey();//初始化密钥key

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public String getResult(){
        return result;
    }
    public String getTag(){
        return tag;
    }
    public int getInId(){return inId;}

    @Override
    public void onAdded() {
        //job has been secured to disk, add item to database
        //任务加入队列并被保存在硬盘上，定义此时要处理的逻辑；
        Log.d(tag, "onAdded");
    }

    @Override
    public void onRun() throws Throwable {
        //任务开始执执行，在此定义任务的主题逻辑，当执行完毕后，任务将被从任务队列中删除；
       // Log.d(tag, "开始运行..."+text);

        String url= "http://211.138.17.10:20600/gbh/mobiledata";//
        String dataAction = req.getString("dataAction");//"MainPage.customerLogin_new";
        String param  = req.getString("param");
        String backInfo = req.getString("backInfo");//"MainPage.customerLogin_new";

        JSONObject backObject =new JSONObject(backInfo);
        int Id=backObject.getInt("phoneID");

        // 封装加密传输数据
        Map<String, String> postData = new HashMap<>();
        postData.put("action", dataAction); //
        postData.put("key", getDesKey()); //加密key
        postData.put("data",requestEncrypt(param));
        String data = HttpTool.toQueryStringWithEncode(postData);
        //发送http 头

        JSONObject headers=new JSONObject();
        headers.put("Content-Type", "application/x-www-form-urlencoded");
        headers.put("User-Agent", "Dalvik/2.1.0 (Linux; U; Android 7.1.2; vivo X9Plus Build/N2G47H)");
//      propertyMap.put("Host", "211.138.17.10:20100");
        headers.put("Connection", "Keep-Alive");
        headers.put("Accept-Encoding","gzip");
        headers.put("Client-Type", getEncryptFlag());
        headers.put("Timestamp", getCurrentTimestamp());

        JSONObject requestData = new JSONObject();
        requestData.put("url",url);
        requestData.put("requestMethod","POST");
        requestData.put("header",headers.toString());
        requestData.put("requestStr",data);
        String str = HttpRequest.httpRequest(requestData.toString());
        str= responseDecrypt(str);
        StoreResultInfo(str);
        @SuppressLint("DefaultLocale") String sql=String.format("UPDATE PHONE_NUM SET IS_DONE = 1 WHERE _id = %d",Id);
       // System.out.println("CustomerLoginJob-sql:0:"+sql);
        Database database =App.getInstance().getDaoSession().getDatabase();
        database.execSQL(sql);
        //返回数据串

        backObject.put("backResult",str);
        result=backObject.toString();

    }

    @Override
    protected void onCancel(@CancelReason int cancelReason, @Nullable Throwable throwable) {
        //delete
        //任务取消的时候要执行的逻辑；
    }

    @Override
    protected RetryConstraint shouldReRunOnThrowable(Throwable throwable, int runCount,
                                                     int maxRunCount) {
        /*if(throwable instanceof TwitterException) {
            //if it is a 4xx error, stop
            TwitterException twitterException = (TwitterException) throwable;
            int errorCode = twitterException.getErrorCode();
            return errorCode < 400 || errorCode > 499 ? RetryConstraint.RETRY : RetryConstraint.CANCEL;
        }
        return RetryConstraint.RETRY;*/
        /**
         * 当onRun()方法中抛出异常时，就会调用该函数，
         *该函数返回Job类在执行发生异常时的应对策略，
         *是重新执行还是取消，或者是一定时间之后再尝试。
         */

        //RetryConstraint的自带策略，立刻重新尝试执行策略，
        // 直到执行成功或者尝试次数达到最大（18次）；
        // return RetryConstraint.RETRY;

        //RetryConstraint的自带策略，取消当前任务的执行；
        //return RetryConstraint.CANCEL;

        //定期延迟尝试执行任务，如果任务执行失败，
        // 下次执行的延迟时间会以指数形式增长,最大尝试次数为20次；
        //return RetryConstraint.createExponentialBackoff(runCount, 10);
        return null;
    }

    public void StoreResultInfo(String str){
        com.alibaba.fastjson.JSONObject jsonObject =JSON.parseObject(str);
       // System.out.println(jsonObject.toJSONString());
        ResultInfoDao resultInfoDao = App.getInstance().getDaoSession().getResultInfoDao(); //
        ResultInfo resultInfo=new ResultInfo();
        resultInfo.setX_RESULTINFO(jsonObject.getString("X_RESULTINFO"));
        String returnCode=jsonObject.getString("returnCode");
        resultInfo.setReturnCode(returnCode);
        resultInfo.setReturnMsg(jsonObject.getString("returnMsg"));
        resultInfo.setX_RESULTCODE(jsonObject.getString("X_RESULTCODE"));
        resultInfo.setX_RECORDNUM(jsonObject.getString("X_RECORDNUM"));
        //{"X_RESULTINFO":"OK","returnCode":"9999","returnMsg":"用户信息不存在","X_RESULTCODE":"0","X_RECORDNUM":1}
        if(returnCode.equals("0000")){
            resultInfo.setUser_name(jsonObject.getString("user_name"));
            resultInfo.setUser_full_name(jsonObject.getString("user_full_name"));
            com.alibaba.fastjson.JSONObject user=jsonObject.getJSONObject("user");
            resultInfo.setPhone(user.getString("phone"));
            resultInfo.setIs4G(user.getString("isVip"));
            resultInfo.setPreSave(user.getString("preSave"));
            resultInfo.setCurrentOfferId(user.getString("currentOfferId"));
            resultInfo.setState(user.getString("state"));
            resultInfo.setStarLevelCreditLimit(user.getString("starLevelCreditLimit"));
            resultInfo.setBrandName(user.getString("brandName"));
            resultInfo.setBrandId(user.getString("brandId"));
            resultInfo.setUsername(user.getString("username"));
            resultInfo.setBalOrgName(user.getString("balOrgName"));
            resultInfo.setBrandCode(user.getString("brandCode"));
            resultInfo.setHistoryCost(user.getString("historyCost"));
            resultInfo.setValidateLevel(user.getString("validateLevel"));
            resultInfo.setCreateDate(user.getString("createDate"));
            resultInfo.setOfferName(user.getString("offerName"));
            resultInfo.setRegionId(user.getString("regionId"));
            resultInfo.setNoticeFlag(user.getString("noticeFlag"));
            resultInfo.setBanlance(user.getString("banlance"));
            resultInfo.setBalOrgId(user.getString("balOrgId"));
            resultInfo.setRegionName(user.getString("regionName"));
            resultInfo.setCost(user.getString("cost"));
            resultInfo.setIs4G(user.getString("is4G"));
            resultInfo.setStarLevel(user.getString("starLevel"));
            resultInfo.setPoint(user.getString("point"));
            resultInfo.setCurrentOweFee(user.getString("currentOweFee"));
            resultInfo.setStarLevelValue(user.getString("starLevelValue"));
            com.alibaba.fastjson.JSONObject extInfo=user.getJSONObject("extInfo");
            resultInfo.setAge(extInfo.getString("age"));
            resultInfo.setBroadbandSpeed(extInfo.getString("broadbandSpeed"));
            resultInfo.setAmountCredit(user.getString("amountCredit"));
        }
        resultInfoDao.insert(resultInfo);
    }

    public static void initKey() {
        randomDesKey = String.valueOf((8.9999999E7d * Math.random()) + 1.0E7d);
      //  System.out.println("CustomerLoginJob-initKey:0:"+randomDesKey);
        try {
        // System.out.println("CustomerLoginJob-initKey:1:"+randomDesKey);
            desKey=DES.getKey(randomDesKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static String getEncryptFlag() throws Exception {
        return DES.encryptString(getKey(), "hzs_app_client_flag");
    }
    private static Key getKey() throws Exception {
        byte[] arrayOfByte1 = "G3YYT".getBytes();
        byte[] arrayOfByte2 = new byte[8];
        for (byte b = 0;; b++) {
            if (b >= arrayOfByte1.length || b >= arrayOfByte2.length)
                return new SecretKeySpec(arrayOfByte2, "DES");
            arrayOfByte2[b] = (byte)arrayOfByte1[b];
        }
    }
    public String getDesKey() throws Exception {
        if (!MultipleManager.isMultiple()) {
         //   System.out.println("DashboardFragment-getDesKey:0:"+randomDesKey);
            String rsaDesKey= RSA.encrypt(randomDesKey, getPublicKey());
        //    System.out.println("DashboardFragment-getDesKey:1:"+rsaDesKey);
            return rsaDesKey;
        }
        if (MultipleManager.getCurrPublicKey() == null) {
            String rsaFile= MobileAppInfo.getSdcardAppPath(this.getApplicationContext())+ File.separator+"key/crmApp";
            //  String rsaFile=MultipleManager.getMultBasePath() + Constant.Server.KEY + File.separator + MultipleManager.getCurrAppId();
           // System.out.println("DashboardFragment-getDesKey:2:"+rsaFile);
            File rsa =new File(rsaFile);
            if(!rsa.exists()){
                MultipleManager.getCurrAppConfig().setPublicKey(RSA.loadPublicKey(new FileInputStream(MultipleManager.getMultBasePath() + Constant.Server.KEY + File.separator + MultipleManager.getCurrAppId())));
            }else{
                rsaFile= "raw/crmApp";
                //  InputStream is= contextThreadLocal.get().getAssets().open(rsaFile);
                //  MultipleManager.getCurrAppConfig().setPublicKey(RSA.loadPublicKey(is));

            }
        }
        return RSA.encrypt(randomDesKey, MultipleManager.getCurrPublicKey());
    }
    private RSAPublicKey getPublicKey() throws Exception {
        if (publicKey == null) {
            //先从assets中获取
            //this.mContext.getAssets().open("raw/crmApp");
            //从资源下载的地方获取
            String rsaFile=MobileAppInfo.getSdcardAppPath(this.getApplicationContext())+ File.separator+"key/crmApp";
           // System.out.println("DashboardFragment-getPublicKey:1:"+rsaFile);
            publicKey = RSA.loadPublicKey(new FileInputStream(rsaFile));
        }
        return publicKey;
    }

    public static String requestEncrypt(String data) throws Exception {
       // System.out.println("DashboardFragment-requestEncrypt:0:");
        return DES.encryptString(desKey, data);
    }
    public static String responseDecrypt(String data) throws Exception {
       // System.out.println("DashboardFragment-responseDecrypt:0:");
        return DES.decryptString(desKey, data);
    }
    public Map<String, String> transPostData(String dataAction, String dataParam,String threadRandomDesKey) throws Exception {
        Map<String, String> postData = new HashMap<>();
        postData.put(Constant.Server.ACTION, dataAction);
        //        String threadRandomDesKey =randomDesKey;
        //        SecretKey threadDesKey=desKey ;
        postData.put(Constant.Server.KEY, getDesKey());
        if (dataParam != null) {
            //String encypt=MobileSecurity.requestEncrypt(dataParam);
            postData.put("data", requestEncrypt(dataParam));
        }

        return postData;
    }



}
