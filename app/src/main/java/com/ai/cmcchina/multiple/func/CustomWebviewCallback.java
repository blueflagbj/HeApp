package com.ai.cmcchina.multiple.func;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import com.ai.cmcchina.crm.util.UIUtil;

import com.tencent.tinker.android.p131dx.instruction.Opcodes;
import com.wade.mobile.util.Utility;
import org.json.JSONArray;

/* renamed from: com.ai.cmcchina.multiple.func.CustomWebviewCallback */
public abstract class CustomWebviewCallback {
    protected final int SCAN_QR_CODE = 10001;
    private String action;
    private String callback;
    /* access modifiers changed from: private */
    public Activity context;
    private final Handler handler;
    HandlerThread thread = new HandlerThread("CustomWebviewCallback-HandlerThread");
    /* access modifiers changed from: private */
    public WebView webView;

    public CustomWebviewCallback(WebView webView2) {
//        G3Logger.debug("CustomWebviewCallback:构造方法");
        this.thread.start();
        this.handler = new Handler(this.thread.getLooper());
        this.context = (Activity) webView2.getContext();
        this.webView = webView2;
    }

    @JavascriptInterface
    public void exec(final String action2, final String callback2, final String params) {
        this.handler.post(new Runnable() {
            public void run() {
                try {
                    System.out.println("JavascriptInterface:exec：" + Thread.currentThread());
                    System.out.println("JavascriptInterface:action: " + action2 + "  callback: " + callback2);
                    System.out.println("JavascriptInterface:params = " + params);

                    CustomWebviewCallback.this.execute(action2, callback2, new JSONArray(params));
                } catch (Exception e) {
                    final String error = Utility.getBottomMessage(e);
                    CustomWebviewCallback.this.context.runOnUiThread(new Runnable() {
                        public void run() {
                            CustomWebviewCallback.this.error(callback2, error);
                        }
                    });
                }
            }
        });
    }

    public void execute(String action2, String callback2, JSONArray params) throws Exception {
        System.out.println("CustomWebviewCallback：execute方法");
        this.callback = callback2;
        this.action = action2;
        try {
            getClass().getMethod(action2, new Class[]{JSONArray.class}).invoke(this, new Object[]{params});
        } catch (NoSuchMethodException e) {
            throw new Exception("该插件方法未定义【" + action2 + "】");
        }
    }

    public void callback(String result) {
        String code;
        try {
            System.out.println("callback:" + Thread.currentThread());
            System.out.println("callback:result = " + result);

            String result2 = encodeForJs(result);
            int index = this.callback.indexOf("_WadeMobileSet_Key_");
            if (index == -1) {
                code = "WadeMobile.callback.execCallback('" + this.callback + "', '" + result2 + "');";
            } else {
                code = "top.WadeMobileSet." + this.callback.substring(index) + ".callback.execCallback('" + this.callback.substring(0, index) + "', '" + result2 + "');";
            }
            System.out.println("executeJs(code) =  " + code);
            executeJs(code);
        } catch (Exception e) {
            e.printStackTrace();
           // G3Logger.debug(e);
            UIUtil.toast((Context) this.context, (Object) "调用方法【" + this.action + "】出错！");
        }
    }

    public void error(String callback2, String message) {
        System.out.println("callback : " + callback2 + " error:" + message);
        String code = "WadeMobile.callback.error('" + callback2 + "', '" + message + "');";
        if (this.context != null && this.webView != null) {
            executeJs(code);
        }
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

    public void executeJs(final String code) {
        if (this.context != null && this.webView != null) {
            this.context.runOnUiThread(new Runnable() {
                public void run() {
                    CustomWebviewCallback.this.webView.loadUrl("javascript:" + code + ";");
                }
            });
        }
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
                case Opcodes.IPUT_BOOLEAN /*92*/:
                    sb.append("\\\\");
                    break;
                default:
                    sb.append(c);
                    break;
            }
        }
        return sb.toString();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        System.out.println("CustomWebviewCallback.onActivityResult");
      //  G3Logger.debug("CustomWebviewCallback.onActivityResult");
    }
}