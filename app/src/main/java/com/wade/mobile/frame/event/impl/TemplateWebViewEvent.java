package com.wade.mobile.frame.event.impl;

import android.webkit.WebView;
import com.wade.mobile.frame.IWadeMobile;
import com.wade.mobile.frame.event.IWebViewEvent;

public class TemplateWebViewEvent implements IWebViewEvent {
    protected IWadeMobile wademobile;

    public TemplateWebViewEvent(IWadeMobile wademobile2) {
        this.wademobile = wademobile2;
    }

    public void loadingStart(WebView view, String url) {
    }

    public void loadingFinished(WebView view, String url) {
    }

    public void loadingError(WebView webView, int errorCode, String description, String failingUrl) {
    }

    public void onLoadResource(WebView view, String url) {
    }
}