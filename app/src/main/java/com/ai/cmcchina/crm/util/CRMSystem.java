package com.ai.cmcchina.crm.util;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/* renamed from: com.ai.cmcchina.crm.util.CRMSystem */
public class CRMSystem {
    public static void hideIME(Activity context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        View view = context.getCurrentFocus();
        if (view != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 2);
        }
    }

    public static void hideIME(Activity context, View focus) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (focus != null) {
            imm.hideSoftInputFromWindow(focus.getWindowToken(), 2);
        }
    }

    public static void showIME(Activity context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        View view = context.getCurrentFocus();
        if (view != null) {
            imm.showSoftInput(view, 1);
        }
    }

    public static void showIME(Activity context, View focus) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (focus != null) {
            imm.showSoftInput(focus, 1);
        }
    }
}