package com.wade.mobile.func;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.widget.Toast;
import com.ailk.common.data.IData;
import com.ailk.common.data.impl.DataMap;
//import com.litesuits.http.LiteHttp;
//import com.litesuits.http.LiteHttpClient;
//import com.litesuits.http.request.Request;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.wade.mobile.app.MobileAppInfo;
import com.wade.mobile.common.MobileThread;
//import com.wade.mobile.common.bluetooth.activity.ShareByBluetoothActivity;
//import com.wade.mobile.common.bluetooth.listener.OnOpenBluetoothListener;
//import com.wade.mobile.common.bluetooth.util.BluetoothTool;
import com.wade.mobile.common.sms.listener.OnSmsReceiveListener;
import com.wade.mobile.common.sms.util.SmsTool;
import com.wade.mobile.frame.IWadeMobile;
import com.wade.mobile.frame.config.MobileConfig;
import com.wade.mobile.frame.config.ServerDataConfig;
import com.wade.mobile.frame.plugin.Plugin;
import com.wade.mobile.safe.MobileSecurity;
import com.wade.mobile.util.BusinessCache;
import com.wade.mobile.util.Constant;
import com.wade.mobile.util.DirectionUtil;
import com.wade.mobile.util.FileUtil;
import com.wade.mobile.util.FuncConstant;
import com.wade.mobile.util.Messages;
import com.wade.mobile.util.StringUtil;
import com.wade.mobile.util.Utility;
import com.wade.mobile.util.http.HttpTool;
import com.wade.mobile.util.http.UnirestUtil;
import java.io.File;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import org.json.JSONArray;


public class MobileNetWork extends Plugin {
    /* access modifiers changed from: private */
    public DirectionUtil directionUtil = DirectionUtil.getInstance(this.context);
    /* access modifiers changed from: private */
    public boolean hasSetSmsListener;

    public MobileNetWork(IWadeMobile wademobile) {
        super(wademobile);
    }

    public void setSmsListener(JSONArray param) throws Exception {
        setSmsListener(param.getString(0));
    }

    public void setSmsListener(String telString) throws Exception {
        HashSet<String> telSet = null;
        if (!isNull(telString)) {
            telSet = new HashSet<>();
            if (StringUtil.isJSONArray(telString)) {
                JSONArray teles = new JSONArray(telString);
                for (int i = 0; i < teles.length(); i++) {
                    telSet.add(teles.getString(i));
                }
            } else {
                telSet.add(telString);
            }
        }
        final HashSet<String> _telSet = telSet;
        if (!this.hasSetSmsListener) {
            SmsTool.setSmsListener(this.context, new OnSmsReceiveListener() {
                public boolean onSmsReceive(String content, String sender, long time) {
                    if (_telSet != null && !_telSet.contains(sender)) {
                        return false;
                    }
                    IData resultData = new DataMap();
                    resultData.put(FuncConstant.CONTENT, content);
                    resultData.put(FuncConstant.SENDER, sender);
                    resultData.put(FuncConstant.TIME, Long.valueOf(time));
                    MobileNetWork.this.callback(resultData.toString(), true);
                    boolean unused = MobileNetWork.this.hasSetSmsListener = false;
                    return true;
                }
            });
            this.hasSetSmsListener = true;
        }
    }

    public void httpRequest(JSONArray param) throws Exception {

        String result;
        String requestUrl = param.getString(0);
        String encode = param.getString(1);
      //  System.out.println("MobileNetWork-JsonArry-httpRequest:0:"+param.toString());
        if (isNull(encode)) {
            encode = MobileConfig.getInstance().getEncode();
        }
        try {
            result = httpRequest(requestUrl, encode);
        } catch (Exception e) {
            result = "{\"X_RESULTCODE\":\"-1\",\"X_RESULTINFO\":\"" + e.getMessage() + "\"}";
        }
        callback(result, true);
    }

