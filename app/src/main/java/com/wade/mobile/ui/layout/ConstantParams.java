package com.wade.mobile.ui.layout;


import android.view.ViewGroup;
import com.wade.mobile.common.MobileException;
import com.wade.mobile.util.MobileRefleck;
import java.util.HashMap;
import java.util.Map;

public class ConstantParams {
    public static final int FILL_PARENT = -1;

    public static final int MATCH_PARENT = -1;

    public static final int WRAP_CONTENT = -2;

    private static Map<String, ViewGroup.LayoutParams> paramsMap = new HashMap<String, ViewGroup.LayoutParams>();

    public static <Type extends ViewGroup.LayoutParams> ViewGroup.LayoutParams getFillParams(Class<Type> paramClass) {
        return getLayoutParams(paramClass, -1, -1, true);
    }

    public static <Type extends ViewGroup.LayoutParams> ViewGroup.LayoutParams getFillParams(Class<Type> paramClass, boolean paramBoolean) {
        return getLayoutParams(paramClass, -1, -1, paramBoolean);
    }

    public static <Type extends ViewGroup.LayoutParams> ViewGroup.LayoutParams getHeightFillParams(Class<Type> paramClass) {
        return getLayoutParams(paramClass, -2, -1, true);
    }

    public static <Type extends ViewGroup.LayoutParams> ViewGroup.LayoutParams getHeightFillParams(Class<Type> paramClass, boolean paramBoolean) {
        return getLayoutParams(paramClass, -2, -1, paramBoolean);
    }

    public static <Type extends ViewGroup.LayoutParams> ViewGroup.LayoutParams getLayoutParams(Class<Type> paramClass, int paramInt1, int paramInt2, boolean paramBoolean) {
        String str = paramClass.getName() + paramInt1 + paramInt2;
        ViewGroup.LayoutParams layoutParams1 = paramsMap.get(str);
        ViewGroup.LayoutParams layoutParams2 = layoutParams1;
        if (layoutParams1 == null)
            try {
                ViewGroup.LayoutParams layoutParams = (ViewGroup.LayoutParams)MobileRefleck.newInstance(paramClass, new Object[] { Integer.valueOf(paramInt1), Integer.valueOf(paramInt2) });
                layoutParams2 = layoutParams;
                if (!paramBoolean) {
                    paramsMap.put(str, layoutParams);
                    layoutParams2 = layoutParams;
                }
                return layoutParams2;
            } catch (Exception exception) {
                exception.printStackTrace();
                throw new MobileException(exception);
            }
        return layoutParams2;
    }

    public static <Type extends ViewGroup.LayoutParams> ViewGroup.LayoutParams getWidthFillParams(Class<Type> paramClass) {
        return getLayoutParams(paramClass, -1, -2, true);
    }

    public static <Type extends ViewGroup.LayoutParams> ViewGroup.LayoutParams getWidthFillParams(Class<Type> paramClass, boolean paramBoolean) {
        return getLayoutParams(paramClass, -1, -2, paramBoolean);
    }

    public static <Type extends ViewGroup.LayoutParams> ViewGroup.LayoutParams getWrapParams(Class<Type> paramClass) {
        return getLayoutParams(paramClass, -2, -2, true);
    }

    public static <Type extends ViewGroup.LayoutParams> ViewGroup.LayoutParams getWrapParams(Class<Type> paramClass, boolean paramBoolean) {
        return getLayoutParams(paramClass, -2, -2, paramBoolean);
    }
}