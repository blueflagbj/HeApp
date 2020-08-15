package com.wade.mobile.common.contacts.helper;

import android.content.Context;

public class DensityHelper {
    private Context context;

    public DensityHelper(Context context2) {
        this.context = context2;
    }

    public Context getContext() {
        return this.context;
    }

    public void setContext(Context context2) {
        this.context = context2;
    }

    public int px2dip(float pxValue) {
        return (int) ((pxValue / this.context.getResources().getDisplayMetrics().density) + 0.5f);
    }

    public int dip2px(float dipValue) {
        return (int) ((dipValue * this.context.getResources().getDisplayMetrics().density) + 0.5f);
    }

    public int px2sp(float pxValue) {
        return (int) ((pxValue / this.context.getResources().getDisplayMetrics().scaledDensity) + 0.5f);
    }

    public int sp2px(float spValue) {
        return (int) ((spValue * this.context.getResources().getDisplayMetrics().scaledDensity) + 0.5f);
    }
}