package com.ai.cmcchina.multiple.util;


import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.webkit.MimeTypeMap;
import com.ai.cmcchina.crm.fragment.BaseFragment;
import com.ai.cmcchina.crm.util.UIUtil;
import com.ai.cmcchina.crm.view.AlertDialog;
import com.wade.mobile.frame.plugin.Plugin;
import java.io.File;

/* renamed from: com.ai.cmcchina.multiple.util.OpenRealNameAuthClientUtil */
public class OpenRealNameAuthClientUtil {
    public static final int REQUEST_CODE = BaseFragment.REQUEST_CODE;

    public static void call10085(Plugin plugin, final Activity context, String transactionId, String signatureCharSequence, String channelId, String phone, String provinceCode, String account, String nodeCode) {
        try {
            context.getPackageManager().getPackageInfo("com.asiainfo.cm10085", PackageManager.GET_ACTIVITIES);
            Intent intent = new Intent();
            intent.setComponent(new ComponentName("com.asiainfo.cm10085", "com.asiainfo.cm10085.IdentityAuthenticationActivity"));
            intent.putExtra("transactionID", transactionId);
            intent.putExtra("billId", phone);
            intent.putExtra("account", account);
            intent.putExtra("channelCode", channelId);
            intent.putExtra("provinceCode", provinceCode);
            intent.putExtra("signature", signatureCharSequence);
            intent.putExtra("mode", "3");
            intent.putExtra("nodeCode", nodeCode);
            plugin.startActivityForResult(intent, REQUEST_CODE);
        } catch (PackageManager.NameNotFoundException e) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("注意");
            builder.setMessage("本机未安装10085实名信息采集app，是否下载？");
            builder.setPositiveButton("下载", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                @TargetApi(9)
                public void onClick(DialogInterface dialog, int which) {
                    OpenRealNameAuthClientUtil.dowanLoadApk(context);
                    if (Build.VERSION.SDK_INT > 9) {
                        Intent intent1 = new Intent("android.intent.action.VIEW_DOWNLOADS");
                        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent1);
                    }
                }
            }).setNegativeButton("取消", (DialogInterface.OnClickListener) null);
            builder.create().show();
        }
    }

    /* access modifiers changed from: private */
    @TargetApi(11)
    public static void dowanLoadApk(Activity context) {
        String url = "http://smrz.realnameonline.cn:30202" + "/down/apk/smrz_nfc.apk";
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setAllowedNetworkTypes(3);
        request.setAllowedOverRoaming(true);
        if (11 > Build.VERSION.SDK_INT) {
            request.setShowRunningNotification(true);
        } else if (13 >= Build.VERSION.SDK_INT) {
            request.setNotificationVisibility(0);
        } else {
            request.setNotificationVisibility(1);
        }
        request.setVisibleInDownloadsUi(true);
        request.setTitle("10085客户端");
        final String filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator + "10085.apk";
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).mkdirs();
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "10085.apk");
        request.setMimeType(MimeTypeMap.getSingleton().getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(url)));
        final long enqueueId = downloadManager.enqueue(request);
        IntentFilter filter = new IntentFilter("android.intent.action.DOWNLOAD_COMPLETE");
        filter.addAction("android.intent.action.DOWNLOAD_NOTIFICATION_CLICKED");
        context.registerReceiver(new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals("android.intent.action.DOWNLOAD_COMPLETE")) {
                    if (enqueueId != intent.getExtras().getLong("extra_download_id")) {
                        UIUtil.toast(context, (Object) "下载失败！");
                    } else if (new File(filePath).exists()) {
                        OpenRealNameAuthClientUtil.installApk(context, new File(filePath));
                    } else {
                        UIUtil.toast(context, (Object) "下载失败！");
                    }
                    context.unregisterReceiver(this);
                } else if ("android.intent.action.DOWNLOAD_NOTIFICATION_CLICKED".equals(intent.getAction()) && Build.VERSION.SDK_INT > 9) {
                    Intent intent1 = new Intent("android.intent.action.VIEW_DOWNLOADS");
                    intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent1);
                }
            }
        }, filter);
    }

    protected static void installApk(Context context, File apk) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setDataAndType(Uri.fromFile(apk), "application/vnd.android.package-archive");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}