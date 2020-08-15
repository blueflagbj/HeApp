package com.wade.mobile.frame;

import android.app.AlertDialog;
import android.view.Window;

public interface IWadeMobileClient {
    void alert(String str, String str2, Object[] objArr);

    void confirm(String str, String str2, String str3, Object[] objArr, String str4, Object[] objArr2);

    AlertDialog.Builder createAlert(String str, String str2, String str3, Object[] objArr);

    AlertDialog.Builder createConfirm(String str, String str2, String str3, Object[] objArr, String str4, Object[] objArr2);

    void execute(String str);

    void execute(String str, Object[] objArr);

    void exitApp();

    void exitAppByConfirm(String str);

    void settingWindowStyle(Window window);

    void shutdownByConfirm(String str);
}