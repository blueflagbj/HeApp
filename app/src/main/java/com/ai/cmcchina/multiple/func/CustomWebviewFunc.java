package com.ai.cmcchina.multiple.func;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.webkit.WebView;
import com.ai.cmcchina.crm.util.Global;

import com.wade.mobile.common.map.util.MapConstant;
import com.wade.mobile.common.scan.activity.CaptureActivity;

import org.json.JSONArray;

/* renamed from: com.ai.cmcchina.multiple.func.CustomWebviewFunc */
public class CustomWebviewFunc extends CustomWebviewCallback {
    /* access modifiers changed from: private */
    public Context context;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 10001:
                    ((Activity) CustomWebviewFunc.this.context).startActivityForResult(new Intent(CustomWebviewFunc.this.context, CaptureActivity.class), 10001);
                    return;
                default:
                    return;
            }
        }
    };
    private WebView webView;

    public CustomWebviewFunc(WebView webView2) {
        super(webView2);
        this.context = webView2.getContext();
        this.webView = webView2;
       // G3Logger.debug("CustomWebviewFunc：构造方法");
    }

    public void scanQrCode(JSONArray params) {
        this.handler.sendEmptyMessage(10001);
    }

    public void optionShare(JSONArray params) throws Exception {
        String jsonParms = params.getString(0);
        String shareType = params.getString(1);
        if (Global.SERVICE_PASSWORD.equals(shareType)) {
          //  WeChatShareUtil.wrapShareInfo(this.context, 0, jsonParms);
        } else if ("1".equals(shareType)) {
          //  WeChatShareUtil.wrapShareInfo(this.context, 1, jsonParms);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
       // G3Logger.debug("CustomWebviewFunc.onActivityResult");
        if (resultCode == -1 && requestCode == 10001) {
            callback(intent.getExtras().getString(MapConstant.CALLBACK_RESULT));
        }
    }
}