package com.ai.cmcchina.crm.handler;


import android.app.Activity;


import com.ai.cmcchina.crm.constant.Constant;
import com.ai.cmcchina.crm.http.CRMRemoteClient;
import com.ai.cmcchina.crm.util.Global;
import com.heclient.heapp.App;

import java.net.SocketTimeoutException;
import java.util.Map;
import org.apache.http.HttpException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.conn.ConnectTimeoutException;

/* renamed from: com.ai.cmcchina.crm.handler.CRMRemoteHandler */
public abstract class CRMRemoteHandler extends RemoteHandler {
    public static String brandFlag = "";
    private Map<String, String> params;

    public CRMRemoteHandler(Activity context, String uri, Map<String, String> params2) {
        super(context, uri, params2);
        this.uri = uri;
        this.params = params2;
    }

    public CRMRemoteHandler(Activity context, String uri, Map<String, String> params2, boolean showProgressDlg) {
        super(context, uri, params2, showProgressDlg);
        this.context = context;
        this.uri = uri;
        this.params = params2;
        this.showProgressDlg = showProgressDlg;
    }

    public CRMRemoteHandler(Activity context, String uri, Map<String, String> params2, boolean showProgressDlg, boolean prgDialogCancelable) {
        super(context, uri, params2, showProgressDlg, prgDialogCancelable);
        this.context = context;
        this.uri = uri;
        this.params = params2;
        this.showProgressDlg = showProgressDlg;
        this.isCancelProcess = prgDialogCancelable;
    }

    public CRMRemoteHandler(Activity context, String uri, Map<String, String> params2, boolean showProgressDlg, String prgDialogMessage) {
        super(context, uri, params2, showProgressDlg, prgDialogMessage);
        this.context = context;
        this.uri = uri;
        this.params = params2;
        this.showProgressDlg = showProgressDlg;
        this.msg = prgDialogMessage;
    }

    public CRMRemoteHandler setTimeout(int timeout) {
        this.timeout = timeout;
        return this;
    }

    public String sendHttpRequest(int timeout) throws HttpException, ClientProtocolException, ConnectTimeoutException, SocketTimeoutException, Exception {
        App app = (App) this.context.getApplication();
        String sessionId = app.getSessionId();
        String url = app.getCrmServerPath() + this.uri;
        this.params.put(Global.SESSION_ID, sessionId);
        this.params.put(Constant.SESSION_ID, sessionId);

/*        G3Logger.debug("url:" + url);
        G3Logger.debug("timeout:" + timeout);
        G3Logger.debug("params:" + this.params);*/
        if (timeout > 0) {
            String result = new CRMRemoteClient(url, timeout, this.params).sendRequest();
            String str = result;
            return result;
        }
        String result2 = new CRMRemoteClient(url, this.params).sendRequest();
        String str2 = result2;
        return result2;
    }
}