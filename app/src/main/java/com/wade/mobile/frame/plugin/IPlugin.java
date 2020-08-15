package com.wade.mobile.frame.plugin;


import android.content.Intent;

public interface IPlugin {
    void callback(String str) throws Exception;

    void onActivityResult(int i, int i2, Intent intent);

    void onDestroy();

    void onPause();

    void onResume();

    void onStop();
}