package com.wade.mobile.ui.helper;

import android.content.Context;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import com.wade.mobile.common.MobileException;
import com.wade.mobile.ui.layout.ConstantParams;


public class LayoutHelper {
    private LayoutHelper() {
        throw new MobileException("LayoutHelper is a helper class,can't be initated");
    }

    public static FrameLayout createFrameLayout(Context paramContext) {
        FrameLayout frameLayout = new FrameLayout(paramContext);
        frameLayout.setLayoutParams(ConstantParams.getFillParams(FrameLayout.LayoutParams.class));
        return frameLayout;
    }

    public static LinearLayout createLinearLayout(Context paramContext) {
        LinearLayout linearLayout = new LinearLayout(paramContext);
        linearLayout.setLayoutParams(ConstantParams.getFillParams(LinearLayout.LayoutParams.class));
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        return linearLayout;
    }
}
