package com.ai.cmcchina.crm.http;

import com.alibaba.fastjson.JSONObject;
import com.heclient.heapp.util.DesPlus;


import java.util.ArrayList;
import java.util.Map;
import org.apache.http.HttpException;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.message.BasicNameValuePair;


/* renamed from: com.ai.cmcchina.crm.http.CRMRemoteClient */
public class CRMRemoteClient extends HttpClient {
    public CRMRemoteClient(String requestUrl, Map<String, String> params) {
        super(requestUrl, params);
    }

    public CRMRemoteClient(String url, int timeout, Map<String, String> params) {
        super(url, params);
        this.timeout = timeout * 1000;
    }

    public String sendRequest() throws HttpException, ClientProtocolException, ConnectTimeoutException, Exception {
        return sendPostRequest();
    }

    /* access modifiers changed from: protected */
    public ArrayList<NameValuePair> addParams(Map<String, String> params) throws Exception {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
        String paramSuffix = null;
        if (this.requestUrl.contains("?")) {
            paramSuffix = this.requestUrl.substring(this.requestUrl.indexOf("?") + 1);
        }
        nameValuePairs.add(new BasicNameValuePair("a", makeParams(paramSuffix, params)));
        return nameValuePairs;
    }

    private String makeParams(String paramSuffix, Map<String, String> params) throws Exception {
        JSONObject json = new JSONObject();
        if (paramSuffix != null) {
            for (String entry : paramSuffix.split("&")) {
                String[] kv = entry.split("=");
                json.put(kv[0], (Object) kv[1]);
            }
        }
        if (params != null) {
            for (Map.Entry<String, String> entry2 : params.entrySet()) {
                json.put(entry2.getKey(), (Object) entry2.getValue());
            }
        }
        return new DesPlus().encrypt(json.toString());
    }
}