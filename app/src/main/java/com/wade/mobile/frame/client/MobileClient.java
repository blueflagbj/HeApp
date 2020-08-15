package com.wade.mobile.frame.client;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import com.wade.mobile.frame.SafeWebView;
import com.wade.mobile.util.Messages;

public class MobileClient extends WebChromeClient {
    private Context ctx;

    public MobileClient(Context ctx2) {
        this.ctx = ctx2;
    }

    @SuppressLint("ResourceType")
    public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
        System.out.println("MobileClient-onJsAlert:0:"+view.toString());
        System.out.println("MobileClient-onJsAlert:1:"+url);
        System.out.println("MobileClient-onJsAlert:2:"+message);

        AlertDialog.Builder dlg = new AlertDialog.Builder(this.ctx);
        dlg.setMessage(message);
        dlg.setTitle(Messages.DIALOG_TITLE_HINT);
        dlg.setCancelable(false);
        dlg.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                result.confirm();
            }
        });
        dlg.show();
        return true;
    }

    @SuppressLint("ResourceType")
    public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
        AlertDialog.Builder dlg = new AlertDialog.Builder(this.ctx);
        dlg.setMessage(message);
        dlg.setTitle(Messages.DIALOG_TITLE_HINT);
        dlg.setCancelable(false);
        dlg.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                result.confirm();
            }
        });
        dlg.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                result.cancel();
            }
        });
        dlg.show();
        return true;
    }

    public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
        if (!(view instanceof SafeWebView)) {
            return super.onJsPrompt(view, url, message, defaultValue, result);
        }
        SafeWebView swv = (SafeWebView) view;
        if (!swv.isSafeInject() || !message.startsWith(SafeWebView.JS_PROMPT_MSG_HEADER)) {
            return super.onJsPrompt(view, url, message, defaultValue, result);
        }
        return swv.handleJsPrompt(view, url, message, defaultValue, result);
    }
}