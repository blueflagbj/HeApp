package com.ai.cmcchina.crm.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import com.tencent.tinker.android.p131dx.instruction.Opcodes;
import java.util.Random;

/* renamed from: com.ai.cmcchina.crm.view.VerifyCodeView */
public class VerifyCodeView extends View {
    private Bitmap bitmap;
    private String code;
    private boolean flag = false;

    public String getCode() {
        return this.code;
    }

    public void setCode(String code2) {
        this.code = code2;
    }

    public VerifyCodeView(Context context) {
        super(context);
    }

    public VerifyCodeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VerifyCodeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        if (!this.flag) {
            generateCodeBitmap();
        }
        canvas.drawBitmap(this.bitmap, 0.0f, 0.0f, (Paint) null);
        this.flag = true;
    }

    private void generateCodeBitmap() {
        int a;
        if (this.bitmap != null) {
            this.bitmap.recycle();
            this.bitmap = null;
        }
        this.bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(this.bitmap);
        canvas.drawColor(-1);
        canvas.save();
        canvas.translate(10.0f, 0.0f);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        float textSize = (float) ((getHeight() * 3) / 5);
        paint.setTextSize(textSize);
        paint.setTextAlign(Paint.Align.CENTER);
        Random random = new Random();
        String sRand = "";
        float x = (float) (getWidth() / 7);
        for (int i = 0; i < 6; i++) {
            int a2 = random.nextInt(41);
            if (a2 < 9) {
                a = a2 + 49;
            } else if (a2 < 25) {
                a = a2 + 56;
            } else {
                a = ((a2 + 97) - 16) - 9;
            }
            String rand = String.valueOf((char) a);
            paint.setColor(Color.rgb(random.nextInt(Opcodes.OR_INT_LIT8), random.nextInt(Opcodes.OR_INT_LIT8), random.nextInt(Opcodes.OR_INT_LIT8)));
            float degrees = 30.0f * random.nextFloat() * ((float) (random.nextInt(2) > 0 ? 1 : -1));
            float dx = x - ((float) (getWidth() / 22));
            float dy = generateY(textSize);
            canvas.rotate(degrees, dx, dy);
            canvas.drawText(rand, dx, dy, paint);
            canvas.rotate(-degrees, dx, dy);
            sRand = sRand + rand;
            x += (float) (getWidth() / 7);
        }
        setCode(sRand);
        paint.setStrokeWidth(2.0f);
        paint.setColor(-16777216);
        for (int i2 = 0; i2 < 3; i2++) {
            canvas.drawLine(((float) getWidth()) * random.nextFloat(), ((float) getHeight()) * random.nextFloat(), ((float) getWidth()) * random.nextFloat(), ((float) getHeight()) * random.nextFloat(), paint);
        }
        canvas.restore();
    }

    private float generateY(float textSize) {
        return ((4.0f * textSize) / 5.0f) + ((float) (Math.random() * ((double) (((float) getHeight()) - textSize))));
    }

    public void refresh() {
        this.flag = false;
        invalidate();
    }
}