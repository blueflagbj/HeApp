package com.wade.mobile.ui.comp.dialog;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/* renamed from: com.wade.mobile.ui.comp.dialog.HintDialog */
public class HintDialog extends AlertDialog.Builder {
    @SuppressLint("ResourceType")
    public HintDialog(Context context, String message, String title, String label) {
        super(context);
        if (message != null) {
            setMessage(message);
        }
        if (title != null) {
            setTitle(title);
        }
        setCancelable(false);
        setPositiveButton(label == null ? "确定": label, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                HintDialog.this.clickEvent();
                dialog.dismiss();
            }
        });
    }

    public HintDialog(Context context, String message, String title) {
        this(context, message, title, (String) null);
    }

    /* access modifiers changed from: protected */
    public void clickEvent() {
    }
}