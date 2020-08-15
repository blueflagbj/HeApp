package com.ai.cmcchina.crm.handler;

import android.app.Activity;
import com.alibaba.fastjson.JSONObject;
import com.ai.cmcchina.crm.constant.Constant;
import com.ai.cmcchina.crm.http.IPURemoteClient;
import com.ai.cmcchina.crm.util.StringUtil;

import com.heclient.heapp.App;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Map;
import org.apache.http.HttpException;
import org.apache.http.client.ClientProtocolException;

/* renamed from: com.ai.cmcchina.crm.handler.IPURemoteHandler */
public abstract class IPURemoteHandler extends RemoteHandler {
    private String targetServer = IPURemoteClient.CRM_SERVER;

    public abstract void onSuccess(String str);

    public IPURemoteHandler(Activity context, Map<String, String> params) {
        super(context, "/mobiledata", params);
    }

    public IPURemoteHandler(Activity context, String uri, Map<String, String> params) {
        super(context, uri, params);
    }

    public IPURemoteHandler(Activity context, String uri, Map<String, String> params, String targetServer2) {
        super(context, uri, params);
        if (StringUtil.isNotBlank(targetServer2)) {
            this.targetServer = targetServer2;
        }
    }

    public IPURemoteHandler(Activity context, Map<String, String> params, String targetServer2) {
        super(context, "/mobiledata", params);
        if (StringUtil.isNotBlank(targetServer2)) {
            this.targetServer = targetServer2;
        }
    }

    public IPURemoteHandler(Activity context, String uri, Map<String, String> params, String targetServer2, boolean showProgressDlg) {
        super(context, uri, params, showProgressDlg);
        if (StringUtil.isNotBlank(targetServer2)) {
            this.targetServer = targetServer2;
        }
    }

    public IPURemoteHandler(Activity context, String uri, Map<String, String> params, String targetServer2, boolean showProgressDlg, String prgDialogMessage) {
        super(context, uri, params, showProgressDlg, prgDialogMessage);
        if (StringUtil.isNotBlank(targetServer2)) {
            this.targetServer = targetServer2;
        }
    }

    public IPURemoteHandler setTimeout(int timeout) {
        this.timeout = timeout;
        return this;
    }

    public String sendHttpRequest(int timeout) throws HttpException, ClientProtocolException, IOException, SocketTimeoutException, Exception {
        addSessionId();
        String result = new IPURemoteClient(this.context, this.targetServer).sendRequest(this.params);
       // G3Logger.debug(this.targetServer + "服务器 result:" + result);
        return result;
    }

    private void addSessionId() {
        String sessionId = ((App) this.context.getApplication()).getSessionId();
        JSONObject jsonObject = JSONObject.parseObject((String) this.params.get("data"));
        jsonObject.put(Constant.SESSION_ID, (Object) sessionId);
        this.params.put("data", jsonObject.toJSONString());
    }
}