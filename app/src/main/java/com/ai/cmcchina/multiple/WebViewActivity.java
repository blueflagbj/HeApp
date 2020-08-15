package com.ai.cmcchina.multiple;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ai.cmcchina.crm.util.UIUtil;
import com.ai.cmcchina.multiple.func.CustomWebviewFunc;

import com.heclient.heapp.App;
import com.heclient.heapp.R;
import com.wade.mobile.util.Constant;


/* renamed from: com.ai.cmcchina.multiple.WebViewActivity */
public class WebViewActivity extends Activity {
    private Button closeBtn;
    private CustomWebviewFunc function;
    private String openUrl = "";
    /* access modifiers changed from: private */
    public ProgressBar progessBar;
    private String title = "";
    /* access modifiers changed from: private */
    public WebView webView;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        initData();
        initView();
        setlistener();
       // ((App) getApplication()).addWatermark(this);
    }

    private void initData() {
        this.openUrl = getIntent().getStringExtra(Constant.MobileWebCacheDB.URL_COLUMN);
        this.title = getIntent().getStringExtra("title");
       // G3Logger.debug("WebViewActivity:需要打开的页面为" + this.openUrl);
        if (TextUtils.isEmpty(this.openUrl)) {
            UIUtil.alert((Context) this, "未获取到需要打开的地址URL，请检查");
        }
    }

    private void setlistener() {
        findViewById(R.id.back_btn).setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                if (WebViewActivity.this.webView.canGoBack()) {
                    WebViewActivity.this.webView.goBack();
                } else {
                    WebViewActivity.this.finish();
                }
            }
        });
        this.closeBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramView) {
                try {
                    if (WebViewActivity.this.webView != null) {
                        WebViewActivity.this.webView.loadUrl("about:blank");
                        WebView unused = WebViewActivity.this.webView = null;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                   // G3Logger.debug(e);
                }
                WebViewActivity.this.finish();
            }
        });
    }

    @SuppressLint({"SetJavaScriptEnabled"})
    private void initView() {
        if (TextUtils.isEmpty(this.title)) {
            this.title = "页面";
        }
        ((TextView) findViewById(R.id.titleTxt)).setText(this.title);
        this.closeBtn = (Button) findViewById(R.id.search_btn);
        this.closeBtn.setBackgroundDrawable((Drawable) null);
        this.closeBtn.setText("关闭");
        this.closeBtn.setVisibility(View.VISIBLE);
        this.webView = (WebView) findViewById(R.id.web_view);
        this.progessBar = (ProgressBar) findViewById(R.id.progress_bar);
        this.webView.requestFocusFromTouch();
        this.webView.setHorizontalFadingEdgeEnabled(false);
        this.webView.setHorizontalScrollBarEnabled(false);
        this.webView.setVerticalScrollBarEnabled(true);
        this.webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        this.webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress < 100) {
                    WebViewActivity.this.progessBar.setVisibility(View.VISIBLE);
                    WebViewActivity.this.progessBar.setProgress(newProgress);
                    return;
                }
                WebViewActivity.this.progessBar.setVisibility(View.GONE);
            }
        });
        this.webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url == null || (!url.contains("alipays") && !url.contains("payOrderId"))) {
                    view.loadUrl(url);
                    return false;
                }
                UIUtil.alert((Context) WebViewActivity.this, "支付需要在浏览器中操作");
                return true;
            }
        });
        WebSettings settings = this.webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        settings.setDefaultTextEncodingName("utf-8");
        settings.setAppCacheEnabled(false);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setDomStorageEnabled(true);
        settings.setSavePassword(false);
        if (this.function == null) {
            this.function = new CustomWebviewFunc(this.webView);
        }
        this.webView.addJavascriptInterface(this.function, "AIClient");
        this.webView.loadUrl(this.openUrl);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != 4 || !this.webView.canGoBack()) {
            return super.onKeyDown(keyCode, event);
        }
        this.webView.goBack();
        return true;
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (this.function != null) {
            this.function.onActivityResult(requestCode, resultCode, data);
        }
    }
}