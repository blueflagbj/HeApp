package com.wade.mobile.app;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.Process;
import android.widget.Toast;
import com.wade.mobile.frame.config.MobileConfig;
import com.wade.mobile.util.Constant;
import java.io.File;

public class SimpleUpdate {
    private Context context;
    /* access modifiers changed from: private */
    public long downloadId;
    private String title;
    private String updateUrl;

    public SimpleUpdate(Context context2, String updateUrl2) {
        this.context = context2;
        this.updateUrl = updateUrl2;
    }

    public void setTitle(String title2) {
        this.title = title2;
    }

    public void update() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.DOWNLOAD_COMPLETE");
        this.context.registerReceiver(new CompleteReceiver(), filter);
        if (MobileAppInfo.getOsVersionNumber() <= 9) {
            this.context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(this.updateUrl)));
            return;
        }
        DownloadManager downloadManager = (DownloadManager) this.context.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(this.updateUrl));
        request.setAllowedNetworkTypes(3);
        request.setDestinationInExternalFilesDir(this.context, Environment.DIRECTORY_DOWNLOADS, MobileConfig.getInstance().getAppPath() + ".apk");
        if (this.title != null) {
            request.setTitle(this.title);
        }
        this.downloadId = downloadManager.enqueue(request);
    }

    /* access modifiers changed from: private */
    public void installAPK(String apkPath) {
        Intent intent = new Intent("android.intent.action.VIEW");
            intent.setFlags(Intent.FLAG_RECEIVER_FOREGROUND);
        intent.setDataAndType(Uri.fromFile(new File(apkPath)), "application/vnd.android.package-archive");
        this.context.startActivity(intent);
        Process.killProcess(Process.myPid());
    }

    class CompleteReceiver extends BroadcastReceiver {
        CompleteReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.intent.action.DOWNLOAD_COMPLETE")) {
                long id = intent.getLongExtra("extra_download_id", 0);
                if (SimpleUpdate.this.downloadId == id) {
                    String downloadPath = SimpleUpdate.this.getDownloadPath(id);
                    if (downloadPath == null) {
                        Toast.makeText(context, "下载异常！！！", Toast.LENGTH_LONG).show();
                        return;
                    }
                    SimpleUpdate.this.installAPK(downloadPath.replace(Constant.START_FILE, ""));
                    context.unregisterReceiver(this);
                }
                Toast.makeText(context, "更新完成....", Toast.LENGTH_LONG).show();
            }
        }
    }

    /* access modifiers changed from: private */
    public String getDownloadPath(long id) {
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(new long[]{id});
        Cursor cursor = ((DownloadManager) this.context.getSystemService(Context.DOWNLOAD_SERVICE)).query(query);
        String path = null;
        while (cursor.moveToNext()) {
            path = cursor.getString(cursor.getColumnIndex("local_uri"));
        }
        return path;
    }
}