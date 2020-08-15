package com.ai.cmcchina.multiple.util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;

import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.core.internal.view.SupportMenu;

import com.ai.cmcchina.multiple.util.GestureLockView;
import java.util.ArrayList;
import java.util.List;

/* renamed from: com.ai.cmcchina.multiple.util.GestureLockViewGroup */
public class GestureLockViewGroup extends RelativeLayout {
    private static final String TAG = "GestureLockViewGroup";
    private int[] mAnswer;
    private List<Integer> mChoose;
    private int mCount;
    private int mFingerOnColor;
    private int mFingerUpColor;
    private int mGestureLockViewWidth;
    private GestureLockView[] mGestureLockViews;
    private int mHeight;
    private int mLastPathX;
    private int mLastPathY;
    private int mMarginBetweenLockView;
    private int mNoFingerInnerCircleColor;
    private int mNoFingerOuterCircleColor;
    private OnGestureLockViewListener mOnGestureLockViewListener;
    private Paint mPaint;
    private Path mPath;
    private Point mTmpTarget;
    private int mTryTimes;
    private int mWidth;

    /* renamed from: com.ai.cmcchina.multiple.util.GestureLockViewGroup$OnGestureLockViewListener */
    public interface OnGestureLockViewListener {
        void gesturePasswordResult(String str);

        void onGestureEvent(boolean z);

        void onUnmatchedExceedBoundary();
    }

