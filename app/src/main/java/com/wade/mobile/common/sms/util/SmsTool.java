package com.wade.mobile.common.sms.util;

import android.content.Context;
import android.content.IntentFilter;
import com.wade.mobile.common.MobileLog;
import com.wade.mobile.common.sms.listener.OnSmsReceiveListener;
import com.wade.mobile.common.sms.receiver.SmsReceiver;

public class SmsTool {
    public static final String ACTION_SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    public static final String TAG = SmsTool.class.getName();

    public static void setSmsListener(Context context, OnSmsReceiveListener listener) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_SMS_RECEIVED);
        intentFilter.setPriority(1000);
        context.registerReceiver(new SmsReceiver(listener), intentFilter);
        MobileLog.d(TAG, "register sms receiver.");
    }
}