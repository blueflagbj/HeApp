package com.wade.mobile.common.contacts.helper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.widget.Button;
import com.wade.mobile.common.contacts.setting.ContactsSettings;
import com.wade.mobile.common.contacts.setting.TypeBarViewSettings;
import com.wade.mobile.common.contacts.util.TypefaceTool;
import java.util.ArrayList;

@SuppressLint("AppCompatCustomView")
public class TypeBarView extends Button {
    public static final String TYPE_FIRST = "☆";
    public static final String TYPE_NONE = "↑";
    public static final String TYPE_NOTYPE = "#";
    private int interval;
    private Paint paint;
    private int selectIndex = -1;
    private int space;
    private TypeBarViewSettings tSettings;
    private IOnTouchTypeBarListener touchTypeBarListener;
    private ArrayList<String> typesList;

    public ArrayList<String> getTypesList() {
        return this.typesList;
    }

    public TypeBarView(Context context, boolean hasNoneType, ContactsSettings settings) {
        super(context);
        this.tSettings = settings.getTypeBarViewSettings();
        this.typesList = new ArrayList<>();
        this.paint = new Paint();
        if (hasNoneType) {
            this.typesList.add(TYPE_NONE);
        }
        this.typesList.add(TYPE_FIRST);
        this.typesList.add("A");
        this.typesList.add("B");
        this.typesList.add("C");
        this.typesList.add("D");
        this.typesList.add("E");
        this.typesList.add("F");
        this.typesList.add("G");
        this.typesList.add("H");
        this.typesList.add("I");
        this.typesList.add("J");
        this.typesList.add("K");
        this.typesList.add("L");
        this.typesList.add("M");
        this.typesList.add("N");
        this.typesList.add("O");
        this.typesList.add("P");
        this.typesList.add("Q");
        this.typesList.add("R");
        this.typesList.add("S");
        this.typesList.add("T");
        this.typesList.add("U");
        this.typesList.add("V");
        this.typesList.add("W");
        this.typesList.add("X");
        this.typesList.add("Y");
        this.typesList.add("Z");
        this.typesList.add(TYPE_NOTYPE);
    }

    public void setOnTouchTypeBarListener(IOnTouchTypeBarListener touchTypeBarListener2) {
        this.touchTypeBarListener = touchTypeBarListener2;
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int height = getHeight();
        int width = getWidth();
        this.interval = height / (this.typesList.size() + 1);
        this.space = ((height - (this.interval * (this.typesList.size() + 1))) / 2) + this.interval;
        int length = this.typesList.size();
        for (int i = 0; i < length; i++) {
            this.paint.setAntiAlias(true);
            this.paint.setTypeface(TypefaceTool.getTypeface(this.tSettings.getTbNormalTextTypeface()));
            this.paint.setColor(this.tSettings.getTbNormalTextColor());
            this.paint.setTextSize((float) ((int) (((double) this.interval) * this.tSettings.getTbNormalTextScale())));
            if (i == this.selectIndex) {
                this.paint.setColor(this.tSettings.getTbPressedTextColor());
                this.paint.setFakeBoldText(this.tSettings.isTbPressedFakeBoldText());
                this.paint.setTextSize((float) ((int) (((double) this.interval) * this.tSettings.getTbPressedTextScale())));
            }
            canvas.drawText(this.typesList.get(i), ((float) (width / 2)) - (this.paint.measureText(this.typesList.get(i)) / 2.0f), (float) ((this.interval * i) + this.space), this.paint);
            this.paint.reset();
        }
    }

    public boolean dispatchTouchEvent(MotionEvent event) {
        int index = (int) (((event.getY() - ((float) this.space)) + ((float) this.interval)) / ((float) this.interval));
        if (index >= 0 && index < this.typesList.size()) {
            switch (event.getAction()) {
                case 0:
                    this.selectIndex = index;
                    if (this.touchTypeBarListener != null) {
                        this.touchTypeBarListener.onTouch(this.typesList.get(this.selectIndex));
                    }
                    setBackgroundColor(this.tSettings.getTbViewPressedBgColor());
                    break;
                case 1:
                    if (this.touchTypeBarListener != null) {
                        this.touchTypeBarListener.onTouchUP();
                    }
                    this.selectIndex = -1;
                    setBackgroundColor(this.tSettings.getTbViewNormalBgColor());
                    break;
                case 2:
                    if (this.selectIndex != index) {
                        this.selectIndex = index;
                        if (this.touchTypeBarListener != null) {
                            this.touchTypeBarListener.onTouch(this.typesList.get(this.selectIndex));
                        }
                        setBackgroundColor(this.tSettings.getTbViewPressedBgColor());
                        break;
                    }
                    break;
            }
        } else {
            this.selectIndex = -1;
            if (this.touchTypeBarListener != null) {
                this.touchTypeBarListener.onTouchUP();
                setBackgroundColor(this.tSettings.getTbViewNormalBgColor());
            }
        }
        invalidate();
        return true;
    }
}