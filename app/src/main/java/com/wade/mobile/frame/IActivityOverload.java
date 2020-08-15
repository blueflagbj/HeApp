package com.wade.mobile.frame;


import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

public interface IActivityOverload {
    void onBackPressed();

    void onCreate();

    void onCreateOptionsMenu(Menu menu);

    void onDestroy();

    boolean onKeyDown(int i, KeyEvent keyEvent);

    void onOptionsItemSelected(MenuItem menuItem);

    void onPause();

    void onRestart();

    void onResume();

    void onStart();

    void onStop();
}