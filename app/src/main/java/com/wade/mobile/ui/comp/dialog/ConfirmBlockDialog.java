package com.wade.mobile.ui.comp.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/* renamed from: com.wade.mobile.ui.comp.dialog.ConfirmBlockDialog */
public class ConfirmBlockDialog extends AlertDialog {
    private String cancelLabel = "取消";
    private Handler mHandler;
    private String okLabel = "确定";
    private Result result;

    /* renamed from: com.wade.mobile.ui.comp.dialog.ConfirmBlockDialog$Result */
    public enum Result {
        OK,
        CANCEL
    }

    public ConfirmBlockDialog(Activity context, String title, String tip) {
        super(context);
        setOwnerActivity(context);
        if (title != null) {
            setTitle(title);
        }
        if (tip != null) {
            setMessage(tip);
        }
    }

    public ConfirmBlockDialog(Activity context, String title, String tip, String okLabel2, String cancelLabel2) {
        super(context);
        setOwnerActivity(context);
        if (title != null) {
            setTitle(title);
        }
        if (tip != null) {
            setMessage(tip);
        }
        this.okLabel = okLabel2;
        this.cancelLabel = cancelLabel2;
    }

    public Result getResult() {
        return this.result;
    }

    public void show() {
        this.mHandler = new Handler() {
            public void handleMessage(Message mesg) {
                throw new RuntimeException();
            }
        };
        if (this.okLabel != null) {
            setButton(-1, this.okLabel, new OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    ConfirmBlockDialog.this.dismissDialog(Result.OK);
                }
            });
        }
        if (this.cancelLabel != null) {
            setButton(-2, this.cancelLabel, new OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    ConfirmBlockDialog.this.dismissDialog(Result.CANCEL);
                }
            });
        }
        super.show();
        try {
            Looper.getMainLooper();
            Looper.loop();
        } catch (RuntimeException e) {
        }
    }

    /* access modifiers changed from: private */
    public void dismissDialog(Result result2) {
        this.result = result2;
        this.mHandler.sendMessage(this.mHandler.obtainMessage());
        super.dismiss();
    }

    public void setOkLabel(String okLabel2) {
        this.okLabel = okLabel2;
    }

    public void setCancelLabel(String cancelLabel2) {
        this.cancelLabel = cancelLabel2;
    }
}