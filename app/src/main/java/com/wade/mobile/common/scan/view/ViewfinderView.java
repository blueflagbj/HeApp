package com.wade.mobile.common.scan.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.heclient.heapp.R;
import com.google.zxing.ResultPoint;

import com.wade.mobile.common.scan.camera.CameraManager;
import java.util.Collection;
import java.util.HashSet;

public final class ViewfinderView extends View {
    private static final long ANIMATION_DELAY = 10;
    private static final int CORNER_WIDTH = 10;
    private static final int MIDDLE_LINE_PADDING = 5;
    private static final int MIDDLE_LINE_WIDTH = 6;
    private static final int OPAQUE = 255;
    private static final int SPEEN_DISTANCE = 5;
    private static final String TAG = "log";
    private static final int TEXT_PADDING_TOP = 30;
    private static final int TEXT_SIZE = 16;
    private static float density;
    private int ScreenRate = ((int) (20.0f * density));
    boolean isFirst;
    private Collection<ResultPoint> lastPossibleResultPoints;
    private final int maskColor;
    private Paint paint = new Paint();
    private Collection<ResultPoint> possibleResultPoints;
    private Bitmap resultBitmap;
    private final int resultColor;
    private final int resultPointColor;
    private int slideBottom;
    private int slideTop;

    public ViewfinderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        density = context.getResources().getDisplayMetrics().density;
        Resources resources = getResources();
        this.maskColor = resources.getColor(R.color.viewfinder_mask);
        this.resultColor = resources.getColor(R.color.result_view);
        this.resultPointColor = resources.getColor(R.color.possible_result_points);
        this.possibleResultPoints = new HashSet(5);
    }

    public void onDraw(Canvas canvas) {
        Rect frame = CameraManager.get().getFramingRect();
        if (frame != null) {
            if (!this.isFirst) {
                this.isFirst = true;
                this.slideTop = frame.top;
                this.slideBottom = frame.bottom;
            }
            int width = canvas.getWidth();
            int height = canvas.getHeight();
            this.paint.setColor(this.resultBitmap != null ? this.resultColor : this.maskColor);
            canvas.drawRect(0.0f, 0.0f, (float) width, (float) frame.top, this.paint);
            canvas.drawRect(0.0f, (float) frame.top, (float) frame.left, (float) (frame.bottom + 1), this.paint);
            canvas.drawRect((float) (frame.right + 1), (float) frame.top, (float) width, (float) (frame.bottom + 1), this.paint);
            canvas.drawRect(0.0f, (float) (frame.bottom + 1), (float) width, (float) height, this.paint);
            if (this.resultBitmap != null) {
                this.paint.setAlpha(255);
                canvas.drawBitmap(this.resultBitmap, (float) frame.left, (float) frame.top, this.paint);
                return;
            }
            this.paint.setColor(-16711936);
            canvas.drawRect((float) frame.left, (float) frame.top, (float) (frame.left + this.ScreenRate), (float) (frame.top + 10), this.paint);
            canvas.drawRect((float) frame.left, (float) frame.top, (float) (frame.left + 10), (float) (frame.top + this.ScreenRate), this.paint);
            canvas.drawRect((float) (frame.right - this.ScreenRate), (float) frame.top, (float) frame.right, (float) (frame.top + 10), this.paint);
            canvas.drawRect((float) (frame.right - 10), (float) frame.top, (float) frame.right, (float) (frame.top + this.ScreenRate), this.paint);
            canvas.drawRect((float) frame.left, (float) (frame.bottom - 10), (float) (frame.left + this.ScreenRate), (float) frame.bottom, this.paint);
            canvas.drawRect((float) frame.left, (float) (frame.bottom - this.ScreenRate), (float) (frame.left + 10), (float) frame.bottom, this.paint);
            canvas.drawRect((float) (frame.right - this.ScreenRate), (float) (frame.bottom - 10), (float) frame.right, (float) frame.bottom, this.paint);
            canvas.drawRect((float) (frame.right - 10), (float) (frame.bottom - this.ScreenRate), (float) frame.right, (float) frame.bottom, this.paint);
            this.slideTop += 5;
            if (this.slideTop >= frame.bottom) {
                this.slideTop = frame.top;
            }
            canvas.drawRect((float) (frame.left + 5), (float) (this.slideTop - 3), (float) (frame.right - 5), (float) (this.slideTop + 3), this.paint);
            Collection<ResultPoint> currentPossible = this.possibleResultPoints;
            Collection<ResultPoint> currentLast = this.lastPossibleResultPoints;
            if (currentPossible.isEmpty()) {
                this.lastPossibleResultPoints = null;
            } else {
                this.possibleResultPoints = new HashSet(5);
                this.lastPossibleResultPoints = currentPossible;
                this.paint.setAlpha(255);
                this.paint.setColor(this.resultPointColor);
            }
            if (currentLast != null) {
                this.paint.setAlpha(127);
                this.paint.setColor(this.resultPointColor);
            }
            postInvalidateDelayed(10, frame.left, frame.top, frame.right, frame.bottom);
        }
    }

    public void drawViewfinder() {
        this.resultBitmap = null;
        invalidate();
    }

    public void drawResultBitmap(Bitmap barcode) {
        this.resultBitmap = barcode;
        invalidate();
    }

    public void addPossibleResultPoint(ResultPoint point) {
        this.possibleResultPoints.add(point);
    }
}