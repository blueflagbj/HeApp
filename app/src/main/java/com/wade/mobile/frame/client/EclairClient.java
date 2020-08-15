package com.wade.mobile.frame.client;


import android.content.Context;
import android.webkit.GeolocationPermissions;
import android.webkit.WebStorage;
import com.wade.mobile.common.MobileLog;

public class EclairClient extends MobileClient {
    private long MAX_QUOTA = 104857600;
    private String TAG = EclairClient.class.getSimpleName();

    public EclairClient(Context ctx) {
        super(ctx);
    }

    public void onExceededDatabaseQuota(String url, String databaseIdentifier, long currentQuota, long estimatedSize, long totalUsedQuota, WebStorage.QuotaUpdater quotaUpdater) {
        MobileLog.d(this.TAG, "event raised onExceededDatabaseQuota estimatedSize: " + Long.toString(estimatedSize) + " currentQuota: " + Long.toString(currentQuota) + " totalUsedQuota: " + Long.toString(totalUsedQuota));
        if (estimatedSize < this.MAX_QUOTA) {
            long newQuota = estimatedSize;
            MobileLog.d(this.TAG, "calling quotaUpdater.updateQuota newQuota: " + Long.toString(newQuota));
            quotaUpdater.updateQuota(newQuota);
            return;
        }
        quotaUpdater.updateQuota(currentQuota);
    }

    public void onConsoleMessage(String message, int lineNumber, String sourceID) {
        MobileLog.d(this.TAG, sourceID + ": Line " + Integer.toString(lineNumber) + " : " + message);
    }

    public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
        super.onGeolocationPermissionsShowPrompt(origin, callback);
        callback.invoke(origin, true, false);
    }
}