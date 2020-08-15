package com.wade.mobile.app;

import android.annotation.SuppressLint;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.util.Log;
import android.widget.Toast;

import static android.net.ConnectivityManager.*;


@SuppressLint("NewApi")
public class MobileNetworkCallbackImpl extends NetworkCallback {
    @Override
    public void onAvailable(Network network) {
        super.onAvailable(network);
        System.out.println("onAvailable: 网络已连接");
    }

    @Override
    public void onLosing(Network network, int maxMsToLive) {
        super.onLosing(network, maxMsToLive);
        System.out.println("onLosing: 网络正在断开");
    }

    @Override
    public void onLost(Network network) {
        super.onLost(network);
        System.out.println("onLost: 网络已断开");
    }

    @Override
    public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
        super.onCapabilitiesChanged(network, networkCapabilities);
        System.out.println("onCapabilitiesChanged...");

    }

    @Override
    public void onLinkPropertiesChanged(Network network, LinkProperties linkProperties) {
        super.onLinkPropertiesChanged(network, linkProperties);
        System.out.println("onLinkPropertiesChanged...");
    }
}
