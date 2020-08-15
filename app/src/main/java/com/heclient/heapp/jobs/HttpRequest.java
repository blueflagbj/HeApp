package com.heclient.heapp.jobs;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.google.gson.JsonObject;
import com.wade.mobile.util.http.FakeX509TrustManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

public class HttpRequest {
    static final String TAG = HttpRequest.class.getSimpleName();
    private static final int conTimeout = 5000;
    private static final String defaultEncode = "UTF-8";
    private static final int readTimeout = 120000;
    private HttpURLConnection conn;

    public HttpRequest(){

    }
    /**
     * 发起https请求并获取结果
     *
     * @param requestData 请求数据
     ** requestData为Json格式：
     ** url 请求地址 默认http 若为https，则必须带上https://
     ** header 添加http头  HashMap样式
     ** requestMethod 请求方式（GET、POST）
     ** requestStr    提交的数据
     **
     * @return String 返回字符串数据
     */
    public static String httpRequest(String requestData) throws Exception {
       //System.out.println("httpRequest:0:"+requestData);
        StringBuffer buffer = new StringBuffer();
        InputStream is = null;
        OutputStreamWriter writer = null;
        JSONObject req = new JSONObject(requestData);
        String requestUrl =req.getString("url");
        String requestMethod =req.getString("requestMethod");
        String header=req.getString("header");

        String requestStr=req.getString("requestStr");
        //JSON headers = JSON.parseArray(requestData);
       // System.out.println("httpRequest:1:"+ header);

       // HashMap<String,String> map=JSON.parseObject(header, HashMap.class);
       // System.out.println("httpRequest:2:"+headers.toString());
        //打开连接
        URL url = new URL(requestUrl);
       // System.out.println("HttpTool-httpRequest:0:"+url);
        HttpURLConnection conn = null;
        try {
            if (requestUrl.startsWith("https")) {
                conn = (HttpsURLConnection) url.openConnection();
                // 创建SSLContext对象，并使用我们指定的信任管理器初始化
                TrustManager[] tm = { new FakeX509TrustManager() };
                SSLContext ctx = SSLContext.getInstance("TLS");
                ctx.init((KeyManager[]) null, tm, (SecureRandom) null);
                // 从上述SSLContext对象中得到SSLSocketFactory对象
                SSLSocketFactory ssf = ctx.getSocketFactory();
                ((HttpsURLConnection) conn).setSSLSocketFactory(ssf);
            } else {
                conn =(HttpURLConnection)url.openConnection();
            }
            conn.setConnectTimeout(conTimeout);//5000
            conn.setReadTimeout(readTimeout);//12000
          //  System.out.println("HttpTool-httpRequest:1:"+requestMethod);
            if ("POST".equals(requestMethod)) {
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setRequestMethod("POST");// 设置请求方式（POST）
            } else if ("GET".equals(requestMethod)) {
                conn.setRequestMethod("GET");// 设置请求方式（GET）
            }
            conn.setUseCaches(false);

            JSONObject headers =new JSONObject(header);
            Iterator<?> it = headers.keys();
            String vol = "";//值
            String key = null;//键
            while(it.hasNext()){//遍历JSONObject
                key = (String) it.next().toString();
                vol = headers.getString(key);
                conn.setRequestProperty(key,vol);
             //   System.out.println("key:"+key+","+"value:"+vol);
            }

            conn.setRequestProperty("Content-Length", Integer.toString(requestStr.length()));
            conn.connect();
          //  System.out.println("HttpTool-httpRequest:2:"+requestStr);
            // 当有数据需要提交时
            if (null != requestStr) {
                OutputStream outputStream = conn.getOutputStream();
                // 注意编码格式，防止中文乱码
                outputStream.write(requestStr.getBytes("UTF-8"));
                outputStream.close();
            }
            // 将返回的输入流转换成字符串
         //   System.out.println("HttpTool-httpRequest:3:");
            InputStream inputStream = conn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            bufferedReader.close();
            inputStreamReader.close();
            // 释放资源
            inputStream.close();
            inputStream = null;
            conn.disconnect();

        } catch (ConnectException ce) {
            Log.d(TAG, "connection timed out.");
        } catch (Exception e) {
            Log.d(TAG, "https request error:{}", e);
        }
     //   System.out.println("HttpTool-httpRequest:6:");
        return buffer.toString();
    }

}
