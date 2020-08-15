package com.wade.mobile.util;

import com.wade.mobile.common.MobileException;
import com.wade.mobile.common.MobileLog;

public class Utility {
    public static String getBottomMessage(Throwable exception) {
        return getBottomException(exception).getMessage();
    }

    private static Throwable getBottomException(Throwable exception) {
        if (exception == null) {
            return null;
        }
        if (exception.getCause() != null) {
            return getBottomException(exception.getCause());
        }
        return exception;
    }

    public static void error(Throwable throwable) {
        if (throwable instanceof MobileException) {
            throw ((MobileException) throwable);
        }
        throw new MobileException(throwable);
    }

    public static void error(String message, Throwable throwable) {
        throw new MobileException(message, throwable);
    }

    public static void error(String message) {
        throw new MobileException(message);
    }

    public static void print(String TAG, Throwable throwable) {
        MobileException e;
        if (throwable instanceof MobileException) {
            e = (MobileException) throwable;
        } else {
            e = new MobileException(throwable);
        }
        MobileLog.e(TAG, e.getMessage());
    }
}