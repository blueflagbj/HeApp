package com.wade.mobile.frame;

import android.app.Application;
import com.wade.mobile.frame.plugin.PluginManager;

public class WadeMobileApplication extends Application {
    private IWadeMobile currentWadeMobile = null;
    private PluginManager pluginManager = null;

    public void onCreate() {
        super.onCreate();
    }

    public IWadeMobile getCurrentWadeMobile() {
        return this.currentWadeMobile;
    }

    public void setCurrentWadeMobile(IWadeMobile currentWadeMobile2) {
        this.currentWadeMobile = currentWadeMobile2;
    }

    public PluginManager getPluginManager() {
        if (this.pluginManager == null) {
            this.pluginManager = new PluginManager(this);
        }
        return this.pluginManager;
    }
}