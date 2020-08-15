package com.wade.mobile.ui.comp.dialog;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/* renamed from: com.wade.mobile.ui.comp.dialog.YMPickerDialog */
public class YMPickerDialog extends DatePickerDialog {
    private final Calendar mCalendar = Calendar.getInstance();
    private final SimpleDateFormat mTitleDateFormat;

    public YMPickerDialog(Context context, OnDateSetListener callBack, int year, int monthOfYear, int dayOfMonth, SimpleDateFormat mTitleDateFormat2) {
        super(context, callBack, year, monthOfYear, dayOfMonth);
        this.mTitleDateFormat = mTitleDateFormat2;
    }

    public void onDateChanged(DatePicker view, int year, int month, int day) {
        super.onDateChanged(view, year, month, day);
        this.mCalendar.set(Calendar.YEAR, year);
        this.mCalendar.set(Calendar.MONTH, month);
        setTitle(this.mTitleDateFormat.format(this.mCalendar.getTime()));
    }

    public void show() {
        super.show();
        DatePicker dp = findDatePicker((ViewGroup) getWindow().getDecorView());
        if (dp != null) {
            try {
                Field f = dp.getClass().getDeclaredField("mDaySpinner");
                f.setAccessible(true);
                ((LinearLayout) f.get(dp)).setVisibility(View.GONE);
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e2) {
                e2.printStackTrace();
            } catch (IllegalArgumentException e3) {
                e3.printStackTrace();
            } catch (IllegalAccessException e4) {
                e4.printStackTrace();
            }
        }
    }

    private DatePicker findDatePicker(ViewGroup group) {
        DatePicker result;
        if (group != null) {
            int j = group.getChildCount();
            for (int i = 0; i < j; i++) {
                View child = group.getChildAt(i);
                if (child instanceof DatePicker) {
                    return (DatePicker) child;
                }
                if ((child instanceof ViewGroup) && (result = findDatePicker((ViewGroup) child)) != null) {
                    return result;
                }
            }
        }
        return null;
    }
}