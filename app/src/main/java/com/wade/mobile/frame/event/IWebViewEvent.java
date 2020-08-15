package com.wade.mobile.frame.event;

import android.webkit.WebView;

public interface IWebViewEvent {
    void loadingError(WebView webView, int i, String str, String str2);

    void loadingFinished(WebView webView, String str);

    void loadingStart(WebView webView, String str);

    void onLoadResource(WebView webView, String str);
}