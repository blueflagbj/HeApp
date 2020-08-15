package com.wade.mobile.ui.comp.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/* renamed from: com.wade.mobile.ui.comp.dialog.ConfirmDialog */
public class ConfirmDialog extends AlertDialog.Builder {
    public ConfirmDialog(Context context, String title, String message, String okLabel, String cancelLabel, String midLabel) {
        super(context);
        if (message != null) {
            setMessage(message);
        }
        if (title != null) {
            setTitle(title);
        }
        setCancelable(false);
        setPositiveButton(okLabel == null ?"确定" : okLabel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                ConfirmDialog.this.okEvent();
                dialog.dismiss();
            }
        });
        setNegativeButton(cancelLabel == null ?"取消" : cancelLabel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                ConfirmDialog.this.cancelEvent();
                dialog.dismiss();
            }
        });
        if (midLabel != null) {
            setNeutralButton(midLabel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    ConfirmDialog.this.midEvent();
                    dialog.dismiss();
                }
            });
        }
    }

    public ConfirmDialog(Context context, String title, String message, String okLabel, String cancelLabel) {
        this(context, title, message, okLabel, cancelLabel, (String) null);
    }

    public ConfirmDialog(Context context, String title, String message) {
        this(context, title, message, (String) null, (String) null);
    }

    /* access modifiers changed from: protected */
    public void okEvent() {
    }

    /* access modifiers changed from: protected */
    public void cancelEvent() {
    }

    /* access modifiers changed from: protected */
    public void midEvent() {
    }
}