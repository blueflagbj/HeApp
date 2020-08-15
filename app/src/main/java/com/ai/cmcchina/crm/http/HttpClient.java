package com.ai.cmcchina.crm.http;

import com.ai.cmcchina.crm.util.IOUtil;
import com.heclient.heapp.util.DesPlus;


import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;


public abstract class HttpClient {
    private DefaultHttpClient httpClient = new DefaultHttpClient();
    private Map<String, String> params;
    protected String requestUrl;
    protected int timeout = 30000;

    public abstract String sendRequest() throws Exception;

    public HttpClient() {
    }

    public HttpClient(String requestUrl2, Map<String, String> params2) {
        this.requestUrl = requestUrl2;
        this.params = params2;
        initHttpClient();
    }

    private void initHttpClient() {
        this.httpClient.setKeepAliveStrategy(new ConnectionKeepAliveStrategy() {
            public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
                return 120000;
            }
        });
        this.httpClient.getParams().setIntParameter("http.socket.timeout", this.timeout);
        this.httpClient.getParams().setIntParameter("http.connection.timeout", this.timeout);
        this.httpClient.getParams().setParameter("http.protocol.cookie-policy", "best-match");
    }

    /* access modifiers changed from: protected */
    public ArrayList<NameValuePair> addParams(Map<String, String> params2) throws Exception {
        if (params2 == null) {
            params2 = new HashMap<>();
        }
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
        for (Map.Entry<String, String> entry : params2.entrySet()) {
            nameValuePairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        return nameValuePairs;
    }

    /* access modifiers changed from: protected */
    public String sendPostRequest() throws HttpException, ClientProtocolException, ConnectTimeoutException, SocketTimeoutException, Exception {

        HttpPost httpPost = new HttpPost(this.requestUrl);
        httpPost.getParams().setIntParameter("http.socket.timeout", this.timeout);
        httpPost.getParams().setIntParameter("http.connection.timeout", this.timeout);
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(addParams(this.params)));
            HttpResponse httpResponse = this.httpClient.execute(httpPost);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                String result = new String(IOUtil.readStream(httpResponse.getEntity().getContent()), "utf-8");
                if (result.startsWith("a=")) {
                    result = new DesPlus().decrypt(result.substring(2));
                }
                return result;
            }

            throw new HttpException("客户端网络异常，请重试！！");
        } finally {
            this.httpClient.getConnectionManager().shutdown();
        }
    }

}