    public String httpRequest(String requestUrl, String encode) throws Exception {
        MobileConfig config = MobileConfig.getInstance();
        if (!requestUrl.startsWith("http")) {
            if (!requestUrl.startsWith("/")) {
                requestUrl = "/" + requestUrl;
            }
            requestUrl = config.getRequestHost() + requestUrl;
        }
        HashMap<String, String> hashMap = new HashMap<String, String>();
//        hashMap.put("Client-Type", getEncryptFlag());
//        hashMap.put("Timestamp", getCurrentTimestamp());
        hashMap.put("Content-Type", "application/x-www-form-urlencoded");
        hashMap.put("User-Agent", "Dalvik/2.1.0 (Linux; U; Android 7.1.2; vivo X9Plus Build/N2G47H)");
//        hashMap.put("Host", "211.138.17.10:20100");
        hashMap.put("Connection", "Keep-Alive");
        hashMap.put("Accept-Encoding","gzip");
       // System.out.println("MobileNetWork-httpRequest:0:");
        return HttpTool.httpRequest(HttpTool.urlEncode(requestUrl, encode), hashMap, "POST", encode,"MobileNetWork-httpRequest0");
    }

/*
    public void httpGet(JSONArray param) throws Exception {
        String result;
        String requestUrl = param.getString(0);
        if (isNull(param.getString(1))) {
            String encode = MobileConfig.getInstance().getEncode();
        }
        try {
            result = LiteHttp.getInstance(this.context).execute(new Request(requestUrl)).getString();
        } catch (Exception e) {
            result = e.getMessage();
        }
        callback(result, true);
    }


    public String httpGet(String requestUrl, String encode) throws Exception {
        MobileConfig config = MobileConfig.getInstance();
        if (!requestUrl.startsWith("http")) {
            if (!requestUrl.startsWith("/")) {
                requestUrl = "/" + requestUrl;
            }
            requestUrl = config.getRequestHost() + requestUrl;
        }
        return HttpTool.httpRequest(HttpTool.urlEncode(requestUrl, encode), (String) null, "GET", encode);
    }
*/
    public void dataRequest(JSONArray param) throws Exception {
        System.out.println("MobileNetWork-JsonArry-dataRequest:0:"+param.toString());
        String dataAction = param.getString(0);
        String data = param.getString(1);
        System.out.println("MobileNetWork-JsonArry-dataRequest:1:"+data);
        callback(dataRequest(dataAction, isNull(data) ? null : data,"MobileNetWork-dataRequest0"), true);
        System.out.println("MobileNetWork-JsonArry-dataRequest:6:"+param.toString());
    }

    public String dataRequest(String dataAction, String param,String callName) throws Exception {
        System.out.println("MobileNetWork-dataRequest:0:"+param.toString());
        System.out.println("MobileNetWork-dataRequest:0-1:"+callName);
        String result;
        if (param==null){
            System.out.println("MobileNetWork-dataRequest:1:");
        }
        synchronized (dataAction) {
            result = (String) BusinessCache.getInstance().get(dataAction);
            if (result != null) {
                BusinessCache.getInstance().remove(dataAction);
            } else {
                System.out.println("MobileNetWork-dataRequest:5:"+param.toString());
                result = requestBizData(dataAction, param);
            }
        }
        System.out.println("MobileNetWork-dataRequest:6:");
        return result;
    }

    /* access modifiers changed from: private */
    public String requestBizData(String dataAction, String param) throws Exception {
        System.out.println("MobileNetWork-requestBizData:0:"+dataAction);
        System.out.println("MobileNetWork-requestBizData:1:"+param);
        String result = HttpTool.httpRequest(MobileConfig.getInstance().getRequestUrl(), HttpTool.toQueryStringWithEncode(transPostData(dataAction, param)), "POST","MobileNetWork-requestBizData0");
        if (ServerDataConfig.isEncrypt(dataAction).booleanValue()) {
            result= MobileSecurity.responseDecrypt(result);
        }
        System.out.println("MobileNetWork-requestBizData:2:"+result);
        return result;
    }

    public Map<String, String> transPostData(String dataAction, String dataParam) throws Exception {
        Map<String, String> postData = new HashMap<>();
        postData.put(Constant.Server.ACTION, dataAction);
        if (ServerDataConfig.isEncrypt(dataAction).booleanValue()) {
            MobileSecurity.init(this.context);
            postData.put(Constant.Server.KEY, MobileSecurity.getDesKey());
            if (dataParam != null) {
                postData.put("data", MobileSecurity.requestEncrypt(dataParam));
            }
        } else if (dataParam != null) {
            postData.put("data", dataParam);
        }
        return postData;
    }

    public void storageDataByThread(JSONArray param) throws Exception {
        String dataAction = param.getString(0);
        String data = param.getString(1);
        boolean isEncrypt = false;
        if (param.length() > 2) {
            if (Constant.TRUE.equals(param.getString(2))) {
                isEncrypt = true;
            } else {
                isEncrypt = false;
            }
        }
        int waitoutTime = 5;
        if (param.length() > 3) {
            waitoutTime = param.getInt(3);
        }
        storageDataByThread(dataAction, isNull(data) ? null : data, isEncrypt, (long) waitoutTime);
    }

