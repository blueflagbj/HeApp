package com.wade.mobile.common.sms.listener;

import android.telephony.SmsMessage;

public abstract class OnSmsReceiveListener {
    private SmsMessage sms;

    public abstract boolean onSmsReceive(String str, String str2, long j);

    public boolean isAbortBroadcast(String content, String sender, long time) {
        return false;
    }

    public void setSmsMessage(SmsMessage sms2) {
        this.sms = sms2;
    }

    public SmsMessage getSmsMessage() {
        return this.sms;
    }
}