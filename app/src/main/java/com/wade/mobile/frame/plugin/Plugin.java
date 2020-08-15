package com.wade.mobile.frame.plugin;

import android.app.Activity;
import android.content.Intent;
import com.tencent.tinker.android.p131dx.instruction.Opcodes;
import com.wade.mobile.frame.IWadeMobile;
import com.wade.mobile.frame.WadeWebView;

public abstract class Plugin implements IPlugin {
    public static final String NULL = "null";
    /* access modifiers changed from: protected */
    public final String TAG = getClass().getSimpleName();
    protected String callback;
    /* access modifiers changed from: protected */
    public Activity context;
    protected IWadeMobile wademobile;

    public Plugin(IWadeMobile wademobile2) {
        this.wademobile = wademobile2;
        this.context = wademobile2.getActivity();
    }

    public void setWademobile(IWadeMobile wademobile2) {
        this.wademobile = wademobile2;
    }

    public void setContext(Activity context2) {
        this.context = context2;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
    }

    public void callback(String result, boolean isEscape) {
        String code;
        String result2 = encodeForJs(result);
        System.out.println("Plugin-callback:0");
        int index = this.callback.indexOf("_WadeMobileSet_Key_");
        if (index == -1) {
            code = "WadeMobile.callback.execCallback('" + this.callback + "', '" + result2 + "');";
        } else {
            code = "top.WadeMobileSet." + this.callback.substring(index) + ".callback.execCallback('" + this.callback.substring(0, index) + "', '" + result2 + "');";
        }
        System.out.println("Plugin-callback:6"+code);
        executeJs(code);
    }

    public static String encodeForJs(String data) {
        if (data == null || "".equals(data)) {
            return "";
        }
        int length = data.length();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            char c = data.charAt(i);
            switch (c) {
                case 8:
                    sb.append("\\b");
                    break;
                case 9:
                    sb.append("\\t");
                    break;
                case 10:
                    sb.append("\\n");
                    break;
                case 12:
                    sb.append("\\f");
                    break;
                case 13:
                    sb.append("\\r");
                    break;
                case '\'':
                    sb.append("\\'");
                    break;
                case Opcodes.IPUT_BOOLEAN:
                    sb.append("\\\\");
                    break;
                default:
                    sb.append(c);
                    break;
            }
        }
        return sb.toString();
    }

    public void callback(String result) {
        callback(result, false);
    }

    public void error(String message) {
        String code;
        String message2 = encodeForJs(message);
        int index = this.callback.indexOf("_WadeMobileSet_Key_");
        if (index == -1) {
            StringBuilder append = new StringBuilder().append("WadeMobile.callback.error('").append(this.callback).append("', '");
            if (message2 == null) {
                message2 = "";
            }
            code = append.append(message2).append("');").toString();
        } else {
            StringBuilder append2 = new StringBuilder().append("top.WadeMobileSet.").append(this.callback.substring(index)).append(".callback.error('").append(this.callback).append("', '");
            if (message2 == null) {
                message2 = "";
            }
            code = append2.append(message2).append("');").toString();
        }
        executeJs(code);
    }

    public void success(String message) {
        String message2 = encodeForJs(message);
        StringBuilder append = new StringBuilder().append("WadeMobile.callback.success('").append(this.callback).append("', '");
        if (message2 == null) {
            message2 = "";
        }
        executeJs(append.append(message2).append("');").toString());
    }

    public void onPause() {
    }

    public void onResume() {
    }

    public void onDestroy() {
    }

    public void onStop() {
    }

    public void setCallback(String callback2) {
        this.callback = callback2;
    }

    public WadeWebView getWebView() {
        return this.wademobile.getCurrentWebView();
    }

    public void executeJs(String code) {
        if (getWebView() != null) {
            getWebView().executeJs(code);
        }
    }

    public void startActivityForResult(Intent intent, int requestCode) {
        this.wademobile.startActivityForResult(this, intent, requestCode);
    }

    /* access modifiers changed from: protected */
    public boolean isNull(String value) {
        return value == null || "null".equals(value) || "".equals(value);
    }
}