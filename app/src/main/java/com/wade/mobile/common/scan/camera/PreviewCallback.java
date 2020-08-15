package com.wade.mobile.common.scan.camera;


import android.graphics.Point;
import android.hardware.Camera;
import android.os.Handler;
import com.wade.mobile.common.MobileLog;

final class PreviewCallback implements Camera.PreviewCallback {
    private static final String TAG = PreviewCallback.class.getSimpleName();
    private final CameraConfigurationManager configManager;
    private Handler previewHandler;
    private int previewMessage;
    private final boolean useOneShotPreviewCallback;

    PreviewCallback(CameraConfigurationManager configManager2, boolean useOneShotPreviewCallback2) {
        this.configManager = configManager2;
        this.useOneShotPreviewCallback = useOneShotPreviewCallback2;
    }

    /* access modifiers changed from: package-private */
    public void setHandler(Handler previewHandler2, int previewMessage2) {
        this.previewHandler = previewHandler2;
        this.previewMessage = previewMessage2;
    }

    public void onPreviewFrame(byte[] data, Camera camera) {
        Point cameraResolution = this.configManager.getCameraResolution();
        if (!this.useOneShotPreviewCallback) {
            camera.setPreviewCallback((Camera.PreviewCallback) null);
        }
        if (this.previewHandler != null) {
            this.previewHandler.obtainMessage(this.previewMessage, cameraResolution.x, cameraResolution.y, data).sendToTarget();
            this.previewHandler = null;
            return;
        }
        MobileLog.d(TAG, "Got preview callback, but no handler for it");
    }
}