package com.ai.cmcchina.crm.http;

import android.app.Activity;
import android.util.Log;

import androidx.constraintlayout.widget.Constraints;


import com.heclient.heapp.App;
import com.heclient.heapp.util.DesPlus;
import com.wade.mobile.util.Constant;
import com.wade.mobile.util.cipher.DES;
import com.wade.mobile.util.http.HttpTool;

import org.jetbrains.annotations.NotNull;

import java.security.Key;
import java.security.interfaces.RSAPublicKey;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;


public class IPURemoteClient extends HttpClient {
    public static final String CRM_SERVER = "CRM";
    public static final String ESOP_SERVER = "ESOP";
    public static final String PARTNER_SERVER = "PARTNER";

    protected Activity context;


    public IPURemoteClient() {
    }

    public IPURemoteClient(Activity context2, String targetServer) throws Exception {
        this.context = context2;

        if (CRM_SERVER.equals(targetServer)) {
            this.requestUrl =((App) this.context.getApplication()).getCrmServerPath() + "/mobiledata";
            Log.d(TAG, "IPURemoteClient: 0:"+this.requestUrl);
/*        } else if ("ESOP".equals(targetServer)) {
            this.requestUrl = app.getEsopServerPath() + "/mobiledata";
        } else if (PARTNER_SERVER.equals(targetServer)) {
            this.requestUrl = app.getPartnerServerPath() + "/mobiledata";*/
        }
    }

    public IPURemoteClient(Activity context2) {
        this.context = context2;
    }

    public String sendRequest() throws Exception {
        return super.sendPostRequest();
    }

    public String sendRequest(String url, Map<String, String> params) throws Exception {
       // HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
        Map map = params;
        Log.d(Constraints.TAG, "IPURemoteClient-sendRequest:4: "+url);
        Map<String, String>propertyMap = requestProperty();
        Log.d(Constraints.TAG, "IPURemoteClient-sendRequest:5: "+params.toString());
        String paramStr =transReqParams(params);
        Log.d(Constraints.TAG, "IPURemoteClient-sendRequest:6: "+paramStr);
        return decryptData(HttpTool.httpRequest(url,propertyMap , "POST",paramStr,"IPURemoteClient-sendRequest0"));
    }

    public String sendRequest(Map<String, String> params) throws Exception {
        return sendRequest(this.requestUrl, params);
    }

    private String transReqParams(Map<String, String> params) throws Exception {
        Log.d(TAG, "IPURemoteClient-transReqParams: 0");
       
        return toQueryStringWithEncode(transPostData(params.get(Constant.Server.ACTION), params));
    }


    /*转换post内容*/
    public Map<String, String> transPostData(String paramString, Map<String, String> paramMap) throws Exception {
        HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
        hashMap.put("action", paramString);
        Log.d(TAG, "transPostData: 0:"+paramString);
        if (paramMap != null)
            hashMap.put("data", encryptData(paramMap.get("data")));
        Log.d(TAG, "transPostData: 1:");
        hashMap.put("key", getDesKey());
        return (Map)hashMap;
    }

    private void createRandomKey() {
        ((App) this.context.getApplication()).setRandomKey(String.valueOf((8.9999999E7d * Math.random()) + 1.0E7d));
    }
    /*获取Key值
     * *Key值 由DES的指定密钥 经RSA及公钥加密得到
     * */
    public String getDesKey() throws Exception {
        String strRandomKey="";
        RSAPublicKey publicKey = ((App) this.context.getApplication()).getPublicKey();
       // Log.d(TAG, "getDesKey_publicKey: "+publicKey.toString());
        String randomKey = ((App) this.context.getApplication()).getRandomKey();
        strRandomKey= DesPlus.encryptyRandomKey(publicKey,randomKey);
        return strRandomKey;
    }
    /*解密*/
    private String decryptData(String paramString) throws Exception {
        String decByBpKey="";
        String randomKey = ((App) this.context.getApplication()).getRandomKey();
        Log.d(TAG, "decryptData: 0"+randomKey);
        decByBpKey=DesPlus.decrptyDataByPublicKey(paramString,randomKey);
        Log.d(TAG, "decryptData: 1"+randomKey);
        return decByBpKey;
    }
    /*加密*/
    public String encryptData(String paramString){
        String encByBpKey="";
        ((App) this.context.getApplication()).createRandomKey();
        String randomKey=((App) this.context.getApplication()).getRandomKey();
        Log.d(TAG, "encryptData: 0:"+randomKey);
        encByBpKey=DesPlus.encryptDataByPublicKey(paramString,randomKey);
        return encByBpKey;
    }

