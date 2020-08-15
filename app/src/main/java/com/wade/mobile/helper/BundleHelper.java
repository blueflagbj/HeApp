package com.wade.mobile.helper;


import android.app.Activity;
import android.os.Bundle;

public class BundleHelper {
    private Activity activity;

    public BundleHelper(Activity paramActivity) {
        this.activity = paramActivity;
    }

    public boolean getBooleanProperty(String paramString, boolean paramBoolean) {
        Bundle bundle = this.activity.getIntent().getExtras();
        if (bundle != null) {
            Boolean bool = (Boolean)bundle.get(paramString);
            if (bool != null)
                paramBoolean = bool.booleanValue();
        }
        return paramBoolean;
    }

    public int getIntegerProperty(String paramString, int paramInt) {
        Bundle bundle = this.activity.getIntent().getExtras();
        if (bundle != null) {
            Integer integer = (Integer)bundle.get(paramString);
            if (integer != null)
                paramInt = integer.intValue();
        }
        return paramInt;
    }

    public String getStringProperty(String paramString1, String paramString2) {
        Bundle bundle = this.activity.getIntent().getExtras();
        if (bundle != null) {
            paramString1 = bundle.getString(paramString1);
            if (paramString1 != null)
                paramString2 = paramString1;
        }
        return paramString2;
    }
}