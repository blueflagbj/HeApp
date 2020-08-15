package com.wade.mobile.frame.impl;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.util.Log;
import android.view.Window;
import com.wade.mobile.frame.IWadeMobile;
import com.wade.mobile.frame.IWadeMobileClient;
import com.wade.mobile.ui.comp.dialog.ConfirmDialog;
import com.wade.mobile.ui.comp.dialog.HintDialog;
import com.wade.mobile.util.Constant;
import com.wade.mobile.util.Messages;
import org.json.JSONArray;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class WadeMobileClient implements IWadeMobileClient {
    protected Activity context;
    protected IWadeMobile wademobile;

    public WadeMobileClient(IWadeMobile wademobile2) {
        this.wademobile = wademobile2;
        this.context = wademobile2.getActivity();
        Log.d(TAG, "WadeMobileClient: 0");
    }

    public void settingWindowStyle(Window window) {
        window.requestFeature(1);
        window.setFlags(2048, 2048);
    }

    public void execute(String action, Object[] params) {
        JSONArray array = new JSONArray();
        if (params != null) {
            for (Object param : params) {
                array.put(param);
            }
        }
        System.out.println("WadeMobileClientExecute0001"+array.toString());
        execute(action, array);
    }

    public void execute(String action) {
        execute(action, new JSONArray());
    }

    /* access modifiers changed from: protected */
    public void execute(String action, JSONArray params) {

        this.wademobile.getPluginManager().execute(action, params);
    }

    public void shutdownByConfirm(String message) {
        new ConfirmDialog(this.context, message, (String) null) {
            /* access modifiers changed from: protected */
            public void okEvent() {
                super.okEvent();
                WadeMobileClient.this.exitApp();
            }
        }.show();
    }

    public void exitAppByConfirm(String message) {
        shutdownByConfirm(message);
    }

    public void exitApp() {
        Intent intent = new Intent();
        intent.setAction(Constant.Broadcast.EXIT_APP_ACTION);
        this.context.sendBroadcast(intent);
    }

    public void alert(final String message, final String action, final Object[] param) {
        this.context.runOnUiThread(new Runnable() {
            public void run() {
                WadeMobileClient.this.createAlert(message, Messages.DIALOG_TITLE_HINT, action, param).show();
            }
        });
    }

    public AlertDialog.Builder createAlert(String message, String title, String action, Object[] param) {
        final String str = action;
        final Object[] objArr = param;
        return new HintDialog(this.context, message, title) {
            /* access modifiers changed from: protected */
            public void clickEvent() {
                super.clickEvent();
                if (str != null) {
                    WadeMobileClient.this.execute(str, objArr);
                }
            }
        };
    }

    public void confirm(String message, String title, String positiveAction, Object[] positiveParam, String negativeAction, Object[] negativeParam) {
        final String str = message;
        final String str2 = title;
        final String str3 = positiveAction;
        final Object[] objArr = positiveParam;
        final String str4 = negativeAction;
        final Object[] objArr2 = negativeParam;
        this.context.runOnUiThread(new Runnable() {
            public void run() {
                WadeMobileClient.this.createConfirm(str, str2, str3, objArr, str4, objArr2).show();
            }
        });
    }

    public AlertDialog.Builder createConfirm(String message, String title, String positiveAction, Object[] positiveParam, String negativeAction, Object[] negativeParam) {
        final String str = positiveAction;
        final Object[] objArr = positiveParam;
        final String str2 = negativeAction;
        final Object[] objArr2 = negativeParam;
        return new ConfirmDialog(this.context, title, message) {
            /* access modifiers changed from: protected */
            public void okEvent() {
                super.okEvent();
                if (str != null) {
                    WadeMobileClient.this.execute(str, objArr);
                }
            }

            /* access modifiers changed from: protected */
            public void cancelEvent() {
                super.cancelEvent();
                if (str2 != null) {
                    WadeMobileClient.this.execute(str2, objArr2);
                }
            }
        };
    }
}