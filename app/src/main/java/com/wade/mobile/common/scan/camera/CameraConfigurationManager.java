package com.wade.mobile.common.scan.camera;

import android.content.Context;
import android.graphics.Point;
import android.hardware.Camera;
import android.os.Build;
import android.view.Display;
import android.view.WindowManager;
import com.wade.mobile.common.MobileLog;
import com.wade.mobile.util.Constant;
import java.util.regex.Pattern;

final class CameraConfigurationManager {
    private static final Pattern COMMA_PATTERN = Pattern.compile(Constant.PARAMS_SQE);
    private static final int DESIRED_SHARPNESS = 30;
    private static final String TAG = CameraConfigurationManager.class.getSimpleName();
    private static final int TEN_DESIRED_ZOOM = 27;
    private Point cameraResolution;
    private final Context context;
    private int previewFormat;
    private String previewFormatString;
    private Point screenResolution;

    CameraConfigurationManager(Context context2) {
        this.context = context2;
    }

    /* access modifiers changed from: package-private */
    public void initFromCameraParameters(Camera camera) {
        Camera.Parameters parameters = camera.getParameters();
        this.previewFormat = parameters.getPreviewFormat();
        this.previewFormatString = parameters.get("preview-format");
        MobileLog.d(TAG, "Default preview format: " + this.previewFormat + '/' + this.previewFormatString);
        Display display = ((WindowManager) this.context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        this.screenResolution = new Point(display.getWidth(), display.getHeight());
        MobileLog.d(TAG, "Screen resolution: " + this.screenResolution);
        this.cameraResolution = getCameraResolution(parameters, this.screenResolution);
        MobileLog.d(TAG, "Camera resolution: " + this.screenResolution);
    }

    /* access modifiers changed from: package-private */
    public void setDesiredCameraParameters(Camera camera) {
        Camera.Parameters parameters = camera.getParameters();
        MobileLog.d(TAG, "Setting preview size: " + this.cameraResolution);
        parameters.setPreviewSize(this.cameraResolution.x, this.cameraResolution.y);
        setFlash(parameters);
        setZoom(parameters);
        camera.setDisplayOrientation(90);
        camera.setParameters(parameters);
    }

    /* access modifiers changed from: package-private */
    public Point getCameraResolution() {
        return this.cameraResolution;
    }

    /* access modifiers changed from: package-private */
    public Point getScreenResolution() {
        return this.screenResolution;
    }

    /* access modifiers changed from: package-private */
    public int getPreviewFormat() {
        return this.previewFormat;
    }

    /* access modifiers changed from: package-private */
    public String getPreviewFormatString() {
        return this.previewFormatString;
    }

    private static Point getCameraResolution(Camera.Parameters parameters, Point screenResolution2) {
        String previewSizeValueString = parameters.get("preview-size-values");
        if (previewSizeValueString == null) {
            previewSizeValueString = parameters.get("preview-size-value");
        }
        Point cameraResolution2 = null;
        if (previewSizeValueString != null) {
            MobileLog.d(TAG, "preview-size-values parameter: " + previewSizeValueString);
            cameraResolution2 = findBestPreviewSizeValue(previewSizeValueString, screenResolution2);
        }
        if (cameraResolution2 == null) {
            return new Point((screenResolution2.x >> 3) << 3, (screenResolution2.y >> 3) << 3);
        }
        return cameraResolution2;
    }

    private static Point findBestPreviewSizeValue(CharSequence previewSizeValueString, Point screenResolution2) {
        int bestX = 0;
        int bestY = 0;
        int diff = Integer.MAX_VALUE;
        String[] split = COMMA_PATTERN.split(previewSizeValueString);
        int length = split.length;
        int i = 0;
        while (true) {
            if (i >= length) {
                break;
            }
            String previewSize = split[i].trim();
            int dimPosition = previewSize.indexOf(120);
            if (dimPosition < 0) {
                MobileLog.w(TAG, "Bad preview-size: " + previewSize);
            } else {
                try {
                    int newX = Integer.parseInt(previewSize.substring(0, dimPosition));
                    int newY = Integer.parseInt(previewSize.substring(dimPosition + 1));
                    float newDiff = Math.abs(((((float) screenResolution2.x) * 1.0f) / ((float) newY)) - ((((float) screenResolution2.y) * 1.0f) / ((float) newX)));
                    if (newDiff == 0.0f) {
                        bestX = newX;
                        bestY = newY;
                        break;
                    } else if (newDiff < ((float) diff)) {
                        bestX = newX;
                        bestY = newY;
                        diff = (int) newDiff;
                    }
                } catch (NumberFormatException e) {
                    MobileLog.w(TAG, "Bad preview-size: " + previewSize);
                }
            }
            i++;
        }
        if (bestX <= 0 || bestY <= 0) {
            return null;
        }
        return new Point(bestX, bestY);
    }

    private static int findBestMotZoomValue(CharSequence stringValues, int tenDesiredZoom) {
        int tenBestValue = 0;
        String[] split = COMMA_PATTERN.split(stringValues);
        int length = split.length;
        int i = 0;
        while (i < length) {
            try {
                double value = Double.parseDouble(split[i].trim());
                int tenValue = (int) (10.0d * value);
                if (Math.abs(((double) tenDesiredZoom) - value) < ((double) Math.abs(tenDesiredZoom - tenBestValue))) {
                    tenBestValue = tenValue;
                }
                i++;
            } catch (NumberFormatException e) {
                return tenDesiredZoom;
            }
        }
        return tenBestValue;
    }

    private void setFlash(Camera.Parameters parameters) {
        if (!Build.MODEL.contains("Behold II") || CameraManager.SDK_INT != 3) {
            parameters.set("flash-value", 2);
        } else {
            parameters.set("flash-value", 1);
        }
        parameters.set("flash-mode", "off");
    }

    private void setZoom(Camera.Parameters parameters) {
        String zoomSupportedString = parameters.get("zoom-supported");
        if (zoomSupportedString == null || Boolean.parseBoolean(zoomSupportedString)) {
            int tenDesiredZoom = 27;
            String maxZoomString = parameters.get("max-zoom");
            if (maxZoomString != null) {
                try {
                    int tenMaxZoom = (int) (10.0d * Double.parseDouble(maxZoomString));
                    if (27 > tenMaxZoom) {
                        tenDesiredZoom = tenMaxZoom;
                    }
                } catch (NumberFormatException e) {
                    MobileLog.w(TAG, "Bad max-zoom: " + maxZoomString);
                }
            }
            String takingPictureZoomMaxString = parameters.get("taking-picture-zoom-max");
            if (takingPictureZoomMaxString != null) {
                try {
                    int tenMaxZoom2 = Integer.parseInt(takingPictureZoomMaxString);
                    if (tenDesiredZoom > tenMaxZoom2) {
                        tenDesiredZoom = tenMaxZoom2;
                    }
                } catch (NumberFormatException e2) {
                    MobileLog.w(TAG, "Bad taking-picture-zoom-max: " + takingPictureZoomMaxString);
                }
            }
            String motZoomValuesString = parameters.get("mot-zoom-values");
            if (motZoomValuesString != null) {
                tenDesiredZoom = findBestMotZoomValue(motZoomValuesString, tenDesiredZoom);
            }
            String motZoomStepString = parameters.get("mot-zoom-step");
            if (motZoomStepString != null) {
                try {
                    int tenZoomStep = (int) (10.0d * Double.parseDouble(motZoomStepString.trim()));
                    if (tenZoomStep > 1) {
                        tenDesiredZoom -= tenDesiredZoom % tenZoomStep;
                    }
                } catch (NumberFormatException e3) {
                }
            }
            if (!(maxZoomString == null && motZoomValuesString == null)) {
                parameters.set("zoom", String.valueOf(((double) tenDesiredZoom) / 10.0d));
            }
            if (takingPictureZoomMaxString != null) {
                parameters.set("taking-picture-zoom", tenDesiredZoom);
            }
        }
    }

    public static int getDesiredSharpness() {
        return 30;
    }
}