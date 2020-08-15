package com.wade.mobile.util.res;

import android.content.ContextWrapper;
import com.wade.mobile.common.MobileException;
import com.wade.mobile.util.MobileRefleck;

public class ResUtil {
    public static int getR(ContextWrapper context, String _class, String classAttr) {
        try {
            Object obj = MobileRefleck.getStaticProperty(context.getPackageName() + ".R$" + _class, classAttr);
            if (obj == null) {
                return 0;
            }
            return Integer.parseInt(obj.toString());
        } catch (Exception e) {
            throw new MobileException((Throwable) e);
        }
    }
}