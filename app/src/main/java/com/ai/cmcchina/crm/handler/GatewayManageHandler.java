package com.ai.cmcchina.crm.handler;

import android.app.Activity;
import android.os.Build;
import android.util.Log;


import com.ai.cmcchina.crm.http.GatewayClient;

import com.heclient.heapp.App;
import com.wade.mobile.frame.config.MobileConfig;

import org.apache.http.HttpException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.conn.ConnectTimeoutException;

import java.net.SocketTimeoutException;
import java.util.Map;
import java.util.Objects;

import static androidx.constraintlayout.widget.Constraints.TAG;

public abstract class GatewayManageHandler extends RemoteHandler {
    private Map<String, String> params;

    public GatewayManageHandler(Activity context, String uri, Map<String, String> params2) {
        super(context, uri, params2);
        this.params = params2;
    }

    public String sendHttpRequest(int timeout) throws HttpException, ClientProtocolException, ConnectTimeoutException, SocketTimeoutException, Exception {
         String url = MobileConfig.getInstance().getRequestUrl();

        String sessionID=((App) this.context.getApplication()).getSessionId();
        this.params.put("SESSION_ID",sessionID);
       // Log.d(TAG, "sendHttpRequest:0: "+url);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Log.d(TAG, "sendHttpRequest: 1:"+sessionID);
        }
        String result = new GatewayClient(this.context).sendRequest(url, this.params);
       // Log.d(TAG, "GatewayManageHandler result:" + result);
        return result;
    }

}
