package com.wade.mobile.frame.impl;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import com.wade.mobile.app.MobileOperation;
import com.wade.mobile.common.MobileCache;
import com.wade.mobile.common.MobileThread;
import com.wade.mobile.common.db.DBHelper;
import com.wade.mobile.frame.IActivityOverload;
import com.wade.mobile.frame.IWadeMobile;
import com.wade.mobile.frame.plugin.PluginManager;
import com.wade.mobile.util.Constant;
import com.wade.mobile.util.Messages;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class ActivityOverload implements IActivityOverload {
    protected BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        public void onReceive(Context arg0, Intent arg1) {
            DBHelper.closeAll();
            MobileCache.getInstance().clear();
            ActivityOverload.this.context.finish();
            Log.d(TAG, "BroadcastReceiver-onReceive: 0");
            new MobileThread("Kill") {
                /* access modifiers changed from: protected */
                public void execute() throws Exception {
                    Thread.sleep(500);
                    MobileOperation.exitApp();
                }
            }.start();
        }
    };
    protected Activity context;
    protected IWadeMobile wademobile;

    public ActivityOverload(IWadeMobile wademobile2) {
        this.wademobile = wademobile2;
        this.context = wademobile2.getActivity();
        Log.d(TAG, "ActivityOverload: 0:"+this.context.getClass().getName());
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    public void onBackPressed() {
        WebView webView = this.wademobile.getCurrentWebView();
        if (webView == null || !webView.canGoBack()) {
            this.wademobile.getWadeMobileClient().shutdownByConfirm(Messages.CONFIRM_CLOSE);
        } else {
            webView.goBack();
        }
    }

    public void onCreate() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constant.Broadcast.EXIT_APP_ACTION);
        this.context.registerReceiver(this.broadcastReceiver, filter);
        Log.d(TAG, "ActivityOverload-onCreate:0 ");
    }

    public void onPause() {
        PluginManager pluginManager;
        if (!this.wademobile.getKeepRunning() && (pluginManager = this.wademobile.getPluginManager()) != null) {
            pluginManager.onPause();
        }
    }

    public void onResume() {
        PluginManager pluginManager = this.wademobile.getPluginManager();
        if (pluginManager != null) {
            pluginManager.onResume();
        }
    }

    public void onStop() {
        PluginManager pluginManager = this.wademobile.getPluginManager();
        if (pluginManager != null) {
            pluginManager.onStop();
        }
    }

    public void onDestroy() {
        PluginManager pluginManager = this.wademobile.getPluginManager();
        if (pluginManager != null) {
            pluginManager.onDestroy();
        }
        this.context.unregisterReceiver(this.broadcastReceiver);
    }

    public void onCreateOptionsMenu(Menu menu) {
    }

    public void onOptionsItemSelected(MenuItem item) {
    }

    public void onStart() {
    }

    public void onRestart() {
    }
}