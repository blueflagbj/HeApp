package com.wade.mobile.ui.build.dialog.progressdialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import com.wade.mobile.ui.build.dialog.DialogBuilder;

/* renamed from: com.wade.mobile.ui.build.dialog.progressdialog.ProgressDialogBuilder */
public abstract class ProgressDialogBuilder<Type> extends DialogBuilder<ProgressDialog> {
    protected final int STYLE_HORIZONTAL = 1;
    protected final int STYLE_SPINNER = 0;
    protected boolean cancelable = true;
    protected boolean canceledOnTouchOutside = false;
    protected Drawable icon;
    protected String message;
    protected int progressDialogStyle = 0;
    protected String title;

    public abstract ProgressDialog getProgressDialog();

    public ProgressDialogBuilder(Context context) {
        super(context);
    }

    public Type setProgressStyle(int progressDialogStyle2) {
        this.progressDialogStyle = progressDialogStyle2;
        return (Type) this;
    }

    public Type setCancelable(boolean cancelable2) {
        this.cancelable = cancelable2;
        return (Type) this;
    }

    public Type setCanceledOnTouchOutside(boolean canceledOnTouchOutside2) {
        this.canceledOnTouchOutside = canceledOnTouchOutside2;
        return (Type) this;
    }

    public Type setIcon(Drawable icon2) {
        this.icon = icon2;
        return (Type) this;
    }

    public Type setTitle(String title2) {
        this.title = title2;
        return (Type) this;
    }

    public Type setMessage(String message2) {
        this.message = message2;
        return (Type) this;
    }

    public ProgressDialog build() {
        ProgressDialog progressDialog = getProgressDialog();
        progressDialog.setProgressStyle(this.progressDialogStyle);
        progressDialog.getWindow().setGravity(17);
        progressDialog.setCancelable(this.cancelable);
        progressDialog.setCanceledOnTouchOutside(this.canceledOnTouchOutside);
        if (this.icon != null) {
            progressDialog.setIcon(this.icon);
        }
        if (this.title != null) {
            progressDialog.setTitle(this.title);
        }
        if (this.message != null) {
            progressDialog.setMessage(this.message);
        }
        return progressDialog;
    }
}