    public void storageDataByThread(String dataAction, String param, boolean isEncrypt, long waitoutTime) throws Exception {
        if (waitoutTime < 3 || waitoutTime > 10) {
            waitoutTime = 5;
        }
        final String str = dataAction;
        final String iData = param;
        new MobileThread(dataAction, waitoutTime) {
            /* access modifiers changed from: protected */
            public void execute() throws Exception {
                String result = MobileNetWork.this.requestBizData(str, iData);
                synchronized (str) {
                    BusinessCache.getInstance().put(str, result);
                }
            }
        }.start();
    }

/*
    public void shareByBluetooth(JSONArray param) throws Exception {
        try {
            if (BluetoothAdapter.getDefaultAdapter() == null) {
                Toast.makeText(this.context, "蓝牙初始化出错!", 1);
            } else if (Build.VERSION.SDK_INT < 16) {
                try {
                    this.context.startActivity(new Intent(this.context, ShareByBluetoothActivity.class));
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this.context, "蓝牙初始化出错!", 1);
                }
            } else {
                final BluetoothTool bluetoothTools = new BluetoothTool(this.context);
                final MobileAppInfo mobileAppInfo = MobileAppInfo.getInstance(this.context);
                if (bluetoothTools.isEnabled()) {
                    bluetoothTools.sendFile(mobileAppInfo.getApk());
                } else {
                    bluetoothTools.openBluetooth(new OnOpenBluetoothListener() {
                        public void OnOpened(BluetoothAdapter adapter) {
                            try {
                                bluetoothTools.sendFile(mobileAppInfo.getApk());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        } catch (Exception e2) {
            Toast.makeText(this.context, "蓝牙初始化出错!", 1);
        }
    }
*/

    public void openBrowser(JSONArray param) throws Exception {
        openBrowser(param.getString(0));
    }

    public void openBrowser(String url) {
        this.context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(url)));
    }

    public void uploadWithServlet(JSONArray param) throws Exception {
        uploadWithServlet(param.getJSONArray(0), param.getString(1), isNull(param.getString(2)) ? null : param.getString(2));
    }

    @SuppressLint("StaticFieldLeak")
    public void uploadWithServlet(final JSONArray filePaths, final String dataAction, String dataParam) throws Exception {
        Map<String, String> postData = transPostData(dataAction, dataParam);
        new AsyncTask<Map<String, String>, Integer, String>() {
            /* access modifiers changed from: protected */
            public String doInBackground(Map<String, String>... args) {
                Map<String, String> postData = args[0];
                Map<String, Object> fileData = new HashMap<>();
                int i = 0;
                while (i < filePaths.length()) {
                    try {
                        String filePath = filePaths.getString(i);
                        File file = new File(filePath);
                        if (!file.exists()) {
                            Utility.error(Messages.FILE_NOT_EXIST + ":" + filePath);
                        }
                        fileData.put("UPLOAD_FILE" + i, file);
                        i++;
                    } catch (Exception e) {
                        MobileNetWork.this.error(e.getMessage());
                        Utility.error((Throwable) e);
                        return null;
                    }
                }
                fileData.put("UPLOAD_FILE_COUNT", Integer.valueOf(filePaths.length()));
                String result = null;
                try {
                    result = UnirestUtil.uploadByPost(HttpTool.urlEscape(MobileConfig.getInstance().getRequestUrl() + "?" + HttpTool.urlEncode(HttpTool.toQueryString(postData), MobileConfig.getInstance().getEncode())).toString(), fileData);
                } catch (UnirestException e) {
                    e.printStackTrace();
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                try {
                    if (ServerDataConfig.isEncrypt(dataAction).booleanValue()) {
                        return MobileSecurity.responseDecrypt(result);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return result;
            }

            /* access modifiers changed from: protected */
            public void onPostExecute(String result) {
                super.onPostExecute(result);
                if (result != null) {
                    MobileNetWork.this.callback(result, true);
                }
            }
        }.execute(new Map[]{postData});
    }

    public void downloadWithServlet(JSONArray param) throws Exception {
        downloadWithServlet(param.getString(0), param.getString(1), param.getString(2) == null ? null : param.getString(2));
    }

    public void downloadWithServlet(final String _savePath, String dataAction, String dataParam) throws Exception {
        Map<String, String> tempPostData = transPostData(dataAction, dataParam);
        final Map<String, Object> postData = new HashMap<>();
        postData.putAll(tempPostData);
        new AsyncTask<String, Integer, String>() {
            /* access modifiers changed from: protected */
            public String doInBackground(String... arg0) {
                String savePath = _savePath;
                try {
                    InputStream in = UnirestUtil.downloadByPost(MobileConfig.getInstance().getRequestUrl(), postData);
                    if (!savePath.startsWith(File.separator)) {
                        savePath = MobileNetWork.this.directionUtil.getDirection(savePath, true);
                    }
                    File file = new File(savePath).getParentFile();
                    if (!file.exists() && !file.mkdirs()) {
                        Utility.error("创建下载文件夹失败!");
                    }
                    FileUtil.writeFile(in, savePath);
                } catch (Exception e) {
                    MobileNetWork.this.error("[" + savePath + "]异常:" + e.getMessage());
                }
                return savePath;
            }

            /* access modifiers changed from: protected */
            public void onPostExecute(String savePath) {
                super.onPostExecute(savePath);
                if (savePath != null) {
                    MobileNetWork.this.callback(savePath, true);
                }
            }
        }.execute(new String[0]);
    }

    public void uploadFile(JSONArray param) throws Exception {
        uploadFile(param.getString(0));
    }

    public void uploadFile(String path) throws Exception {
    }
}