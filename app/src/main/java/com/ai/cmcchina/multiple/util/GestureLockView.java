package com.ai.cmcchina.multiple.util;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.View;

/* renamed from: com.ai.cmcchina.multiple.util.GestureLockView */
public class GestureLockView extends View {
    private static final String TAG = "GestureLockView";
    private int mArrowDegree = -1;
    private Path mArrowPath;
    private float mArrowRate = 0.333f;
    private int mCenterX;
    private int mCenterY;
    private int mColorFingerOn;
    private int mColorFingerUp;
    private int mColorNoFingerInner;
    private int mColorNoFingerOutter;
    private Mode mCurrentStatus = Mode.STATUS_NO_FINGER;
    private int mHeight;
    private float mInnerCircleRadiusRate = 0.3f;
    private Paint mPaint;
    private int mRadius;
    private int mStrokeWidth = 12;
    private int mWidth;

    /* renamed from: com.ai.cmcchina.multiple.util.GestureLockView$Mode */
    enum Mode {
        STATUS_NO_FINGER,
        STATUS_FINGER_ON,
        STATUS_FINGER_UP
    }

    public GestureLockView(Context context, int colorNoFingerInner, int colorNoFingerOutter, int colorFingerOn, int colorFingerUp) {
        super(context);
        this.mColorNoFingerInner = colorNoFingerInner;
        this.mColorNoFingerOutter = colorNoFingerOutter;
        this.mColorFingerOn = colorFingerOn;
        this.mColorFingerUp = colorFingerUp;
        this.mPaint = new Paint(1);
        this.mArrowPath = new Path();
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        this.mWidth = MeasureSpec.getSize(widthMeasureSpec);
        this.mHeight = MeasureSpec.getSize(heightMeasureSpec);
        this.mWidth = this.mWidth < this.mHeight ? this.mWidth : this.mHeight;
        int i = this.mWidth / 2;
        this.mCenterY = i;
        this.mCenterX = i;
        this.mRadius = i;
        this.mRadius -= 18;
        float mArrowLength = ((float) (this.mWidth / 2)) * this.mArrowRate;
        this.mArrowPath.moveTo((float) (this.mWidth / 2), (float) (this.mStrokeWidth + 2));
        this.mArrowPath.lineTo(((float) (this.mWidth / 2)) - mArrowLength, ((float) (this.mStrokeWidth + 2)) + mArrowLength);
        this.mArrowPath.lineTo(((float) (this.mWidth / 2)) + mArrowLength, ((float) (this.mStrokeWidth + 2)) + mArrowLength);
        this.mArrowPath.close();
        this.mArrowPath.setFillType(Path.FillType.WINDING);
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        switch (this.mCurrentStatus) {
            case STATUS_FINGER_ON:
                this.mPaint.setStyle(Paint.Style.STROKE);
                this.mPaint.setColor(this.mColorFingerOn);
                this.mPaint.setStrokeWidth(5.0f);
                canvas.drawCircle((float) this.mCenterX, (float) this.mCenterY, (float) this.mRadius, this.mPaint);
                this.mPaint.setStyle(Paint.Style.FILL);
                canvas.drawCircle((float) this.mCenterX, (float) this.mCenterY, ((float) this.mRadius) * this.mInnerCircleRadiusRate, this.mPaint);
                return;
            case STATUS_FINGER_UP:
                this.mPaint.setColor(this.mColorFingerUp);
                this.mPaint.setStyle(Paint.Style.STROKE);
                this.mPaint.setStrokeWidth(2.0f);
                canvas.drawCircle((float) this.mCenterX, (float) this.mCenterY, (float) this.mRadius, this.mPaint);
                this.mPaint.setStyle(Paint.Style.FILL);
                canvas.drawCircle((float) this.mCenterX, (float) this.mCenterY, ((float) this.mRadius) * this.mInnerCircleRadiusRate, this.mPaint);
                drawArrow(canvas);
                return;
            case STATUS_NO_FINGER:
                this.mPaint.setStyle(Paint.Style.STROKE);
                this.mPaint.setColor(this.mColorNoFingerOutter);
                this.mPaint.setStrokeWidth(2.0f);
                canvas.drawCircle((float) this.mCenterX, (float) this.mCenterY, (float) this.mRadius, this.mPaint);
                return;
            default:
                return;
        }
    }

    private void drawArrow(Canvas canvas) {
        if (this.mArrowDegree != -1) {
            this.mPaint.setStyle(Paint.Style.FILL);
            canvas.save();
            canvas.rotate((float) this.mArrowDegree, (float) this.mCenterX, (float) this.mCenterY);
            canvas.drawPath(this.mArrowPath, this.mPaint);
            canvas.restore();
        }
    }

    public void setMode(Mode mode) {
        this.mCurrentStatus = mode;
        invalidate();
    }

    public void setArrowDegree(int degree) {
        this.mArrowDegree = degree;
    }

    public int getArrowDegree() {
        return this.mArrowDegree;
    }
}