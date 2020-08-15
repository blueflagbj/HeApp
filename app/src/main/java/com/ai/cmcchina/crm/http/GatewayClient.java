package com.ai.cmcchina.crm.http;

import android.app.Activity;
import android.util.Log;

import com.ai.cmcchina.crm.http.IPURemoteClient;
import com.wade.mobile.frame.config.MobileConfig;

import org.apache.http.HttpException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.conn.ConnectTimeoutException;

import java.util.Map;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class GatewayClient extends IPURemoteClient {
    public GatewayClient() {
        this.requestUrl = MobileConfig.getInstance().getRequestUrl();
    }

    public GatewayClient(Activity context) {
        this.context = context;
        this.requestUrl = MobileConfig.getInstance().getRequestUrl();
    }

    public String sendRequest() throws HttpException, ClientProtocolException, ConnectTimeoutException, Exception {

       return sendPostRequest();
    }

    public String sendRequest(String url, Map<String, String> params) throws Exception {
        Log.d(TAG, "sendRequest:3: "+url);
        return super.sendRequest(url, params);
    }

}
