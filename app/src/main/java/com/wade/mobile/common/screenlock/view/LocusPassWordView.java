package com.wade.mobile.common.screenlock.view;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.heclient.heapp.R;
import com.wade.mobile.common.MobileLog;
import com.wade.mobile.common.screenlock.util.BitmapUtil;
import com.wade.mobile.common.screenlock.util.MathUtil;
import com.wade.mobile.common.screenlock.util.RoundUtil;
import com.wade.mobile.util.Constant;
import com.wade.mobile.util.cipher.MD5;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class LocusPassWordView extends View {
    public static final String LOCK = "lock";
    public static final String passfileName = "LocusPassword";
    private long CLEAR_TIME = 500;
    private boolean checking = false;

    /* renamed from: h */
    private float h = 0.0f;
    private boolean isCache = false;
    private boolean isTouch = true;
    private int lineAlpha = 50;
    private Bitmap locus_arrow;
    private Bitmap locus_line;
    private Bitmap locus_line_error;
    private Bitmap locus_line_semicircle;
    private Bitmap locus_line_semicircle_error;
    private Bitmap locus_round_click;
    private Bitmap locus_round_click_error;
    private Bitmap locus_round_original;
    private OnCompleteListener mCompleteListener;
    private Matrix mMatrix = new Matrix();
    private Paint mPaint = new Paint(1);
    private Point[][] mPoints = ((Point[][]) Array.newInstance(Point.class, new int[]{3, 3}));
    float moveingX;
    float moveingY;
    boolean movingNoPoint = false;
    private int passwordMinLength = 4;

    /* renamed from: r */
    private float f9676r = 0.0f;
    private List<Point> sPoints = new ArrayList();
    private TimerTask task = null;
    private Timer timer = new Timer();

    /* renamed from: w */
    private float w = 0.0f;

    public interface OnCompleteListener {
        void onComplete(String str);
    }

    public LocusPassWordView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public LocusPassWordView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LocusPassWordView(Context context) {
        super(context);
    }

    public void onDraw(Canvas canvas) {
        if (!this.isCache) {
            initCache();
        }
        drawToCanvas(canvas);
    }

    private void drawToCanvas(Canvas canvas) {
        for (int i = 0; i < this.mPoints.length; i++) {
            for (Point p : this.mPoints[i]) {
                if (p.state == Point.STATE_CHECK) {
                    canvas.drawBitmap(this.locus_round_click, p.f9678x - this.f9676r, p.f9679y - this.f9676r, this.mPaint);
                } else if (p.state == Point.STATE_CHECK_ERROR) {
                    canvas.drawBitmap(this.locus_round_click_error, p.f9678x - this.f9676r, p.f9679y - this.f9676r, this.mPaint);
                } else {
                    canvas.drawBitmap(this.locus_round_original, p.f9678x - this.f9676r, p.f9679y - this.f9676r, this.mPaint);
                }
            }
        }
        if (this.sPoints.size() > 0) {
            int tmpAlpha = this.mPaint.getAlpha();
            this.mPaint.setAlpha(this.lineAlpha);
            Point tp = this.sPoints.get(0);
            for (int i2 = 1; i2 < this.sPoints.size(); i2++) {
                Point p2 = this.sPoints.get(i2);
                drawLine(canvas, tp, p2);
                tp = p2;
            }
            if (this.movingNoPoint) {
                drawLine(canvas, tp, new Point((float) ((int) this.moveingX), (float) ((int) this.moveingY)));
            }
            this.mPaint.setAlpha(tmpAlpha);
            this.lineAlpha = this.mPaint.getAlpha();
        }
    }
    private void initCache() {
        this.w = getWidth();
        this.h = getHeight();
        float f1 = 0.0F;
        float f2 = 0.0F;
        if (this.w > this.h) {
            f1 = (this.w - this.h) / 2.0F;
            this.w = this.h;
        } else {
            f2 = (this.h - this.w) / 2.0F;
            this.h = this.w;
        }
        this.locus_round_original = BitmapFactory.decodeResource(getResources(), R.drawable.screenlock_round_original);
        this.locus_round_click = BitmapFactory.decodeResource(getResources(), R.drawable.screenlock_round_click);
        this.locus_round_click_error = BitmapFactory.decodeResource(getResources(), R.drawable.screenlock_round_click_error);
        this.locus_line = BitmapFactory.decodeResource(getResources(), R.drawable.screenlock_line);
        this.locus_line_semicircle = BitmapFactory.decodeResource(getResources(), R.drawable.screenlock_line_semicircle);
        this.locus_line_error = BitmapFactory.decodeResource(getResources(), R.drawable.screenlock_line_error);
        this.locus_line_semicircle_error = BitmapFactory.decodeResource(getResources(), R.drawable.screenlock_line_semicircle_error);
        this.locus_arrow = BitmapFactory.decodeResource(getResources(), R.drawable.screenlock_arrow);
        float f3 = this.w;
        if (this.w > this.h)
            f3 = this.h;
        float f4 = f3 / 8.0F * 2.0F;
        float f5 = f4 / 2.0F;
        f3 = f3 % 16.0F / 2.0F;
        f3 = f1 + f3 + f3;
        f1 = f5;
        if (this.locus_round_original.getWidth() > f4) {
            f1 = 1.0F * f4 / this.locus_round_original.getWidth();
            this.locus_round_original = BitmapUtil.zoom(this.locus_round_original, f1);
            this.locus_round_click = BitmapUtil.zoom(this.locus_round_click, f1);
            this.locus_round_click_error = BitmapUtil.zoom(this.locus_round_click_error, f1);
            this.locus_line = BitmapUtil.zoom(this.locus_line, f1);
            this.locus_line_semicircle = BitmapUtil.zoom(this.locus_line_semicircle, f1);
            this.locus_line_error = BitmapUtil.zoom(this.locus_line_error, f1);
            this.locus_line_semicircle_error = BitmapUtil.zoom(this.locus_line_semicircle_error, f1);
            this.locus_arrow = BitmapUtil.zoom(this.locus_arrow, f1);
            f1 = (this.locus_round_original.getWidth() / 2);
        }
        this.mPoints[0][0] = new Point(0.0F + f3 + f1, 0.0F + f2 + f1);
        this.mPoints[0][1] = new Point(this.w / 2.0F + f3, 0.0F + f2 + f1);
        this.mPoints[0][2] = new Point(this.w + f3 - f1, 0.0F + f2 + f1);
        this.mPoints[1][0] = new Point(0.0F + f3 + f1, this.h / 2.0F + f2);
        this.mPoints[1][1] = new Point(this.w / 2.0F + f3, this.h / 2.0F + f2);
        this.mPoints[1][2] = new Point(this.w + f3 - f1, this.h / 2.0F + f2);
        this.mPoints[2][0] = new Point(0.0F + f3 + f1, this.h + f2 - f1);
        this.mPoints[2][1] = new Point(this.w / 2.0F + f3, this.h + f2 - f1);
        this.mPoints[2][2] = new Point(this.w + f3 - f1, this.h + f2 - f1);
        byte b1 = 0;
        Point[][] arrayOfPoint = this.mPoints;
        int i = arrayOfPoint.length;
        byte b2 = 0;
        label24: while (true) {
            if (b2 >= i) {
                this.f9676r = (this.locus_round_original.getHeight() / 2);
                this.isCache = true;
                return;
            }
            Point[] arrayOfPoint1 = arrayOfPoint[b2];
            int j = arrayOfPoint1.length;
            for (byte b = 0;; b++) {
                if (b >= j) {
                    b2++;
                    continue label24;
                }
                (arrayOfPoint1[b]).index = b1;
                b1++;
            }
        }
    }


    private void drawLine(Canvas canvas, Point a, Point b) {
        float ah = (float) MathUtil.distance((double) a.f9678x, (double) a.f9679y, (double) b.f9678x, (double) b.f9679y);
        float degrees = getDegrees(a, b);
        canvas.rotate(degrees, a.f9678x, a.f9679y);
        if (a.state == Point.STATE_CHECK_ERROR) {
            this.mMatrix.setScale((ah - ((float) this.locus_line_semicircle_error.getWidth())) / ((float) this.locus_line_error.getWidth()), 1.0f);
            this.mMatrix.postTranslate(a.f9678x, a.f9679y - (((float) this.locus_line_error.getHeight()) / 2.0f));
            canvas.drawBitmap(this.locus_line_error, this.mMatrix, this.mPaint);
            canvas.drawBitmap(this.locus_line_semicircle_error, a.f9678x + ((float) this.locus_line_error.getWidth()), a.f9679y - (((float) this.locus_line_error.getHeight()) / 2.0f), this.mPaint);
        } else {
            this.mMatrix.setScale((ah - ((float) this.locus_line_semicircle.getWidth())) / ((float) this.locus_line.getWidth()), 1.0f);
            this.mMatrix.postTranslate(a.f9678x, a.f9679y - (((float) this.locus_line.getHeight()) / 2.0f));
            canvas.drawBitmap(this.locus_line, this.mMatrix, this.mPaint);
            canvas.drawBitmap(this.locus_line_semicircle, (a.f9678x + ah) - ((float) this.locus_line_semicircle.getWidth()), a.f9679y - (((float) this.locus_line.getHeight()) / 2.0f), this.mPaint);
        }
        canvas.drawBitmap(this.locus_arrow, a.f9678x, a.f9679y - (((float) this.locus_arrow.getHeight()) / 2.0f), this.mPaint);
        canvas.rotate(-degrees, a.f9678x, a.f9679y);
    }

    public float getDegrees(Point a, Point b) {
        float ax = a.f9678x;
        float ay = a.f9679y;
        float bx = b.f9678x;
        float by = b.f9679y;
        if (bx == ax) {
            if (by > ay) {
                return 90.0f;
            }
            if (by < ay) {
                return 270.0f;
            }
            return 0.0f;
        } else if (by == ay) {
            if (bx <= ax && bx < ax) {
                return 180.0f;
            }
            return 0.0f;
        } else if (bx > ax) {
            if (by > ay) {
                return 0.0f + switchDegrees(Math.abs(by - ay), Math.abs(bx - ax));
            }
            if (by < ay) {
                return 360.0f - switchDegrees(Math.abs(by - ay), Math.abs(bx - ax));
            }
            return 0.0f;
        } else if (bx >= ax) {
            return 0.0f;
        } else {
            if (by > ay) {
                return 90.0f + switchDegrees(Math.abs(bx - ax), Math.abs(by - ay));
            }
            if (by < ay) {
                return 270.0f - switchDegrees(Math.abs(bx - ax), Math.abs(by - ay));
            }
            return 0.0f;
        }
    }

    private float switchDegrees(float x, float y) {
        return (float) MathUtil.pointTotoDegrees((double) x, (double) y);
    }

    public int[] getArrayIndex(int index) {
        return new int[]{index / 3, index % 3};
    }

    private Point checkSelectPoint(float x, float y) {
        for (int i = 0; i < this.mPoints.length; i++) {
            for (Point p : this.mPoints[i]) {
                if (RoundUtil.checkInRound(p.f9678x, p.f9679y, this.f9676r, (float) ((int) x), (float) ((int) y))) {
                    return p;
                }
            }
        }
        return null;
    }

    public void reset() {
        for (Point p : this.sPoints) {
            p.state = Point.STATE_NORMAL;
        }
        this.sPoints.clear();
        enableTouch();
    }

    private int crossPoint(Point p) {
        if (!this.sPoints.contains(p)) {
            return 0;
        }
        if (this.sPoints.size() <= 2 || this.sPoints.get(this.sPoints.size() - 1).index == p.index) {
            return 1;
        }
        return 2;
    }

    private void addPoint(Point point) {
        this.sPoints.add(point);
    }

    private String toPointString() {
        if (this.sPoints.size() < this.passwordMinLength) {
            return "";
        }
        StringBuffer sf = new StringBuffer();
        for (Point p : this.sPoints) {
            sf.append(Constant.PARAMS_SQE);
            sf.append(p.index);
        }
        return sf.deleteCharAt(0).toString();
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (!this.isTouch) {
            return false;
        }
        this.movingNoPoint = false;
        float ex = event.getX();
        float ey = event.getY();
        boolean isFinish = false;
        Point p = null;
        switch (event.getAction()) {
            case 0:
                if (this.task != null) {
                    this.task.cancel();
                    this.task = null;
                    MobileLog.d("task", "touch cancel()");
                }
                reset();
                p = checkSelectPoint(ex, ey);
                if (p != null) {
                    this.checking = true;
                    break;
                }
                break;
            case 1:
                p = checkSelectPoint(ex, ey);
                this.checking = false;
                isFinish = true;
                break;
            case 2:
                if (this.checking && (p = checkSelectPoint(ex, ey)) == null) {
                    this.movingNoPoint = true;
                    this.moveingX = ex;
                    this.moveingY = ey;
                    break;
                }
        }
        if (!isFinish && this.checking && p != null) {
            int rk = crossPoint(p);
            if (rk == 2) {
                this.movingNoPoint = true;
                this.moveingX = ex;
                this.moveingY = ey;
            } else if (rk == 0) {
                p.state = Point.STATE_CHECK;
                addPoint(p);
            }
        }
        if (isFinish) {
            if (this.sPoints.size() == 1) {
                reset();
            } else if (this.sPoints.size() < this.passwordMinLength && this.sPoints.size() > 0) {
                error();
                clearPassword();
                Toast.makeText(getContext(), "密码太短,请重新输入!", Toast.LENGTH_SHORT).show();
            } else if (this.mCompleteListener != null && this.sPoints.size() >= this.passwordMinLength) {
                disableTouch();
                this.mCompleteListener.onComplete(toPointString());
            }
        }
        postInvalidate();
        return true;
    }

    private void error() {
        for (Point p : this.sPoints) {
            p.state = Point.STATE_CHECK_ERROR;
        }
    }

    public void markError() {
        markError(this.CLEAR_TIME);
    }

    public void markError(long time) {
        for (Point p : this.sPoints) {
            p.state = Point.STATE_CHECK_ERROR;
        }
        clearPassword(time);
    }

    public void enableTouch() {
        this.isTouch = true;
    }

    public void disableTouch() {
        this.isTouch = false;
    }

    public void clearPassword() {
        clearPassword(this.CLEAR_TIME);
    }

    public void clearPassword(long time) {
        if (time > 1) {
            if (this.task != null) {
                this.task.cancel();
                MobileLog.d("task", "clearPassword cancel()");
            }
            this.lineAlpha = 130;
            postInvalidate();
            this.task = new TimerTask() {
                public void run() {
                    LocusPassWordView.this.reset();
                    LocusPassWordView.this.postInvalidate();
                }
            };
            MobileLog.d("task", "clearPassword schedule(" + time + ")");
            this.timer.schedule(this.task, time);
            return;
        }
        reset();
        postInvalidate();
    }

    public void setOnCompleteListener(OnCompleteListener mCompleteListener2) {
        this.mCompleteListener = mCompleteListener2;
    }

    private String getPassword() {
        return getContext().getSharedPreferences("LocusPassword", 0).getString("password", "");
    }

    public boolean isPasswordEmpty() {
        return TextUtils.isEmpty(getPassword());
    }

    public boolean verifyPassword(String password) {
        if (TextUtils.isEmpty(password) || !MD5.hexDigest(password).equals(getPassword())) {
            return false;
        }
        return true;
    }

    public void resetPassWord(String password, String dataParam) {
        SharedPreferences.Editor editor = getContext().getSharedPreferences("LocusPassword", 0).edit();
        editor.putString("password", password);
        editor.putString("dataParam", dataParam);
        editor.putString("lockState", LOCK);
        editor.commit();
    }

    public void removePassWord() {
        SharedPreferences.Editor editor = getContext().getSharedPreferences("LocusPassword", 0).edit();
        editor.remove("password");
        editor.remove("dataParam");
        editor.commit();
    }

    public int getPasswordMinLength() {
        return this.passwordMinLength;
    }

    public void setPasswordMinLength(int passwordMinLength2) {
        this.passwordMinLength = passwordMinLength2;
    }
}