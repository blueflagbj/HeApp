package com.wade.mobile.frame;

import android.app.Activity;
import android.content.Intent;
import android.view.ViewGroup;
import com.wade.mobile.frame.plugin.Plugin;
import com.wade.mobile.frame.plugin.PluginManager;
import com.wade.mobile.ui.view.FlipperLayout;

public interface IWadeMobile {
    Activity getActivity();

    WadeWebView getCurrentWebView();

    FlipperLayout getFlipperLayout();

    boolean getKeepRunning();

    ViewGroup getMainLayout();

    PluginManager getPluginManager();

    IWadeMobileClient getWadeMobileClient();

    void startActivityForResult(Plugin plugin, Intent intent, int i);
}