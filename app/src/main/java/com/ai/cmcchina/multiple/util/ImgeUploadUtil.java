package com.ai.cmcchina.multiple.util;


import android.annotation.SuppressLint;

import com.ai.cmcchina.crm.util.StringUtil;

import com.heclient.heapp.util.DesPlus;
import com.wade.mobile.frame.config.MobileConfig;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyStore;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;

import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;


/* renamed from: com.ai.cmcchina.multiple.util.ImgeUploadUtil */
public class ImgeUploadUtil {
    private static final String BOUNDRY = "---------------------------7dbe81d3124a";
    static final HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };
    public static final String ERROR_CRYPT = "数据异常";
    public static final String ERROR_FAILED = "服务器链接失败！";
    public static final String ERROR_SERVER = "服务器异常";
    public static final String ERROR_TIMEOUT = "服务器超时，请重试";
    public static final String ERROR_UNKNOW = "未知异常:";
    private static final String HTTPS = "https";
    private static final String LINE_END = "\r\n";
    private static final String LINE_START = "--";
    private static HostnameVerifier defaultHostnameVerifier = null;
    private static SSLSocketFactory defaultSSLSocketFactory = null;
    public static final boolean encrypted = true;

    /* JADX WARNING: type inference failed for: r30v18, types: [java.net.URLConnection] */
    /* JADX WARNING: Multi-variable type inference failed */
    @SuppressLint({"DefaultLocale"})
    public static String postWithAttachment(String esopServerAddr, int timeout, Map<String, String> params, String[] files, boolean trustEveryone) throws ClientProtocolException, IOException, Exception {
        String opId = params.get("opId");
        if (StringUtil.isBlank(opId)) {
            opId = "";
        }
        String uri = esopServerAddr + params.get("BUSINESS_ACTION") + "?action=" + params.get("BUSINESS_METHOD") + "&opId=" + opId;
        HttpURLConnection connection = null;
        DataInputStream inStream = null;
        try {
            URL url = new URL(uri);
            if (url.getProtocol().toLowerCase().equals("https")) {
                connection = getHttpsConnection(trustEveryone, url);
            } else {
                connection = (HttpURLConnection) url.openConnection();
            }
            setConnectionParams(connection);
            DataOutputStream dos = new DataOutputStream(connection.getOutputStream());
            try {
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    String value = entry.getValue();
                    dos.writeBytes("-----------------------------7dbe81d3124a\r\n");
                    dos.writeBytes("Content-Disposition: form-data; name=\"" + entry.getKey().toString() + "\";");
                    dos.writeBytes("\r\n\r\n");
                    if (value == null) {
                        value = "";
                    }
                    if (Pattern.compile("([\\u4E00-\\u9FA5])").matcher(value).find()) {
                        value = URLEncoder.encode(value);
                    }
                    dos.writeBytes(value);
                    dos.writeBytes("\r\n");
                }
                if (files != null) {
                    if (!"".equals(files)) {
                        for (int i = 0; i < files.length; i++) {
                            String file = files[i];
                            // G3Logger.debug("ImgeUploadUtil图片上传路径>>>" + file);
                            String[] fileItem = files[i].split("/");
                            String fileName = fileItem[fileItem.length - 1];
                            FileInputStream fileInputStream = new FileInputStream(new File(file));
                            dos.writeBytes("-----------------------------7dbe81d3124a\r\n");
                            dos.writeBytes("Content-Disposition: form-data; name=\"file\"; filename=\"" + fileName + "\"" + "\r\n");
                            dos.writeBytes("Content-Type: image/jpeg\r\n");
                            dos.writeBytes("\r\n");
                            int bufferSize = Math.min(fileInputStream.available(), 8096);
                            byte[] buffer = new byte[bufferSize];
                            int bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                            while (bytesRead > 0) {
                                dos.write(buffer, 0, bufferSize);
                                bufferSize = Math.min(fileInputStream.available(), 8096);
                                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                            }
                            dos.writeBytes("\r\n");
                            fileInputStream.close();
                        }
                    }
                }
                dos.writeBytes("-----------------------------7dbe81d3124a--");
                dos.writeBytes("\r\n");
                dos.flush();
                dos.close();
                StringBuffer stringBuffer = new StringBuffer("");
                DataInputStream dataInputStream = new DataInputStream(connection.getInputStream());
                while (true) {
                    try {
                        String line = dataInputStream.readLine();
                        if (line == null) {
                            break;
                        }
                        stringBuffer.append(line);
                    } catch (Throwable th) {
                        th = th;
                        inStream = dataInputStream;
                        DataOutputStream dataOutputStream = dos;
                    }
                }
                String result = stringBuffer.toString();
                if (uri.contains("com.ai.esp.g3")) {
                    try {
                        result = new DesPlus("hnesop").decrypt(result, "gbk");
                    } catch (Exception e) {
                    }
                }
                if (dataInputStream != null) {
                    try {
                        dataInputStream.close();
                    } catch (IOException e2) {
                        // G3Logger.debug(e2);
                    }
                }
                if (connection != null) {
                    try {
                        connection.disconnect();
                    } catch (Exception e3) {
                        // G3Logger.debug(e3);
                    }
                }
                return result;
            } catch (FileNotFoundException e4) {
                throw new IOException("服务器链接错误");
            } catch (Throwable th2) {
                DataOutputStream dataOutputStream2 = dos;
                if (inStream != null) {
                    try {
                        inStream.close();
                    } catch (IOException e5) {
                        // G3Logger.debug(e5);
                    }
                }
                if (connection != null) {
                    try {
                        connection.disconnect();
                    } catch (Exception e6) {
                        // G3Logger.debug(e6);
                    }
                }
                throw th2;
            }
        } catch (Throwable th3) {
            throw  th3;
        }
    }

    private static void setConnectionParams(HttpURLConnection connection) throws ProtocolException {
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setUseCaches(false);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Connection", "Keep-Alive");
        connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=---------------------------7dbe81d3124a");
    }

    private static HttpURLConnection getHttpsConnection(boolean trustEveryone, URL url) throws IOException {
        if (!trustEveryone) {
            return (HttpsURLConnection) url.openConnection();
        }
        HttpsURLConnection https = (HttpsURLConnection) url.openConnection();
        defaultHostnameVerifier = https.getHostnameVerifier();
        https.setHostnameVerifier(DO_NOT_VERIFY);
        https.setHostnameVerifier(defaultHostnameVerifier);
        HttpsURLConnection.setDefaultSSLSocketFactory(defaultSSLSocketFactory);
        return https;
    }

    private static DefaultHttpClient getHttpsClient() {
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load((InputStream) null, (char[]) null);
            org.apache.http.conn.ssl.SSLSocketFactory sf = new SSLSocketFactoryEx(trustStore);
            sf.setHostnameVerifier(org.apache.http.conn.ssl.SSLSocketFactory.STRICT_HOSTNAME_VERIFIER);
            HttpParams params = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(params, 10000);
            HttpConnectionParams.setSoTimeout(params, 10000);
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, "UTF-8");
            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", sf, 443));
            return new DefaultHttpClient(new ThreadSafeClientConnManager(params, registry), params);
        } catch (Exception e) {
            return new DefaultHttpClient();
        }
    }

    public static String urlEncode(String url, String encode) {
        if (url == null || "".equals(url)) {
            return "";
        }
        if (encode == null || "".equals(encode)) {
            encode = MobileConfig.getInstance().getEncode();
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

    public static String toQueryString(Map<String, String> data) {
        StringBuilder buff = new StringBuilder();
        for (String key : data.keySet()) {
            buff.append(key).append("=").append(data.get(key)).append("&");
        }
        buff.setLength(buff.length() - 1);
        return buff.toString();
    }

    public static String urlEscape(String url) throws URISyntaxException, MalformedURLException {
        URL _url = new URL(url);
        return new URI(_url.getProtocol(), _url.getUserInfo(), _url.getHost(), _url.getPort(), _url.getPath(), _url.getQuery(), (String) null).toString();
    }
}