    /*http 包头*/
    public Map<String, String>requestProperty() throws Exception{
        HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
        hashMap.put("Client-Type", getEncryptFlag());
        hashMap.put("Timestamp", getCurrentTimestamp());
        hashMap.put("Content-Type", "application/x-www-form-urlencoded");
        hashMap.put("User-Agent", "Dalvik/2.1.0 (Linux; U; Android 7.1.2; vivo X9Plus Build/N2G47H)");
        hashMap.put("Host", "211.138.17.10:20100");
        hashMap.put("Connection", "Keep-Alive");
        hashMap.put("Accept-Encoding","gzip");
        return (Map)hashMap;
    }
    private  String getCurrentTimestamp() throws Exception {
        Log.d(TAG, "getCurrentTimestamp: 0:");
        Key desKey =((App) this.context.getApplication()).getDesKey();
        Log.d(TAG, "getCurrentTimestamp:1:");
        return DES.encryptString(desKey, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
    }

    @NotNull
    private  String getEncryptFlag() throws Exception {
        Log.d(TAG, "getEncryptFlag: 0:");
        Key desKey =((App) this.context.getApplication()).getDesKey() ;
        Log.d(TAG, "getEncryptFlag: 1"+desKey.toString());
        DES des =new DES();
        Log.d(TAG, "getEncryptFlag: 2"+desKey.toString());
        String retureStr=des.encryptString( desKey, "hzs_app_client_flag");
        Log.d(TAG, "getEncryptFlag: 3"+retureStr);
        return retureStr ;
    }

    public static String toQueryStringWithEncode(Map<String, String> data) {
        StringBuilder buff = new StringBuilder();
        Log.d(TAG, "toQueryStringWithEncode: 0:");
        for (String key : data.keySet()) {
            buff.append(postDataEncode(key)).append("=").append(postDataEncode(data.get(key))).append("&");
        }
        buff.setLength(buff.length() - 1);
        Log.d(TAG, "toQueryStringWithEncode: 1:"+buff.toString());
        return buff.toString();
    }

    /*
     *转义特殊符号
     * */
    public static String postDataEncode(String data) {
        if (data == null || "".equals(data)) {
            return "";
        }
        if (data.indexOf(37) == -1 && data.indexOf(43) == -1 && data.indexOf(38) == -1) {
            return data;
        }
        int length = data.length();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            char c = data.charAt(i);
            switch (c) {
                case '%':
                    sb.append("%25");
                    break;
                case '&':
                    sb.append("%26");
                    break;
                case '+':
                    sb.append("%2B");
                    break;
                default:
                    sb.append(c);
                    break;
            }
        }
        return sb.toString();
    }
    /*发送http 请求*/


    public String sendRequest(String url,Map<String, String>propertyMap, Map<String, String> params) throws Exception {
        /*httpRequest(String requestUrl,Map<String, String>propertyMap,String requestMethod,String outputStr)*/
       System.out.println("IPU-sendRequest:0:"+params.toString());
        String outputStr =transReqParams(params);
        return decryptData(HttpTool.httpRequest(url, propertyMap, "POST",outputStr,"IPURemoteClient-sendRequest1"));
    }

}
