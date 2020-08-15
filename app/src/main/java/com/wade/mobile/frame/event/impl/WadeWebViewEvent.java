package com.wade.mobile.frame.event.impl;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import com.wade.mobile.common.MobileThreadManage;
import com.wade.mobile.frame.IWadeMobile;
import com.wade.mobile.frame.WadeWebView;
import com.wade.mobile.frame.config.MobileConfig;
import com.wade.mobile.frame.event.IWebViewEvent;
import com.wade.mobile.ui.util.UiTool;
import com.wade.mobile.util.Constant;
import com.wade.mobile.util.Messages;
import com.wade.mobile.util.MobileGraphics;

public class WadeWebViewEvent implements IWebViewEvent {
    private Activity context;
    protected Handler handler;
    protected boolean isLoadingDialog = MobileConfig.getInstance().isLoadingDialog();
    protected boolean isLoadingImage;
    protected ProgressDialog progressDialog;
    protected AlertDialog.Builder retryDialog;
    /* access modifiers changed from: private */
    public IWadeMobile wademobile;

    public WadeWebViewEvent(IWadeMobile wademobile2) {
        this.isLoadingImage = MobileConfig.getInstance().getLoadingBgImage() != null && !MobileConfig.getInstance().getLoadingBgImage().trim().equals("");
        this.wademobile = wademobile2;
        this.context = wademobile2.getActivity();
        initHandler();
    }

    public void loadingStart(WebView webView, String url) {
        if (this.isLoadingDialog) {
            sendHandler(0, Messages.DIALOG_LOADING);
        } else if (this.isLoadingImage) {
            sendHandler(3);
        }
    }

    public void loadingFinished(WebView webView, String url) {
        if (webView instanceof WadeWebView) {
            ((WadeWebView)webView).loadOver();
        }
        Thread thread = MobileThreadManage.getInstance().getThread(url);
        if (thread != null) {
            thread.interrupt();
        }
        if (this.isLoadingDialog) {
            sendHandler(1);
        } else if (this.isLoadingImage) {
            sendHandler(4);
        }
    }

    public void loadingError(WebView webView, int errorCode, String description, String failingUrl) {
        if (webView instanceof WadeWebView) {
            ((WadeWebView) webView).loadOver();
        }
        if (this.isLoadingDialog) {
            sendHandler(1);
        }
        if (MobileConfig.getInstance().isOvertimeRetry()) {
            sendHandler(2, failingUrl);
        }
    }

    /* access modifiers changed from: protected */
    @SuppressLint("HandlerLeak")
    private void initHandler() {
        if (this.handler == null) {
            this.handler = new Handler() {
                public void handleMessage(Message msg) {
                    if (!Thread.currentThread().isInterrupted()) {
                        switch (msg.what) {
                            case 0:
                                WadeWebViewEvent.this.showProgressDialog(msg.obj == null ? "" : msg.obj.toString());
                                break;
                            case 1:
                                WadeWebViewEvent.this.hideProgressDialog();
                                break;
                            case 2:
                                WadeWebViewEvent.this.showRetryDialog(msg.obj == null ? "" : msg.obj.toString());
                                break;
                            case 3:
                                WadeWebViewEvent.this.startLoadingImage();
                                break;
                            case 4:
                                WadeWebViewEvent.this.overLoadingImage();
                                break;
                        }
                    }
                    super.handleMessage(msg);
                }
            };
        }
    }

    /* access modifiers changed from: protected */
    private void sendHandler(int what) {
        this.handler.sendEmptyMessage(what);
    }

    /* access modifiers changed from: protected */
    private void sendHandler(int what, Object obj) {
        this.handler.sendMessage(Message.obtain(this.handler, what, obj));
    }

    /* access modifiers changed from: protected */
    private void initLoadingDialog() {
        if (this.progressDialog == null) {
            this.progressDialog = new ProgressDialog(this.context);
            this.progressDialog.setProgressStyle(0);
            this.progressDialog.getWindow().setGravity(17);
            this.progressDialog.setCancelable(false);
            this.progressDialog.setCanceledOnTouchOutside(false);
            if (MobileConfig.getInstance().isLoadingDialog()) {
                this.progressDialog.setIcon(this.context.getResources().getDrawable(UiTool.getR(this.context, "drawable", Constant.WadeMobileActivity.ICON)));
                this.progressDialog.setTitle(" ");
                this.progressDialog.setMessage(Messages.DIALOG_LOADING);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void showProgressDialog(String msg) {
        initLoadingDialog();
        if (!this.progressDialog.isShowing()) {
            this.progressDialog.setMessage(msg);
            this.progressDialog.show();
        }
    }

    /* access modifiers changed from: protected */
    public void hideProgressDialog() {
        if (this.progressDialog != null) {
            this.progressDialog.hide();
        }
    }

    /* access modifiers changed from: protected */
    public void showRetryDialog(final String url) {
        if (this.retryDialog == null) {
            this.retryDialog = this.wademobile.getWadeMobileClient().createConfirm((String) null, (String) null, (String) null, (Object[]) null, Constant.Function.close, new Object[]{false});
        }
        this.retryDialog.setMessage(Messages.NETWORK_UNSTABLE);
        this.retryDialog.setPositiveButton(Messages.TRY_AGAIN, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                WadeWebViewEvent.this.wademobile.getWadeMobileClient().execute(Constant.Function.openUrl, new Object[]{url});
            }
        });
        this.retryDialog.show();
    }

    public void startLoadingImage() {
        String imageName = MobileConfig.getInstance().getLoadingBgImage();
        ViewGroup mainLayout = this.wademobile.getMainLayout();
        mainLayout.setBackgroundResource(UiTool.getR(this.context, "drawable", imageName));
        for (int i = 0; i < mainLayout.getChildCount(); i++) {
            mainLayout.getChildAt(i).setVisibility(View.GONE);
        }
    }

    public void overLoadingImage() {
        ViewGroup mainLayout = this.wademobile.getMainLayout();
        for (int i = 0; i < mainLayout.getChildCount(); i++) {
            mainLayout.getChildAt(i).setVisibility(View.VISIBLE);
        }
    }

    /* access modifiers changed from: protected */
    public void setCurWebViewBg(View view) {
        Bitmap viewShot = MobileGraphics.screenSnapshot(view);
        WebView webView =this.wademobile.getCurrentWebView();
        if (viewShot != null) {
            webView.setBackgroundDrawable(new BitmapDrawable(viewShot));
            webView.setBackgroundColor(Color.argb(0, 0, 0, 0));
            return;
        }
        webView.setBackgroundDrawable((Drawable) null);
        webView.setBackgroundColor(-1);
    }

    public void onLoadResource(WebView view, String url) {
    }
}