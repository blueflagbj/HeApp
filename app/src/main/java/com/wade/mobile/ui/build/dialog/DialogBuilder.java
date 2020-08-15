package com.wade.mobile.ui.build.dialog;


import android.content.Context;

/* renamed from: com.wade.mobile.ui.build.dialog.DialogBuilder */
public abstract class DialogBuilder<Type> {
    protected Context context;

    public abstract Type build();

    public DialogBuilder(Context context2) {
        this.context = context2;
    }

    public Context getContext() {
        return this.context;
    }
}