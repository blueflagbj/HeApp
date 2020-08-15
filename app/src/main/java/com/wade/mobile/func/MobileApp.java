package com.wade.mobile.func;

import android.content.Intent;
import com.wade.mobile.common.MobileLog;
import com.wade.mobile.frame.IWadeMobile;
import com.wade.mobile.frame.plugin.Plugin;
import com.wade.mobile.util.Constant;
import com.wade.mobile.util.Messages;
import org.json.JSONArray;
import org.json.JSONException;

public class MobileApp extends Plugin {
    private Intent callbackIntent;

    public MobileApp(IWadeMobile wademobile) {
        super(wademobile);
    }

    public void close(JSONArray param) throws Exception {
        boolean showConfirm = false;
        if (param.length() == 1) {
            if (isNull(param.getString(0)) || !Constant.TRUE.equals(param.getString(0))) {
                showConfirm = false;
            } else {
                showConfirm = true;
            }
        }
        close(Boolean.valueOf(showConfirm));
    }

    public void close(Boolean showConfirm) {
        if (!(this.context instanceof IWadeMobile)) {
            return;
        }
        if (showConfirm.booleanValue()) {
            this.wademobile.getWadeMobileClient().shutdownByConfirm(Messages.CONFIRM_CLOSE);
        } else {
            this.wademobile.getWadeMobileClient().exitApp();
        }
    }

    public void setResult(JSONArray param) throws Exception {
        setResult(param.getString(0), param.getString(1));
    }

    public void setResult(String key, String value) {
        if (this.callbackIntent == null) {
            this.callbackIntent = new Intent();
        }
        this.callbackIntent.putExtra(key, value);
    }

    public void logCat(JSONArray param) throws JSONException {
        String msg = param.getString(0);
        String title = param.getString(1);
        if (title == null) {
            title = "LogCat";
        }
        MobileLog.i(title, msg);
    }
}