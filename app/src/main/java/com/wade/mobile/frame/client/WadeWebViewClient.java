package com.wade.mobile.frame.client;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.wade.mobile.common.MobileLog;
import com.wade.mobile.frame.IWadeMobile;
import com.wade.mobile.frame.event.IWebViewEvent;
import com.wade.mobile.util.Constant;

public class WadeWebViewClient extends WebViewClient {
    protected final String TAG = getClass().getSimpleName();
    private Activity context;
    private IWebViewEvent event;

    public WadeWebViewClient(IWadeMobile wademobile, IWebViewEvent event2) {
        this.context = wademobile.getActivity();
        this.event = event2;
    }

    public void onPageStarted(WebView webView, String url, Bitmap favicon) {
        if (!webView.getSettings().getLoadsImagesAutomatically()) {
            webView.getSettings().setLoadsImagesAutomatically(true);
        }
        super.onPageStarted(webView, url, favicon);
        this.event.loadingStart(webView, url);
    }

    public void onPageFinished(WebView webView, String url) {
        super.onPageFinished(webView, url);
        this.event.loadingFinished(webView, url);
    }

    public void onReceivedError(WebView webView, int errorCode, String description, String failingUrl) {
        super.onReceivedError(webView, errorCode, description, failingUrl);
        this.event.loadingError(webView, errorCode, description, failingUrl);
    }

    public void onLoadResource(WebView view, String url) {
        super.onLoadResource(view, url);
        this.event.onLoadResource(view, url);
    }

    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if (url.startsWith("tel:")) {
            try {
                Intent intent = new Intent("android.intent.action.DIAL");
                intent.setData(Uri.parse(url));
                this.context.startActivity(intent);
                return true;
            } catch (ActivityNotFoundException e) {
                MobileLog.d(this.TAG, "拨号错误: " + url + ": " + e.toString());
                return true;
            }
        } else if (url.startsWith("geo:0,0?q=".substring(1, 4))) {
            try {
                Intent intent2 = new Intent("android.intent.action.VIEW");
                intent2.setData(Uri.parse(url));
                this.context.startActivity(intent2);
                return true;
            } catch (ActivityNotFoundException e2) {
                MobileLog.d(this.TAG, "显示地图错误: " + url + ": " + e2.toString());
                return true;
            }
        } else if (url.startsWith("mailto:")) {
            try {
                Intent intent3 = new Intent("android.intent.action.VIEW");
                intent3.setData(Uri.parse(url));
                this.context.startActivity(intent3);
                return true;
            } catch (ActivityNotFoundException e3) {
                MobileLog.d(this.TAG, "发送邮件错误" + url + ": " + e3.toString());
                return true;
            }
        } else if (url.startsWith("sms:")) {
            try {
                Intent intent4 = new Intent("android.intent.action.VIEW");
                intent4.setData(Uri.parse(url));
                intent4.putExtra("address", url.substring(4));
                intent4.setType("vnd.android-dir/mms-sms");
                this.context.startActivity(intent4);
                return true;
            } catch (ActivityNotFoundException e4) {
                MobileLog.d(this.TAG, "发送短信错误" + url + ":" + e4.toString());
                return true;
            }
        } else if (!url.startsWith(Constant.START_HTTP) && !url.startsWith(Constant.START_HTTPS) && !url.startsWith(Constant.START_FILE)) {
            return false;
        } else {
            view.loadUrl(url);
            return true;
        }
    }
}