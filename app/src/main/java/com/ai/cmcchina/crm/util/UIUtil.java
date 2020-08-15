package com.ai.cmcchina.crm.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.ai.cmcchina.crm.listener.AfterListener;
import com.ai.cmcchina.crm.listener.CalendarSelectListener;
import com.ai.cmcchina.crm.view.CustomerValidateNumMsgDialog;
import com.heclient.heapp.R;

import java.io.IOException;
import java.util.Calendar;

import static com.wade.mobile.util.assets.AssetsUtil.TAG;

/* renamed from: com.ai.cmcchina.crm.util.UIUtil */
public class UIUtil {
    public static final int largeTextSize = 28;
    public static final int mediumTextSize = 24;
    public static final int smallTextSize = 18;
    public static final int twentyTextSize = 20;

    /* renamed from: com.ai.cmcchina.crm.util.UIUtil$DialogCallback */
    public interface DialogCallback {
        void callback(int i);
    }

    public static void toast(Context context, Object message) {
        Toast toast = new Toast(context);
        LinearLayout view = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.toast, (ViewGroup) null);
        TextView tv = (TextView) view.getChildAt(0);
        if (message != null) {
            tv.setText(message.toString());
        } else {
            tv.setText("null");
        }
        toast.setView(view);
        toast.setGravity(81, 0, 200);
        toast.show();
    }

    public static void toast(Context context, int resId) {
        Toast toast = new Toast(context);
        LinearLayout view = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.toast, (ViewGroup) null);
        ((TextView) view.getChildAt(0)).setText(resId);
        toast.setView(view);
        toast.setGravity(17, 0, 0);
        toast.show();
    }

    @SuppressLint("ResourceType")
    public static void alert(Context context, String message) {
        new AlertDialog.Builder(context).setTitle("警告").setMessage(message).setPositiveButton("确认", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                arg0.dismiss();
            }
        }).setCancelable(false).create().show();
    }

    @SuppressLint("ResourceType")
    public static void alert(Context context, String message, DialogInterface.OnClickListener onClickListener) {
        new AlertDialog.Builder(context).setTitle("警告").setMessage(message).setPositiveButton("确认", onClickListener).setCancelable(false).create().show();
    }

    @SuppressLint("ResourceType")
    public static void alert(Context context, int resId) {
        new AlertDialog.Builder(context).setTitle("警告").setMessage(resId).setPositiveButton("确认", (DialogInterface.OnClickListener) null).setCancelable(false).create().show();
    }

    public static ProgressDialog displayWaiting(Context context, String message) {
        ProgressDialog dialog = ProgressDialog.show(context, "警告", message, true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);
        return dialog;
    }

    public static void dismissWaiting(ProgressDialog pDlg) {
        if (pDlg != null && pDlg.isShowing()) {
            pDlg.dismiss();
        }
    }

    public static int getFinalSize(Context context, float size) {
        return (int) (DeviceUtil.getScreenRate((Activity) context) * size);
    }

    public static int px2Dip(Context context, float px) {
        return (int) TypedValue.applyDimension(1, px, context.getResources().getDisplayMetrics());
    }

    public static TextView makeImageTextView(Context context, int imgId, int txtId, int imgSize, int txtColor) {
        TextView tv = new TextView(context);
        Drawable top = context.getResources().getDrawable(imgId);
        top.setBounds(0, 0, getFinalSize(context, (float) imgSize), getFinalSize(context, (float) imgSize));
        tv.setCompoundDrawables((Drawable) null, top, (Drawable) null, (Drawable) null);
        tv.setText(txtId);
        tv.setTextSize(0, (float) getFinalSize(context, 18.0f));
        tv.setTextColor(txtColor);
        tv.setGravity(17);
        tv.setCompoundDrawablePadding(getFinalSize(context, 7.0f));
        return tv;
    }

    public static TextView makeImageTextView(Context context, String imgUrl, String txt, int imgSize, int txtColor) {
        Drawable top;
        TextView tv = new TextView(context);
        Resources res = context.getResources();
        try {
            top = Drawable.createFromStream(res.getAssets().open("images/" + imgUrl), imgUrl);
        } catch (IOException e) {
            Log.e(TAG, "makeImageTextView: ",e );;
            top = res.getDrawable(R.drawable.ic_launcher);
        }
        top.setBounds(0, 0, getFinalSize(context, (float) imgSize), getFinalSize(context, (float) imgSize));
        tv.setCompoundDrawables((Drawable) null, top, (Drawable) null, (Drawable) null);
        tv.setText(txt);
        tv.setTextSize(0, (float) getFinalSize(context, 18.0f));
        tv.setTextColor(txtColor);
        tv.setGravity(17);
        tv.setCompoundDrawablePadding(getFinalSize(context, 7.0f));
        tv.setSingleLine(true);
        tv.setEllipsize(TextUtils.TruncateAt.END);
        return tv;
    }

    public static ListView makeListView(Context context) {
        ListView listView = new ListView(context);
        listView.setLayoutParams(new ViewGroup.LayoutParams(-1, -2));
        return listView;
    }

    public static ExpandableListView makeExpandableListView(Context context) {
        ExpandableListView listView = new ExpandableListView(context);
        listView.setLayoutParams(new ViewGroup.LayoutParams(-1, -2));
        return listView;
    }

    public static EditText makeFormTextView(Context context) {
        EditText et = new EditText(context);
        et.setLayoutParams(new LinearLayout.LayoutParams(0, getFinalSize(context, 50.0f), 1.0f));
        et.setTextSize(0, (float) getFinalSize(context, 18.0f));
        return et;
    }

    public static int getImageHeight(Context context, int imageId) {
        return ((BitmapDrawable) context.getResources().getDrawable(imageId)).getBitmap().getHeight();
    }

    public static void showCalendarDialog(Context context, final CalendarSelectListener listener) {
        Calendar c = Calendar.getInstance();
        Context context2 = context;
        new DatePickerDialog(context2, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                listener.dateSelect(view, year, monthOfYear, dayOfMonth);
            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE)).show();
    }

    public static void showConfirmDialog(Context context, String msg, boolean isShowNegativeBtn, final AfterListener listener) {
        CustomerValidateNumMsgDialog.Builder builder = new CustomerValidateNumMsgDialog.Builder(context,R.layout.dialog_customer_msg);
        builder.setTitle("提示");
        builder.setMessage(msg);
        builder.setPositiveButton("确定", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                listener.doAction(new Object[0]);
            }
        });
        if (isShowNegativeBtn) {
            builder.setNegativeButton("取消", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    listener.doAction(new Object[0]);
                }
            });
        }
        builder.create().show();
    }

    public static void alert(Context context, String message, DialogInterface.OnClickListener positiveButton, DialogInterface.OnClickListener negativeButton) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("提示");
        builder.setMessage(message);
        if (positiveButton != null) {
            builder.setPositiveButton("确定", positiveButton);
        }
        if (negativeButton != null) {
            builder.setNegativeButton("取消", negativeButton);
        }
        builder.setCancelable(false).create().show();
    }

    public static void alert(Context context, String title, String msg, String positiveText, String negativeText, DialogInterface.OnClickListener positiveButton, DialogInterface.OnClickListener negativeButton) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (!StringUtil.isNotBlank(title)) {
            title = "提示";
        }
        builder.setTitle(title);
        builder.setMessage(msg);
        if (positiveButton != null) {
            if (!StringUtil.isNotBlank(positiveText)) {
                positiveText = "确定";
            }
            builder.setPositiveButton(positiveText, positiveButton);
        }
        if (negativeButton != null) {
            if (!StringUtil.isNotBlank(negativeText)) {
                negativeText = "取消";
            }
            builder.setNegativeButton(negativeText, negativeButton);
        }
        builder.setCancelable(false).create().show();
    }

    public static void singleChoiceItemsAlert(Context context, String[] items, String title, int positon, final DialogCallback dialogCallback) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }
        builder.setSingleChoiceItems(items, positon, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();
                dialogCallback.callback(i);
            }
        });
        AlertDialog alert = builder.create();
        alert.setCanceledOnTouchOutside(true);
        alert.show();
    }
}