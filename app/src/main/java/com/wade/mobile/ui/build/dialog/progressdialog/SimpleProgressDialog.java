package com.wade.mobile.ui.build.dialog.progressdialog;

import android.app.ProgressDialog;
import android.content.Context;

/* renamed from: com.wade.mobile.ui.build.dialog.progressdialog.SimpleProgressDialog */
public class SimpleProgressDialog extends ProgressDialogBuilder<SimpleProgressDialog> {
    protected ProgressDialog progressDialog;

    public SimpleProgressDialog(Context context) {
        super(context);
        this.progressDialog = new ProgressDialog(context);
    }

    public ProgressDialog getProgressDialog() {
        return this.progressDialog;
    }
}