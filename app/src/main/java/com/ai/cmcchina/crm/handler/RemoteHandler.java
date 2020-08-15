package com.ai.cmcchina.crm.handler;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.Map;
import org.apache.http.HttpException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.conn.ConnectTimeoutException;

import static androidx.constraintlayout.widget.Constraints.TAG;


public abstract class RemoteHandler extends Handler implements Runnable {
    public static final int MSG_ERROR = 0;
    public static final int MSG_SUCCESS = 1;
    protected Activity context;
    protected boolean isCancelProcess;
    protected String msg;
    protected Map<String, String> params;
    protected ProgressDialog prossDialog;
    protected boolean showProgressDlg;
    protected int timeout;
    protected String uri;

    public abstract void onSuccess(String str);
    public abstract String sendHttpRequest(int i) throws HttpException, ClientProtocolException, IOException, SocketTimeoutException, Exception;

    public RemoteHandler(Activity context2, String uri2, Map<String, String> params2) {
        this(context2, uri2, params2, true);
    }
    public RemoteHandler(Activity context2, String uri2, Map<String, String> params2, boolean showProgressDlg2) {
        this.msg = "加载中，请稍候...";
        this.timeout = -1;
        this.uri = uri2;
        this.params = params2;
        this.context = context2;
        this.showProgressDlg = showProgressDlg2;
    }

    public RemoteHandler(Activity context2, String uri2, Map<String, String> params2, boolean showProgressDlg2, boolean prgDialogCancelable) {
        this.msg = "加载中，请稍候...";
        this.timeout = -1;
        this.context = context2;
        this.uri = uri2;
        this.params = params2;
        this.showProgressDlg = showProgressDlg2;
        this.isCancelProcess = prgDialogCancelable;
    }

    public RemoteHandler(Activity context2, String uri2, Map<String, String> params2, boolean showProgressDlg2, String prgDialogMessage) {
        this.msg = "加载中，请稍候...";
        this.timeout = -1;
        this.context = context2;
        this.uri = uri2;
        this.params = params2;
        this.showProgressDlg = showProgressDlg2;
        this.msg = prgDialogMessage;
    }

    public void start() {
        if (this.showProgressDlg) {
            this.prossDialog = displayWaiting(this.context, this.msg);
            this.prossDialog.setCancelable(this.isCancelProcess);
        }
        new Thread(this).start();
    }
    public static ProgressDialog displayWaiting(Context context, String message) {
        ProgressDialog dialog = ProgressDialog.show(context, "", message, true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);
        return dialog;
    }

    public void handleMessage(Message msg2) {
        super.handleMessage(msg2);
        if (this.prossDialog != null && this.prossDialog.isShowing()) {
            this.prossDialog.dismiss();
        }
        String result = (String) msg2.obj;
        if (result == null) {
            result = "";
        }
        if (msg2.what == 0) {
            onError(result);
        } else if (msg2.what == 1) {
            onSuccess(result);
        }
    }

    public void onError(String result) {
        if (this.showProgressDlg) {
            dismissWaiting(this.prossDialog);
            Log.d(TAG, "onError: "+result);

            //toast((Context) this.context, (Object) result);
        }
    }
    public static void dismissWaiting(ProgressDialog pDlg) {
        if (pDlg != null && pDlg.isShowing()) {
            pDlg.dismiss();
        }
    }

    public void run() {
        try {
            String result = sendHttpRequest(this.timeout);
            sendMessage(obtainMessage(1, result));
        } catch (ClientProtocolException e) {
            sendMessage(obtainMessage(0, "请检查网络" + e.getMessage()));
        } catch (HttpException e2) {
            sendMessage(obtainMessage(0, e2.getMessage()));
        } catch (ConnectTimeoutException e3) {
            sendMessage(obtainMessage(0, "服务器连接超时，请稍候重试"));
        } catch (SocketTimeoutException e4) {
            sendMessage(obtainMessage(0, "服务器连接超时，请稍候重试"));
        } catch (ConnectException e5) {
            sendMessage(obtainMessage(0, "服务器连接失败"));
        } catch (Exception e6) {
            sendMessage(obtainMessage(0, "其他错误：" + e6.getMessage()));
        }
    }




}
