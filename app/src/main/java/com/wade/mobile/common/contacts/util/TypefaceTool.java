package com.wade.mobile.common.contacts.util;

import android.graphics.Typeface;

public class TypefaceTool {
    public static final int DEFAULT = 65521;
    public static final int DEFAULT_BOLD = 65522;
    public static final int SERIF = 65523;

    public static Typeface getTypeface(int type) {
        switch (type) {
            case 65521:
                return Typeface.DEFAULT;
            case 65522:
                return Typeface.DEFAULT_BOLD;
            case SERIF /*65523*/:
                return Typeface.SERIF;
            default:
                return Typeface.DEFAULT;
        }
    }
}