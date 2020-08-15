package com.wade.mobile.common.sms.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;
import com.wade.mobile.common.sms.listener.OnSmsReceiveListener;

public class SmsReceiver extends BroadcastReceiver {
    private OnSmsReceiveListener listener;

    public SmsReceiver(OnSmsReceiveListener listener2) {
        this.listener = listener2;
    }

    public void onReceive(Context context, Intent intent) {
        for (Object p : (Object[]) intent.getExtras().get("pdus")) {
            SmsMessage sms = SmsMessage.createFromPdu((byte[]) p);
            String content = sms.getMessageBody();
            String sender = sms.getOriginatingAddress();
            long time = sms.getTimestampMillis();
            this.listener.setSmsMessage(sms);
            if (this.listener.isAbortBroadcast(content, sender, time)) {
                abortBroadcast();
            }
            if (this.listener.onSmsReceive(content, sender, time)) {
                context.unregisterReceiver(this);
            }
        }
    }
}