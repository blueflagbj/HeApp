package com.wade.mobile.common.scan.decoding;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.heclient.heapp.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import com.wade.mobile.common.MobileLog;
import com.wade.mobile.common.scan.activity.CaptureActivity;
import com.wade.mobile.common.scan.camera.CameraManager;
import com.wade.mobile.common.scan.view.ViewfinderResultPointCallback;
import java.util.Vector;

public final class CaptureActivityHandler extends Handler {
    private static final String TAG = CaptureActivityHandler.class.getSimpleName();
    private final CaptureActivity activity;
    private final DecodeThread decodeThread;
    private State state = State.SUCCESS;

    private enum State {
        PREVIEW,
        SUCCESS,
        DONE
    }

    public CaptureActivityHandler(CaptureActivity activity2, Vector<BarcodeFormat> decodeFormats, String characterSet) {
        this.activity = activity2;
        this.decodeThread = new DecodeThread(activity2, decodeFormats, characterSet, new ViewfinderResultPointCallback(activity2.getViewfinderView()));
        this.decodeThread.start();
        CameraManager.get().startPreview();
        restartPreviewAndDecode();
    }

    public void handleMessage(Message message) {
        Bitmap barcode;
        if (message.what == R.id.auto_focus) {
            if (this.state == State.PREVIEW) {
                CameraManager.get().requestAutoFocus(this, R.id.auto_focus);
            }
        } else if (message.what == R.id.restart_preview) {
            MobileLog.d(TAG, "Got restart preview message");
            restartPreviewAndDecode();
        } else if (message.what == R.id.decode_succeeded) {
            MobileLog.d(TAG, "Got decode succeeded message");
            this.state = State.SUCCESS;
            Bundle bundle = message.getData();
            if (bundle == null) {
                barcode = null;
            } else {
                barcode = bundle.getParcelable(DecodeThread.BARCODE_BITMAP);
            }
            this.activity.handleDecode((Result) message.obj, barcode);
        } else if (message.what == R.id.decode_failed) {
            this.state = State.PREVIEW;
            CameraManager.get().requestPreviewFrame(this.decodeThread.getHandler(), R.id.decode);
        } else if (message.what == R.id.return_scan_result) {
            MobileLog.d(TAG, "Got return scan result message");
            this.activity.setResult(-1, (Intent) message.obj);
            this.activity.finish();
        } else if (message.what == R.id.launch_product_query) {
            MobileLog.d(TAG, "Got product query message");
            Intent intent = new Intent("android.intent.action.VIEW", Uri.parse((String) message.obj));
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
            this.activity.startActivity(intent);
        }
    }

    public void quitSynchronously() {
        this.state = State.DONE;
        CameraManager.get().stopPreview();
        Message.obtain(this.decodeThread.getHandler(), R.id.quit).sendToTarget();
        try {
            this.decodeThread.join();
        } catch (InterruptedException e) {
        }
        removeMessages(R.id.decode_succeeded);
        removeMessages(R.id.decode_failed);
    }

    private void restartPreviewAndDecode() {
        if (this.state == State.SUCCESS) {
            this.state = State.PREVIEW;
            CameraManager.get().requestPreviewFrame(this.decodeThread.getHandler(), R.id.decode);
            CameraManager.get().requestAutoFocus(this, R.id.auto_focus);
            this.activity.drawViewfinder();
        }
    }
}