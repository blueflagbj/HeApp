package com.wade.mobile.util;

public class StringUtil {
    public static String join(String[] array, String delimiter) {
        StringBuilder buff = new StringBuilder();
        if (array != null) {
            for (String s : array) {
                buff.append(s).append(delimiter);
            }
            buff.setLength(buff.length() - delimiter.length());
        }
        return buff.toString();
    }

    public static boolean isJSONArray(String str) {
        return str.trim().startsWith("[") && str.trim().endsWith("]");
    }

    public static boolean isDataMap(String str) {
        return str.trim().startsWith("{") && str.trim().endsWith("}");
    }

    public static String[] mergeStringArray(String[] array1, String[] array2) {
        String[] merge = new String[(array1.length + array2.length)];
        System.arraycopy(array1, 0, merge, 0, array1.length);
        System.arraycopy(array2, 0, merge, array1.length, array2.length);
        return merge;
    }
}