package com.wade.mobile.common.contacts.helper;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;

public class ChildBackgroundView extends Drawable {
    private int backgroundColor;
    private PointF from;
    private int lineColor;
    private Paint.Style style;

    /* renamed from: to */
    private PointF f9611to;
    private boolean withLine;

    public ChildBackgroundView() {
    }

    public ChildBackgroundView(PointF from2, PointF to) {
        this.from = from2;
        this.f9611to = to;
    }

    public Paint.Style getStyle() {
        return this.style;
    }

    public void setStyle(Paint.Style style2) {
        this.style = style2;
    }

    public boolean isWithLine() {
        return this.withLine;
    }

    public void setWithLine(boolean withLine2) {
        this.withLine = withLine2;
    }

    public int getLineColor() {
        return this.lineColor;
    }

    public void setLineColor(int lineColor2) {
        this.lineColor = lineColor2;
    }

    public int getBackgroundColor() {
        return this.backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor2) {
        this.backgroundColor = backgroundColor2;
    }

    public void draw(Canvas canvas) {
        canvas.drawColor(this.backgroundColor == 0 ? -1 : this.backgroundColor);
        if (this.withLine) {
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setColor(this.lineColor == 0 ? Color.parseColor("#A0A0A0") : this.lineColor);
            paint.setStyle(this.style == null ? Paint.Style.STROKE : this.style);
            paint.setStrokeWidth(1.0f);
            Path path = new Path();
            path.moveTo(this.from.x, this.from.y);
            path.lineTo(this.f9611to.x, this.f9611to.y);
            canvas.drawPath(path, paint);
        }
    }

    public void setAlpha(int alpha) {
    }

    public void setColorFilter(ColorFilter cf) {
    }

    public int getOpacity() {
        return PixelFormat.UNKNOWN;//PixelFormat.UNKNOWN
    }
}