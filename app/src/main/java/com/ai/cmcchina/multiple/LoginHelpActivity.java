package com.ai.cmcchina.multiple;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.ai.cmcchina.crm.util.DeviceUtil;
import com.heclient.heapp.R;

/* renamed from: com.ai.cmcchina.multiple.LoginHelpActivity */
public class LoginHelpActivity extends Activity {
    private WebView webView;

    /* access modifiers changed from: protected */
    @TargetApi(16)
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_helper_layout);
        this.webView = (WebView) findViewById(R.id.webview);
        this.webView.requestFocusFromTouch();
        this.webView.setHorizontalFadingEdgeEnabled(false);
        this.webView.setHorizontalScrollBarEnabled(false);
        this.webView.setVerticalScrollBarEnabled(true);
        this.webView.setScrollBarStyle(0);
        this.webView.setWebChromeClient(new WebChromeClient() {
        });
        this.webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        WebSettings settings = this.webView.getSettings();
        if (DeviceUtil.getAndroidSDKVersion() >= 16) {
            settings.setAllowUniversalAccessFromFileURLs(true);
        }
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        settings.setDefaultTextEncodingName("utf-8");
        settings.setAppCacheEnabled(false);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setSavePassword(false);
        this.webView.loadUrl("file:///android_asset/help_know/login_know.html");
    }

    public void back(View view) {
        onBackPressed();
    }
}