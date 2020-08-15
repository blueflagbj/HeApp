package com.wade.mobile.ui.util;

import android.content.Context;
import android.content.ContextWrapper;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import com.wade.mobile.util.res.ResUtil;

/* renamed from: com.wade.mobile.ui.util.UiTool */
public class UiTool {
    public static final String DRAWABLE = "drawable";

    public static final String ID = "id";
    public static final String LAYOUT = "layout";

    public static int getR(ContextWrapper context, String _class, String classAttr) {
        return ResUtil.getR(context, _class, classAttr);
    }

    public static int getScreenWidth(Context context) {
        DisplayMetrics outMetrics = new DisplayMetrics();
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    public static int getScreenHeight(Context context) {
        DisplayMetrics outMetrics = new DisplayMetrics();
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }
}