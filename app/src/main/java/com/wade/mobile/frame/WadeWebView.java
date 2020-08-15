package com.wade.mobile.frame;

import android.app.Activity;
import android.util.Log;

import com.wade.mobile.common.MobileLog;
import com.wade.mobile.common.MobileThread;
import com.wade.mobile.frame.client.WadeWebViewClient;
import com.wade.mobile.frame.config.MobileConfig;
import com.wade.mobile.frame.event.impl.WadeWebViewEvent;
import com.wade.mobile.util.Messages;

public class WadeWebView extends SafeWebView {
    protected final String TAG = getClass().getSimpleName();
    protected Activity context;
    /* access modifiers changed from: private */
    public final int loadUrlTimeout;
    /* access modifiers changed from: private */
    public int timeoutCounter = 0;
    protected IWadeMobile wademobile;
    /* access modifiers changed from: private */
    public WadeWebViewClient webViewClient;

    public WadeWebView(IWadeMobile wademobile2) {
        super(wademobile2.getActivity());
        int timeout = MobileConfig.getInstance().getLoadurlTimeout();
        this.loadUrlTimeout = timeout < 20000 ? 20000 : timeout;
        getSettings().setUserAgentString(getSettings().getUserAgentString() + " IpuMobile/i1/android/00/2.0/Hybrid");

        this.wademobile = wademobile2;
        this.context = wademobile2.getActivity();
        initialize();
    }

    /* access modifiers changed from: protected */
    public void initialize() {
        this.webViewClient = new WadeWebViewClient(this.wademobile, new WadeWebViewEvent(this.wademobile));
        setWebViewClient(this.webViewClient);
        Log.d(TAG, " WadeWebView-initialize:0 ");
    }

    public void executeJs(final String code) {
        this.context.runOnUiThread(new Runnable() {
            public void run() {
                WadeWebView.super.loadUrl("javascript:" + code + ";");
            }
        });
    }

    public void loadRemoteUrl(final String url) {
        this.context.runOnUiThread(new Runnable() {
            public void run() {
                final int currTimeout = WadeWebView.this.timeoutCounter;
                new MobileThread(url) {
                    /* access modifiers changed from: protected */
                    public void execute() throws Exception {
                        try {
                            synchronized (this) {
                                wait((long) WadeWebView.this.loadUrlTimeout);
                            }
                        } catch (InterruptedException e) {
                            MobileLog.d(WadeWebView.this.TAG, this + " of timeout is interrupted");
                        }
                        if (WadeWebView.this.timeoutCounter == currTimeout) {
                            MobileLog.d(WadeWebView.this.TAG, url + " page is error");
                            WadeWebView.this.context.runOnUiThread(new Runnable() {
                                public void run() {
                                    WadeWebView.this.stopLoading();
                                }
                            });
                            WadeWebView.this.webViewClient.onReceivedError(WadeWebView.this, -6, Messages.CONN_SERVER_FAILED, url);
                        }
                    }
                }.start();
                WadeWebView.super.loadUrl(url);
            }
        });
    }

    public void loadOver() {
        this.timeoutCounter++;
    }
}