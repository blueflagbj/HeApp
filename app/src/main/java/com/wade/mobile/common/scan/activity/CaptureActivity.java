package com.wade.mobile.common.scan.activity;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.heclient.heapp.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import com.wade.mobile.common.map.util.MapConstant;
import com.wade.mobile.common.scan.camera.CameraManager;
import com.wade.mobile.common.scan.decoding.CaptureActivityHandler;
import com.wade.mobile.common.scan.decoding.InactivityTimer;
import com.wade.mobile.common.scan.view.ViewfinderView;
import com.wade.mobile.util.Constant;
import java.io.IOException;
import java.util.Vector;

public class CaptureActivity extends Activity implements SurfaceHolder.Callback {
    private static final float BEEP_VOLUME = 0.1f;
    private static final long VIBRATE_DURATION = 200;
    private final MediaPlayer.OnCompletionListener beepListener = new MediaPlayer.OnCompletionListener() {
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };
    private ImageButton cancelScanButton;
    private String characterSet;
    private Vector<BarcodeFormat> decodeFormats;
    /* access modifiers changed from: private */
    public ImageButton flashlightButton;
    private CaptureActivityHandler handler;
    private boolean hasSurface;
    private InactivityTimer inactivityTimer;
    /* access modifiers changed from: private */
    public boolean isFlashlight;
    private MediaPlayer mediaPlayer;
    private boolean playBeep;
    private boolean vibrate;
    private ViewfinderView viewfinderView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan_zxing_capture);
        CameraManager.init(getApplication());
        this.viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
        this.cancelScanButton = (ImageButton) findViewById(R.id.btn_cancel_scan);
        this.flashlightButton = (ImageButton) findViewById(R.id.btn_flashlight);
        this.cancelScanButton.getBackground().setAlpha(0);
        this.flashlightButton.getBackground().setAlpha(0);
        this.hasSurface = false;
        this.inactivityTimer = new InactivityTimer(this);
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        SurfaceHolder surfaceHolder = ((SurfaceView) findViewById(R.id.preview_view)).getHolder();
        if (this.hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(3);
        }
        this.decodeFormats = null;
        this.characterSet = null;
        this.playBeep = true;
        if (((AudioManager) getSystemService(Context.AUDIO_SERVICE)).getRingerMode() != 2) {
            this.playBeep = false;
        }
        initBeepSound();
        this.vibrate = true;
        this.cancelScanButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CaptureActivity.this.finish();
            }
        });
        this.flashlightButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (CaptureActivity.this.isFlashlight) {
                    CameraManager.get().closeLight();
                    CaptureActivity.this.flashlightButton.setImageResource(R.drawable.scan_flashlight_off);
                    CaptureActivity.this.isFlashlight = false;
                    return;
                }
                CameraManager.get().openLight();
                CaptureActivity.this.flashlightButton.setImageResource(R.drawable.scan_flashlight_on);
                CaptureActivity.this.isFlashlight = true;
            }
        });
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        if (this.handler != null) {
            this.handler.quitSynchronously();
            this.handler = null;
        }
        CameraManager.get().closeDriver();
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        this.inactivityTimer.shutdown();
        super.onDestroy();
    }

    public void handleDecode(Result result, Bitmap barcode) {
        this.inactivityTimer.onActivity();
        playBeepSoundAndVibrate();
        String resultString = result.getText();
        if (resultString.equals("")) {
            Toast.makeText(this, "Scan failed!", 0).show();
        } else {
            Intent resultIntent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putString(MapConstant.CALLBACK_RESULT, resultString);
            resultIntent.putExtras(bundle);
            setResult(-1, resultIntent);
        }
        finish();
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder);
            if (this.handler == null) {
                this.handler = new CaptureActivityHandler(this, this.decodeFormats, this.characterSet);
            }
        } catch (IOException e) {
        } catch (RuntimeException e2) {
        }
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    public void surfaceCreated(SurfaceHolder holder) {
        if (!this.hasSurface) {
            this.hasSurface = true;
            initCamera(holder);
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        this.hasSurface = false;
    }

    public ViewfinderView getViewfinderView() {
        return this.viewfinderView;
    }

    public Handler getHandler() {
        return this.handler;
    }

    public void drawViewfinder() {
        this.viewfinderView.drawViewfinder();
    }

    private void initBeepSound() {
        if (this.playBeep && this.mediaPlayer == null) {
            setVolumeControlStream(3);
            this.mediaPlayer = new MediaPlayer();
            this.mediaPlayer.setAudioStreamType(3);
            this.mediaPlayer.setOnCompletionListener(this.beepListener);
            AssetFileDescriptor file = getResources().openRawResourceFd(R.raw.beep);
            try {
                this.mediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
                file.close();
                this.mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                this.mediaPlayer.prepare();
            } catch (IOException e) {
                this.mediaPlayer = null;
            }
        }
    }

    private void playBeepSoundAndVibrate() {
        if (this.playBeep && this.mediaPlayer != null) {
            this.mediaPlayer.start();
        }
        if (this.vibrate) {
            ((Vibrator) getSystemService(Context.VIBRATOR_SERVICE)).vibrate(200);
        }
    }
}