    public GestureLockViewGroup(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GestureLockViewGroup(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mCount = 3;
        this.mAnswer = new int[0];
        this.mChoose = new ArrayList();
        this.mMarginBetweenLockView = 30;
        this.mNoFingerInnerCircleColor = -2040869;
        this.mNoFingerOuterCircleColor = -2040869;
        this.mFingerOnColor = -12490271;
        this.mFingerUpColor = SupportMenu.CATEGORY_MASK;
        this.mTmpTarget = new Point();
        this.mTryTimes = 4;
        this.mPaint = new Paint(1);
        this.mPaint.setStyle(Paint.Style.STROKE);
        this.mPaint.setStrokeCap(Paint.Cap.ROUND);
        this.mPaint.setStrokeJoin(Paint.Join.ROUND);
        this.mPath = new Path();
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        this.mWidth = MeasureSpec.getSize(widthMeasureSpec);
        this.mHeight = MeasureSpec.getSize(heightMeasureSpec);
        int i = this.mWidth < this.mHeight ? this.mWidth : this.mHeight;
        this.mWidth = i;
        this.mHeight = i;
        if (this.mGestureLockViews == null) {
            this.mGestureLockViews = new GestureLockView[(this.mCount * this.mCount)];
            this.mGestureLockViewWidth = (int) ((((float) (this.mWidth * 4)) * 1.0f) / ((float) ((this.mCount * 5) + 1)));
            this.mMarginBetweenLockView = (int) (((double) this.mGestureLockViewWidth) * 0.25d);
            this.mPaint.setStrokeWidth(((float) this.mGestureLockViewWidth) * 0.15f);
            for (int i2 = 0; i2 < this.mGestureLockViews.length; i2++) {
                this.mGestureLockViews[i2] = new GestureLockView(getContext(), this.mNoFingerInnerCircleColor, this.mNoFingerOuterCircleColor, this.mFingerOnColor, this.mFingerUpColor);
                this.mGestureLockViews[i2].setId(i2 + 1);
                LayoutParams lockerParams = new LayoutParams(this.mGestureLockViewWidth, this.mGestureLockViewWidth);
                if (i2 % this.mCount != 0) {
                    lockerParams.addRule(1, this.mGestureLockViews[i2 - 1].getId());
                }
                if (i2 > this.mCount - 1) {
                    lockerParams.addRule(3, this.mGestureLockViews[i2 - this.mCount].getId());
                }
                int rightMargin = this.mMarginBetweenLockView;
                int bottomMargin = this.mMarginBetweenLockView;
                int leftMagin = 0;
                int topMargin = 0;
                if (i2 >= 0 && i2 < this.mCount) {
                    topMargin = this.mMarginBetweenLockView;
                }
                if (i2 % this.mCount == 0) {
                    leftMagin = this.mMarginBetweenLockView;
                }
                lockerParams.setMargins(leftMagin, topMargin, rightMargin, bottomMargin);
                this.mGestureLockViews[i2].setMode(GestureLockView.Mode.STATUS_NO_FINGER);
                addView(this.mGestureLockViews[i2], lockerParams);
            }
           // //G3Logger.debug("GestureLockViewGroupmWidth = " + this.mWidth + " ,  mGestureViewWidth = " + this.mGestureLockViewWidth + " , mMarginBetweenLockView = " + this.mMarginBetweenLockView);
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (action) {
            case 0:
                reset();
                inputPassword(x, y);
                break;
            case 1:
                this.mPaint.setColor(this.mFingerUpColor);
                this.mPaint.setAlpha(120);
                this.mTryTimes--;
                if (this.mOnGestureLockViewListener != null && this.mChoose.size() > 0) {
                    this.mOnGestureLockViewListener.gesturePasswordResult(getGesturePassword());
                    this.mOnGestureLockViewListener.onGestureEvent(checkAnswer());
                }
                //G3Logger.debug("GestureLockViewGroupmUnMatchExceedBoundary = " + this.mTryTimes);
                //G3Logger.debug("GestureLockViewGroupmChoose = " + this.mChoose);
                this.mTmpTarget.x = this.mLastPathX;
                this.mTmpTarget.y = this.mLastPathY;
                changeItemMode();
                for (int i = 0; i + 1 < this.mChoose.size(); i++) {
                    int childId = this.mChoose.get(i).intValue();
                    int nextChildId = this.mChoose.get(i + 1).intValue();
                    GestureLockView startChild = (GestureLockView) findViewById(childId);
                    GestureLockView nextChild = (GestureLockView) findViewById(nextChildId);
                    startChild.setArrowDegree(((int) Math.toDegrees(Math.atan2((double) (nextChild.getTop() - startChild.getTop()), (double) (nextChild.getLeft() - startChild.getLeft())))) + 90);
                }
                break;
            case 2:
                inputPassword(x, y);
                break;
        }
        invalidate();
        return true;
    }

    private void inputPassword(int x, int y) {
        this.mPaint.setColor(this.mFingerOnColor);
        this.mPaint.setAlpha(120);
        GestureLockView child = getChildIdByPos(x, y);
        if (child != null) {
            int cId = child.getId();
            if (!this.mChoose.contains(Integer.valueOf(cId))) {
                this.mChoose.add(Integer.valueOf(cId));
                child.setMode(GestureLockView.Mode.STATUS_FINGER_ON);
                this.mLastPathX = (child.getLeft() / 2) + (child.getRight() / 2);
                this.mLastPathY = (child.getTop() / 2) + (child.getBottom() / 2);
                if (this.mChoose.size() == 1) {
                    this.mPath.moveTo((float) this.mLastPathX, (float) this.mLastPathY);
                } else {
                    this.mPath.lineTo((float) this.mLastPathX, (float) this.mLastPathY);
                }
            }
        }
        this.mTmpTarget.x = x;
        this.mTmpTarget.y = y;
    }

    private void changeItemMode() {
        for (GestureLockView gestureLockView : this.mGestureLockViews) {
            if (this.mChoose.contains(Integer.valueOf(gestureLockView.getId()))) {
                gestureLockView.setMode(GestureLockView.Mode.STATUS_FINGER_UP);
            }
        }
    }

    public void reset() {
        this.mChoose.clear();
        this.mPath.reset();
        for (GestureLockView gestureLockView : this.mGestureLockViews) {
            gestureLockView.setMode(GestureLockView.Mode.STATUS_NO_FINGER);
            gestureLockView.setArrowDegree(-1);
        }
    }

    private boolean checkAnswer() {
        if (this.mAnswer.length != this.mChoose.size()) {
            return false;
        }
        for (int i = 0; i < this.mAnswer.length; i++) {
            if (this.mAnswer[i] != this.mChoose.get(i).intValue()) {
                return false;
            }
        }
        return true;
    }

    private String getGesturePassword() {
        String gesturePassword = "";
        for (int i = 0; i < this.mChoose.size(); i++) {
            gesturePassword = gesturePassword + (this.mChoose.get(i) + "");
        }
        return gesturePassword;
    }

    private boolean checkPositionInChild(View child, int x, int y) {
        int padding = (int) (((double) this.mGestureLockViewWidth) * 0.15d);
        if (x < child.getLeft() + padding || x > child.getRight() - padding || y < child.getTop() + padding || y > child.getBottom() - padding) {
            return false;
        }
        return true;
    }

    private GestureLockView getChildIdByPos(int x, int y) {
        for (GestureLockView gestureLockView : this.mGestureLockViews) {
            if (checkPositionInChild(gestureLockView, x, y)) {
                return gestureLockView;
            }
        }
        return null;
    }

    public void setOnGestureLockViewListener(OnGestureLockViewListener listener) {
        this.mOnGestureLockViewListener = listener;
    }

    public void setAnswer(int[] answer) {
        this.mAnswer = answer;
    }

    public void setUnMatchExceedBoundary(int boundary) {
        this.mTryTimes = boundary;
    }

    public void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (this.mPath != null) {
            canvas.drawPath(this.mPath, this.mPaint);
        }
        if (this.mChoose.size() > 0 && this.mLastPathX != 0 && this.mLastPathY != 0) {
            canvas.drawLine((float) this.mLastPathX, (float) this.mLastPathY, (float) this.mTmpTarget.x, (float) this.mTmpTarget.y, this.mPaint);
        }
    }
}