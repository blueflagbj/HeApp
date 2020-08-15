package com.wade.mobile.func;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;
import android.telephony.SmsManager;
import android.widget.Toast;
import com.wade.mobile.frame.IWadeMobile;
import com.wade.mobile.frame.plugin.Plugin;
import com.wade.mobile.util.Constant;
import com.wade.mobile.util.Messages;
import java.util.Iterator;
import org.json.JSONArray;

public class MobileBasic extends Plugin {
    public MobileBasic(IWadeMobile wademobile) {
        super(wademobile);
    }

    public void call(JSONArray param) throws Exception {
        if (param.length() < 2) {
            throw new Exception(Messages.EXCEPTION_PARAM);
        }
        call(param.getString(0), param.getBoolean(1));
    }

    public void call(String sn, boolean autoCall) {
        if (autoCall) {
            this.context.startActivity(new Intent("android.intent.action.CALL", Uri.parse("tel:" + sn)));
            return;
        }
        this.context.startActivity(new Intent("android.intent.action.DIAL", Uri.parse("tel:" + sn)));
    }

    public void sms(JSONArray param) throws Exception {
        if (param.length() < 3) {
            throw new Exception(Messages.EXCEPTION_PARAM);
        }
        sms(param.getString(0), param.getString(1), param.getBoolean(2));
    }

    public void sms(String sn, String message, boolean flag) {
        String message2;
        if (flag) {
            Intent intent = new Intent("android.intent.action.SENDTO");
            SmsManager smsManager = SmsManager.getDefault();
            PendingIntent pi = PendingIntent.getActivity(this.context, 0, intent, 0);
            if (message.length() > 70) {
                Iterator i$ = smsManager.divideMessage(message).iterator();
                while (i$.hasNext()) {
                    smsManager.sendTextMessage(sn, (String) null, i$.next().toString(), pi, (PendingIntent) null);
                }
            } else {
                smsManager.sendTextMessage(sn, (String) null, message, pi, (PendingIntent) null);
            }
            if (message.length() > 30) {
                message2 = message.substring(0, 30) + "……" + Constant.LINE_SEPARATOR + Messages.SMS_SUCCESS;
            } else {
                message2 = message + Constant.LINE_SEPARATOR + Messages.SMS_SUCCESS;
            }
            Toast.makeText(this.context, message2, Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent2 = new Intent("android.intent.action.VIEW");
        intent2.setData(Uri.parse("sms:" + sn));
        intent2.putExtra("address", sn);
        intent2.putExtra("sms_body", message);
        intent2.setType("vnd.android-dir/mms-sms");
        this.context.startActivity(intent2);
    }

    public void beep(JSONArray param) throws Exception {
        if (param.length() < 1) {
            throw new Exception(Messages.EXCEPTION_PARAM);
        }
        beep(param.getLong(0));
    }

    public void beep(long count) {
        Ringtone notification = RingtoneManager.getRingtone(this.context, RingtoneManager.getDefaultUri(2));
        if (notification != null) {
            for (long i = 0; i < count; i++) {
                notification.play();
                long timeout = 5000;
                while (notification.isPlaying() && timeout > 0) {
                    timeout -= 100;
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                    }
                }
            }
        }
    }

    public void shock(JSONArray param) throws Exception {
        if (param.length() < 1) {
            throw new Exception(Messages.EXCEPTION_PARAM);
        }
        shock(param.getLong(0));
    }

    public void shock(long time) {
        if (time < 500) {
            time = 500;
        } else if (time > 10000) {
            time = 10000;
        }
        ((Vibrator) this.context.getSystemService(Context.VIBRATOR_SERVICE)).vibrate(time);
    }
}