package com.wade.mobile.util.http;

import android.util.Log;

import com.heclient.heapp.App;
import com.wade.mobile.util.Messages;
import com.wade.mobile.util.cipher.DES;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.Key;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import static android.content.ContentValues.TAG;

public class HttpTool {
    static final String TAG = HttpTool.class.getSimpleName();
    public static final int conTimeout = 5000;
    public static final String defaultEncode = "UTF-8";
    public static final int readTimeout = 120000;

    static {
        HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
            public boolean verify(String arg0, SSLSession arg1) {
                return true;
            }
        });
    }
    public static String httpRequest(String url, String data, String type,String callName) throws Exception {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("Client-Type", getEncryptFlag());
        hashMap.put("Timestamp", getCurrentTimestamp());
        hashMap.put("Content-Type", "application/x-www-form-urlencoded");
        hashMap.put("User-Agent", "Dalvik/2.1.0 (Linux; U; Android 7.1.2; vivo X9Plus Build/N2G47H)");
        hashMap.put("Host", "211.138.17.10:20100");
        hashMap.put("Connection", "Keep-Alive");
        hashMap.put("Accept-Encoding","gzip");
        //System.out.println("HttpTool-str-httpRequest:0:");
        return httpRequest(url,hashMap,type, data,callName);
    }

    /**
     * 发起https请求并获取结果
     *
     * @param requestUrl 请求地址
     * @param requestMethod 请求方式（GET、POST）
     * @param outputStr 提交的数据
     * @return String 返回字符串数据
     */
    public static String httpRequest(String requestUrl,Map<String, String>propertyMap,String requestMethod,String outputStr,String callname) throws Exception {
        StringBuffer buffer = new StringBuffer();
        InputStream is = null;
        OutputStreamWriter writer = null;
        //打开连接
        URL url = new URL(requestUrl);
/*        System.out.println("HttpTool-httpRequest:0:"+url);
        System.out.println("HttpTool-httpRequest:0-1:"+callname);*/
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
           // System.out.println("HttpTool-httpRequest:1:"+requestMethod);
            if ("POST".equals(requestMethod)) {
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setRequestMethod("POST");// 设置请求方式（POST）
            } else if ("GET".equals(requestMethod)) {
                conn.setRequestMethod("GET");// 设置请求方式（GET）
            }
            // Acts like a browser
//            Client-Type: +FmcW+pd0ZJzdk6OL6VMIBGfJB57/x5n
//            Timestamp: +DEOioDWglp1Tz7gx2zbUQFuuZAu0rfs
//            Content-Type: application/x-www-form-urlencoded
//            User-Agent: Dalvik/2.1.0 (Linux; U; Android 7.1.2; vivo X9Plus Build/N2G47H)
//            Host: 211.138.17.10:20100
//            Connection: Keep-Alive
//            Accept-Encoding: gzip
//            Content-Length: 508
            conn.setUseCaches(false);
            if(propertyMap !=null){
                for(Map.Entry<String,String> entry:propertyMap.entrySet()){
                    conn.setRequestProperty(entry.getKey(),entry.getValue());
                   // System.out.println("key:"+entry.getKey()+","+"value:"+entry.getValue());
                }
            }

/*            conn.setRequestProperty("Client-Type", getEncryptFlag());
            conn.setRequestProperty("Timestamp", getCurrentTimestamp());
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("User-Agent", "Dalvik/2.1.0 (Linux; U; Android 7.1.2; vivo X9Plus Build/N2G47H)");
            conn.setRequestProperty("Host", "211.138.17.10:20100");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Accept-Encoding","gzip");*/
            conn.setRequestProperty("Content-Length", Integer.toString(outputStr.length()));
//            conn.setRequestProperty("Accept",
//                    "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
//            conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
//            for (String cookie : this.cookies) {
//                conn.addRequestProperty("Cookie", cookie.split(";", 1)[0]);
//            }
//            conn.setRequestProperty("Referer", "https://accounts.google.com/ServiceLoginAuth");
           conn.connect();

           // System.out.println("HttpTool-httpRequest:2:"+outputStr);
            // 当有数据需要提交时
            if (null != outputStr) {
                OutputStream outputStream = conn.getOutputStream();
                // 注意编码格式，防止中文乱码
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();

            }
            // 将返回的输入流转换成字符串
           // System.out.println("HttpTool-httpRequest:3:");
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
      //  System.out.println("HttpTool-httpRequest:6:");
        return buffer.toString();
    }


    public static String httpDownload(String requestUrl, String filePath) throws Exception {
        return httpDownload(requestUrl, filePath, 5000, new DownloadOper(), false);
    }

    public static String httpDownload(String requestUrl, String filePath, int timeout, DownloadOper oper) throws Exception {
        return httpDownload(requestUrl, filePath, timeout, oper, false);
    }
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r8v7, resolved type: javax.net.ssl.HttpsURLConnection} */
    /* JADX WARNING: type inference failed for: r24v8, types: [java.net.URLConnection] */
    /* JADX WARNING: type inference failed for: r24v23, types: [java.net.URLConnection] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x010b  */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x0112  */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x0119  */
    public static String httpDownload(String requestUrl, String filePath, int timeout, DownloadOper oper, boolean isResume) throws Exception {
        InputStream in = null;
        OutputStream out = null;
        boolean flag = true;
        URL url = new URL(requestUrl);
        HttpURLConnection conn = null;
        String response="";
        try {
            if (requestUrl.startsWith("https")) {
                conn = (HttpsURLConnection) url.openConnection();
                // 创建SSLContext对象，并使用我们指定的信任管理器初始化
                TrustManager[] tm = {new FakeX509TrustManager()};
                SSLContext ctx = SSLContext.getInstance("TLS");
                ctx.init((KeyManager[]) null, tm, (SecureRandom) null);
                // 从上述SSLContext对象中得到SSLSocketFactory对象
                SSLSocketFactory ssf = ctx.getSocketFactory();
                ((HttpsURLConnection) conn).setSSLSocketFactory(ssf);
            } else {
                conn = (HttpURLConnection) url.openConnection();
            }

            conn.setConnectTimeout(timeout);
            conn.setRequestProperty("Accept-Encoding", "identity");
            conn.setRequestProperty("Client-Type", getEncryptFlag());
            conn.setRequestProperty("Timestamp", getCurrentTimestamp());
            if (isResume) {
                File file = new File(filePath);
                if (file.exists()) {
                    conn.setRequestProperty("RANGE", "bytes=" + file.length() + "-");
                }
            }
            if (conn.getResponseCode() == 200 || conn.getResponseCode() == 206) {
                in = conn.getInputStream();
                int fileSize = conn.getContentLength();
                if (fileSize < 0) {
                    throw new RuntimeException(Messages.EXCEPTION_FILE_SIZE);
                } else if (in == null) {
                    throw new RuntimeException(Messages.FILE_STREAM_NULL);
                } else {
                    oper.startDownload(fileSize);
                    FileOutputStream fileOutputStream = new FileOutputStream(filePath);
                    try {
                        BufferedInputStream buffIn = new BufferedInputStream(in);
                        BufferedOutputStream buffOut = new BufferedOutputStream(fileOutputStream);
                        byte[] bytes = new byte[8192];
                        while (true) {
                            int c = buffIn.read(bytes);
                            if (c == -1) {
                                break;
                            }
                            buffOut.write(bytes, 0, c);
                            oper.downloading(fileSize, c);
                        }
                        buffOut.flush();
                         response = Messages.DOWNLOAD_SUCCESS;
                        oper.endDownload(true, response);

                    } catch (Exception e) {
                        Log.e(TAG, "httpDownload: ", e);
                        out = fileOutputStream;
                    } finally {
                        if (in != null) {
                            in.close();
                        }
                        if (fileOutputStream != null) {
                            fileOutputStream.close();
                        }
                        if (out != null) {
                            out.close();
                        }
                        if (conn != null) {
                            conn.disconnect();
                        }
                    }
                }
            } else {
                throw new RuntimeException(Messages.EXCEPTION_CONN);
            }
        } catch (Exception e2) {
            Log.d(TAG, "httpDownload: " + String.valueOf(e2.getMessage()) + ":" + requestUrl);

        }
      return  response;
    }
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v5, resolved type: javax.net.ssl.HttpsURLConnection} */
    /* JADX WARNING: type inference failed for: r12v16, types: [java.net.URLConnection] */
    /* JADX WARNING: Multi-variable type inference failed */
    public static String httpDownload(String requestUrl, DownStreamOper oper) throws Exception {
        HttpURLConnection conn;
        InputStream in = null;
        oper.startDown();
        URL url = new URL(requestUrl);
        HttpURLConnection conn2 = null;
        try {
            if (requestUrl.startsWith("https")) {
                conn = (HttpsURLConnection) url.openConnection();
                TrustManager[] tm = {new FakeX509TrustManager()};
                SSLContext ctx = SSLContext.getInstance("TLS");
                ctx.init((KeyManager[]) null, tm, (SecureRandom) null);
                ((HttpsURLConnection) conn).setSSLSocketFactory(ctx.getSocketFactory());
            } else {
                conn = (HttpURLConnection) url.openConnection();
            }
            conn.setConnectTimeout(5000);
            conn.setRequestProperty("Accept-Encoding", "identity");
            conn.setRequestProperty("Client-Type", getEncryptFlag());
            conn.setRequestProperty("Timestamp", getCurrentTimestamp());
            if (conn.getResponseCode() != 200) {
                throw new RuntimeException(Messages.EXCEPTION_CONN);
            }
            InputStream in2 = conn.getInputStream();
            if (conn.getContentLength() <= 0) {
                throw new RuntimeException(Messages.EXCEPTION_FILE_SIZE);
            } else if (in2 == null) {
                throw new RuntimeException(Messages.FILE_STREAM_NULL);
            } else {
                oper.downloading(in2);
                String response = Messages.DOWNLOAD_SUCCESS;
                if (in2 != null) {
                    in2.close();
                }
                if (conn != null) {
                    conn.disconnect();
                }
                oper.endDown(true, response);
                return response;
            }
        } catch (Exception e) {
            Log.d(TAG, "httpDownload: "+e.getMessage()+ ":" + requestUrl);
            throw e;
        } catch (Throwable th) {
            if (in != null) {
                in.close();
            }
            if (conn2 != null) {
                conn2.disconnect();
            }
            oper.endDown(false, (String) null);
            throw th;
        }
    }



    private static class DownloadOper {
        public void startDownload(int fileSize) {
        }

        public void downloading(int fileSize, int count) {
        }

        public void endDownload(boolean flag, String response) {
        }
    }
    public static class DownStreamOper {
        public void startDown() {
        }

        public void downloading(InputStream in) throws Exception {
        }

        public void endDown(boolean flag, String response) {
        }
    }

    public static String getCurrentTimestamp() throws Exception {
        String str = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date());
        return DES.encryptString(getKey(), str);
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

    public static String toQueryStringWithEncode(Map<String, String> data) {
        StringBuilder buff = new StringBuilder();
        for (String key : data.keySet()) {
            buff.append(postDataEncode(key)).append("=").append(postDataEncode(data.get(key))).append("&");
        }
        buff.setLength(buff.length() - 1);
        return buff.toString();
    }
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

    public static String urlEncode(String url, String encode) {
        if (url == null || "".equals(url)) {
            return "";
        }
        if (encode == null || "".equals(encode)) {
            encode = defaultEncode;
        }
        Matcher m = Pattern.compile("[㐀-䶵一-龥龦-龻豈-鶴侮-頻並-龎＀-￯⺀-⻿　-〿㇀-㇯\\s]+").matcher(url);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            try {
                m.appendReplacement(sb, URLEncoder.encode(url.substring(m.start(), m.end()), encode));
            } catch (Exception e) {
            }
        }
        m.appendTail(sb);
        return sb.toString();
    }

    public static String urlEscape(String url) throws URISyntaxException, MalformedURLException {
        URL _url = new URL(url);
        return new URI(_url.getProtocol(), _url.getUserInfo(), _url.getHost(), _url.getPort(), _url.getPath(), _url.getQuery(), (String) null).toString();
    }
    public static String toQueryString(Map<String, String> data) {
       // System.out.println("HttpTool-toQueryString:0:"+data.toString());
        StringBuilder buff = new StringBuilder();
        for (String key : data.keySet()) {
            buff.append(key).append("=").append(data.get(key)).append("&");
        }
        buff.setLength(buff.length() - 1);
      //  System.out.println("HttpTool-toQueryString:1:"+buff.toString());
        return buff.toString();
    }

}
