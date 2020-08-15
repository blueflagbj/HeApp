package com.wade.mobile.frame.impl;


import android.app.Activity;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import com.wade.mobile.app.MobileAppInfo;
import com.wade.mobile.frame.IWadeMobile;
import com.wade.mobile.frame.IWebviewSetting;
import com.wade.mobile.frame.SafeWebView;
import com.wade.mobile.frame.client.EclairClient;
import com.wade.mobile.frame.client.MobileClient;
import com.wade.mobile.frame.config.MobileConfig;
import java.lang.reflect.Field;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class WebviewSetting implements IWebviewSetting {
    protected Activity context;
    protected IWadeMobile wademobile;

    public WebviewSetting(IWadeMobile wademobile2) {
        this.wademobile = wademobile2;
        this.context = wademobile2.getActivity();
        Log.d(TAG, "WebviewSetting: 0");
    }

    public void setWebViewStyle(WebView webView) {
        if (webView != null) {
            bindBrowser(webView);
            setWebViewClient(webView);
            setWebViewCommon(webView.getSettings());
            if (webView instanceof SafeWebView) {
                ((SafeWebView) webView).removeSearchBoxJavaBridge();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void bindBrowser(WebView webView) {
        if (Build.VERSION.SDK_INT < 18) {
            webView.getSettings().setSavePassword(false);
        }
        webView.addJavascriptInterface(this.wademobile.getPluginManager(), "PluginManager");

    }

    /* access modifiers changed from: protected */
    public void setWebViewClient(WebView webView) {
        if (Build.VERSION.RELEASE.startsWith("2.")) {
            webView.setWebChromeClient(new EclairClient(this.context));
        } else {
            webView.setWebChromeClient(new MobileClient(this.context));
        }
        if (MobileAppInfo.getOsVersionNumber() >= 11) {
        }
        if (MobileAppInfo.getOsVersionNumber() >= 19) {
            webView.getSettings().setLoadsImagesAutomatically(true);
        } else {
            webView.getSettings().setLoadsImagesAutomatically(false);
        }
        webView.setBackgroundColor(0);
        webView.setInitialScale(100);
        webView.setVerticalScrollBarEnabled(false);
        webView.setHorizontalScrollBarEnabled(false);
        webView.requestFocusFromTouch();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webView.setWebContentsDebuggingEnabled(true);
        }

    }

    /* access modifiers changed from: protected */
    public void setWebViewCommon(WebSettings settings) {
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        setStorage(settings, true, this.context.getApplicationContext().getDir("database", 0).getPath());
        setCacheMode(settings);
        settings.setNeedInitialFocus(true);
        settings.setSavePassword(false);
        settings.setSaveFormData(false);
        settings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        settings.setGeolocationEnabled(false);
        settings.setDomStorageEnabled(true);//原为false;
        settings.setUseWideViewPort(true);

    }

    /* access modifiers changed from: protected */
    public void setStorage(WebSettings setting, boolean enable, String path) {
        setting.setDatabaseEnabled(enable);
        setting.setDatabasePath(path);
    }

    /* access modifiers changed from: protected */
    public void setCacheMode(WebSettings setting) {
        setting.setCacheMode(MobileConfig.getInstance().getCacheMode());
    }

    /* access modifiers changed from: protected */
    public void setZoom(WebView webView) {
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
    }

    public void setOnFocusScale(final WebView webView) {
        webView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                try {
                    Field defaultScale = WebView.class.getDeclaredField("mDefaultScale");
                    defaultScale.setAccessible(true);
                    defaultScale.setFloat(webView, 1.0f);
                } catch (SecurityException e) {
                    e.printStackTrace();
                } catch (IllegalArgumentException e2) {
                    e2.printStackTrace();
                } catch (IllegalAccessException e3) {
                    e3.printStackTrace();
                } catch (NoSuchFieldException e4) {
                    e4.printStackTrace();
                }
            }
        });
    }
}